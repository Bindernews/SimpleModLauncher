package com.github.vortexellauncher;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.exceptions.ValidationException;
import com.github.vortexellauncher.gui.MainFrame;
import com.github.vortexellauncher.gui.OfflineDialog;
import com.github.vortexellauncher.gui.Res;
import com.github.vortexellauncher.launch.MinecraftLauncher;
import com.github.vortexellauncher.pack.FileStatus;
import com.github.vortexellauncher.pack.ModFile;
import com.github.vortexellauncher.pack.ModType;
import com.github.vortexellauncher.pack.Modpack;
import com.github.vortexellauncher.pack.PackCache;
import com.github.vortexellauncher.util.ErrorUtils;
import com.github.vortexellauncher.util.Utils;
import com.github.vortexellauncher.workers.GameUpdateWorker;
import com.github.vortexellauncher.workers.LoginWorker;
import com.github.vortexellauncher.workers.PackValidator;
import com.google.gson.JsonElement;

/**
 * Controls the flow of the program
 * @author Bindernews
 *
 */
public class Launchpad {

	private static Launchpad instance = null;
	private MainFrame frame;
	private LoginResponse RESPONSE = null;
	private Settings settings;
	private ExecutorService driverThread = Executors.newSingleThreadExecutor();
	private ExecutorService workThread = Executors.newSingleThreadExecutor();
	private File lastLoginFile = new File(OS.dataDir(), "loginlast");
	
	private Launchpad() {
		settings = new Settings();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame = new MainFrame();
				frame.setVisible(true);
				try {
					String lastLoginStr = Utils.simpleCryptIn(lastLoginFile);
					if (lastLoginStr != null) {
						/*String[] lines = lastLoginStr.split("\n");
						UserPass[] uplist = new UserPass[lines.length];
						for(int i=0; i<lines.length; i++) {
						}
						frame.getLoginPanel().setUserPassArray(unpw[0][0], unpw[0][1]);*/
						UserPass up = new UserPass(lastLoginStr);
						frame.getLoginPanel().setUserPass(up);
						frame.getLoginPanel().getChkRemember().setSelected(true);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void attemptLogin(final String username, final String password, final boolean save) {
		if (username.length() == 0) {
			frame.setLoginStatus("No username");
			return;
		}
		frame.setEnabled(false);
		LoginWorker worker = new LoginWorker(username, password) {
			protected void done() {
				workThread.execute(new Runnable() {
					public void run() {
						onDone();
					}
				});
			}
			public void onDone() {
				String responseStr = null;
				try {
					responseStr = get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					if (e.getCause() instanceof IOException
							|| e.getCause() instanceof MalformedURLException) {
						OfflineDialog playOffline = new OfflineDialog(frame);
						playOffline.setVisible(true);
					}
					return;
				}
				try {
					RESPONSE = new LoginResponse(responseStr);
					frame.setLoginStatus("Login succeded");
					if (save) {
						try {
							UserPass up = new UserPass(username, password);
							Utils.simpleCryptOut(up.combine(), lastLoginFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				catch (IllegalArgumentException e) {
					if (responseStr.contains(":")) {
						// log Invalid server response
					}
					else {
						if (responseStr.equalsIgnoreCase("bad login")) {
							frame.setLoginStatus("Bad login");
						}
						else if (responseStr.equalsIgnoreCase("old version")) {
							frame.setLoginStatus("Outdated launcher");
						}
						else if (responseStr.startsWith("Account migrated, use e-mail")) {
							frame.setLoginStatus("Login using your email account");
						}
						else {
							frame.setLoginStatus(responseStr);
							new OfflineDialog(frame).setVisible(true);
						}
					}
					frame.setEnabled(true);
					return;
				}
				// log "Login complete"
				runLaunchGame();
			}
		};
		worker.execute();
	}
	
	public void playOffline() {
		String user = frame.getLoginPanel().getUsername();
		RESPONSE = new LoginResponse("0: :" + user + ": ");
		runLaunchGame();
	}
	
	private static enum ProgressState {
		None,
		CheckingForUpdates,
		ReadingCache,
		Validating,
		Updating,
		Revalidating,
		Launching
	}
	
	protected void runLaunchGame() {
		driverThread.submit(new Runnable() {
			public void run() {
				launchGame();
			}
		});
	}

	protected void launchGame() {
		boolean offline = RESPONSE.sessionID.equals("0");
		PackCache cache;
		PackValidator validator;
		List<ModFile> fileList;
		List<FileStatus> statusList;
		EnumSet<FileStatus> statusSet, newStatusSet;
		GameUpdateWorker updateWorker;
		ProgressState progress = ProgressState.None;
		VersionData nextVersion;
		JsonElement nextJson;
		try {
			progress = ProgressState.ReadingCache;
			cache = new PackCache(settings.getModpack());
			fileList = new ArrayList<ModFile>(cache.getModpack().getMods());
			populateWithMCCoreFiles(cache, fileList);
			
			/*
			if (settings.getModpack().getUpdateURL() != null) {
				progress = ProgressState.CheckingForUpdates;
				nextJson = Utils.readJsonFile(new URL(settings.getModpack().getUpdateURL()));
			}
			*/
			
			progress = ProgressState.Validating;
			validator = new PackValidator(cache, fileList);
			statusList = validator.runHere();
			statusSet = EnumSet.copyOf(statusList);
			
			if (offline) {
				if (!FileStatus.StatusCouldPlay.containsAll(statusSet)) { // there are files with status cannotPlay
					throw new ValidationException("Can not update files while in offline mode");
				}
			} else {
				progress = ProgressState.Updating;
				if (setsOverlap(statusSet, FileStatus.StatusDoDownload)) {
					updateWorker = new GameUpdateWorker(cache, fileList, statusList);
					updateWorker.execute();
					updateWorker.get();
					
					/*
					progress = ProgressState.Revalidating;
					validator = new PackValidator(cache, fileList);
					newStatusSet = EnumSet.copyOf(validator.runHere());
					if (!EnumSet.of(FileStatus.Valid).containsAll(newStatusSet)) {
						throw new ValidationException("Revalidation failed");
					}
					*/
				}
			}
			progress = ProgressState.Launching;
			MinecraftLauncher.launchMinecraft(settings.getModpack(), RESPONSE.username, RESPONSE.sessionID);
			frame.dispose();
		} catch (ExecutionException e) {
			ErrorUtils.showException("Exception occured while " + progress.name(), e.getCause(), true);
			e.printStackTrace();
		} catch (IOException | NullPointerException | ValidationException | InterruptedException e) {
			ErrorUtils.showException("Exception occured while " + progress.name(), e, true);
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	@SuppressWarnings("rawtypes")
	protected static boolean setsOverlap(Set a, Set b) {
		if (a.size() < b.size()) {
			for(Object o : a) {
				if (b.contains(o))
					return true;
			}
			return false;
		} else {
			for(Object o : b) {
				if (a.contains(o))
					return true;
			}
			return false;
		}
	}
	
	public static void main(String[] args) throws InvalidModpackException {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				ErrorUtils.showException(e, true);
			}
		});
		try {
			Res.init();
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (IOException ioe) {
			//TODO crash message
		} catch (UnsupportedLookAndFeelException e) { // it really doesn't matter if setting the L&F fails 
		}
		
		// create the launchpad and start the program
		try {
			instance = new Launchpad();
			JsonElement elem = Utils.readJsonFile(Res.getURL("res/vortexel_pack.json"));
			Modpack pack = new Modpack(elem.getAsJsonObject(), "vortexel_pack.json");
			//JsonElement elem = Utils.readJsonFile(Res.getURL("res/modpack.json"));
			//Modpack pack = new Modpack(elem.getAsJsonObject(), "modpack.json");
			instance.settings.setModpack(pack);
		} catch (IOException e) {
			ErrorUtils.showException(e, true);
		}
	}
	
	protected void populateWithMCCoreFiles(PackCache packData, List<ModFile> fileList) {
		String baseurl = "https://s3.amazonaws.com/MinecraftDownload/";
		String[] binFileNames = {"jinput.jar", "lwjgl.jar", "lwjgl_util.jar"};
		for(String bfn : binFileNames) {
			ModFile mf = new ModFile(bfn, ModType.Bin, baseurl + bfn);
			mf.setVersion(new VersionData(0));
			fileList.add(mf);
		}
		ModFile mcJar = new ModFile("minecraft.jar", ModType.Bin, packData.getModpack().mcversion.getJarUrl());
		mcJar.setVersion(new VersionData(0));
		ModFile mcNatives = new ModFile(OS.nativesJar(), ModType.Native, baseurl + OS.nativesJar());
		mcNatives.setVersion(new VersionData(0));
		fileList.add(mcJar);
		fileList.add(mcNatives);
	}
	
	public void resetLogin() {
		RESPONSE = null;
	}
	
	public static Launchpad i() {
		return instance;
	}
	public static MainFrame frame() {
		return instance.frame;
	}
	public static Settings getSettings() {
		return instance.settings;
	}
}
