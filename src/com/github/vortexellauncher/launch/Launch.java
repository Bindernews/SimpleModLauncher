package com.github.vortexellauncher.launch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import com.github.vortexellauncher.Log;
import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.OSInfo;
import com.github.vortexellauncher.UserPass;
import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.exceptions.JsonValidationException;
import com.github.vortexellauncher.exceptions.ValidationException;
import com.github.vortexellauncher.gui.dialogs.OfflineDialog;
import com.github.vortexellauncher.pack.FileStatus;
import com.github.vortexellauncher.pack.ModFile;
import com.github.vortexellauncher.pack.ModType;
import com.github.vortexellauncher.pack.Modpack;
import com.github.vortexellauncher.pack.PackCache;
import com.github.vortexellauncher.util.ErrorUtils;
import com.github.vortexellauncher.util.JsonUtils;
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
public class Launch {
	
	public static enum ProgressState {
		None,
		CheckingForUpdates,
		ReadingCache,
		Validating,
		Updating,
		Revalidating,
		Launching;
		
		public void log(Level level) {
			Log.log(level, "Launch status: " + name());
		}
	}
	
	private LoginResponse RESPONSE = null;
	private ExecutorService driverThread = Executors.newSingleThreadExecutor();
	private ProgressState progress = ProgressState.None;
	
	public Launch() {
		
	}
	
	public void attemptLogin(final String username, final String password, final boolean save) {
		if (username.length() == 0) {
			Main.frame().getMainPanel().setLoginStatus("No username");
			return;
		}
		Main.frame().setEnabled(false);
		LoginWorker worker = new LoginWorker(username, password) {
			protected void done() {
				driverThread.execute(new Runnable() {
					public void run() {
						loginWorkerDone();
					}
				});
			}
			protected void loginWorkerDone() {
				String responseStr = null;
				try {
					responseStr = get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					if (e.getCause() instanceof IOException
							|| e.getCause() instanceof MalformedURLException) {
						OfflineDialog playOffline = new OfflineDialog(Main.frame());
						playOffline.setVisible(true);
					}
					return;
				}
				try {
					RESPONSE = new LoginResponse(responseStr);
					Main.frame().getMainPanel().setLoginStatus("Login succeded");
					if (save) {
						try {
							UserPass up = new UserPass(username, password);
							Utils.simpleCryptOut(up.combine(), Main.lastLoginFile);
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
							Main.frame().getMainPanel().setLoginStatus("Bad login");
						}
						else if (responseStr.equalsIgnoreCase("old version")) {
							Main.frame().getMainPanel().setLoginStatus("Outdated launcher");
						}
						else if (responseStr.startsWith("Account migrated, use e-mail")) {
							Main.frame().getMainPanel().setLoginStatus("Login using your email account");
						}
						else {
							Main.frame().getMainPanel().setLoginStatus(responseStr);
							new OfflineDialog(Main.frame()).setVisible(true);
						}
					}
					Main.frame().setEnabled(true);
					return;
				}
				// log "Login complete"
				runLaunchGame();
			}
		};
		worker.execute();
	}
	
	
	
	public void playOffline() {
		String user = Main.frame().getMainPanel().getUsername();
		RESPONSE = new LoginResponse("0: :" + user + ": ");
		runLaunchGame();
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
		EnumSet<FileStatus> statusSet;
		GameUpdateWorker updateWorker;
		Modpack modpack;
		Throwable error = null;
		
		logProgress(ProgressState.None);
		Main.frame().getOptionsGui().updateToSettings();
		try {
			logProgress(ProgressState.ReadingCache);
			modpack = Main.metaManager().loadModpack(Main.settings().getModpackName());
			if (modpack.getUpdateURL() != null) {
				logProgress(ProgressState.CheckingForUpdates);
				try {
					JsonElement nextJson = JsonUtils.readJsonURL(new URL(modpack.getUpdateURL()));
					Modpack nextPack = new Modpack(nextJson.getAsJsonObject(), modpack.getUpdateURL());
					if (nextPack.version.compareTo(modpack.version) > 0) {
						Main.metaManager().updatePack(nextJson.getAsJsonObject(), modpack.name);
						modpack = nextPack;
					}
				} catch (IOException e) {
					System.err.println("Failed to check for modpack update");
					e.printStackTrace();
				}
			}
			cache = new PackCache(modpack);
			fileList = new ArrayList<ModFile>(cache.getModpack().getMods());
			populateWithMCCoreFiles(cache, fileList);
			
			if (Main.settings().shouldValidate()) {
				logProgress(ProgressState.Validating);
				validator = new PackValidator(cache, fileList);
				statusList = validator.runHere();
				statusSet = EnumSet.copyOf(statusList);
				
				if (offline) {
					if (!FileStatus.StatusCouldPlay.containsAll(statusSet)) { // there are files with status cannotPlay
						throw new ValidationException("Can not update files while in offline mode");
					}
				} else {
					logProgress(ProgressState.Updating);
					if (setsOverlap(statusSet, FileStatus.StatusDoDownload)) {
						updateWorker = new GameUpdateWorker(modpack, cache, fileList, statusList);
						updateWorker.execute();
						updateWorker.get();
					}
				}
			} else {
				Log.warning("Skiping validation and update steps. Minecraft directory will NOT BE CHECKED.");
			}
			logProgress(ProgressState.Launching);
			try {
				MinecraftLauncher.launchMinecraft(modpack, RESPONSE.username, RESPONSE.sessionID);
			} catch (Exception e) {
				error = e;
			}
		} catch (ExecutionException e) {
			error = e.getCause();
		} catch (IOException e) {
			error = e;
		} catch (NullPointerException e) {
			error = e;
		} catch (ValidationException e) {
			error = e;
		} catch (InterruptedException e) {
			error = e;
		} catch (JsonValidationException e) {
			error = e;
		}
		if (error != null) {
			ErrorUtils.showErrorGui(null, error.getMessage(), ErrorUtils.getExceptionString(error), true);
			//ErrorUtils.printException("Exception occured while " + progress.name(), error, false);
		}
		Main.frame().dispose();
		Main.attemptExit();
	}
	
	protected void logProgress(ProgressState p) {
		progress = p;
		Main.frame().getMainPanel().setLoginStatus(progress.name());
		progress.log(Level.INFO);
		
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
		ModFile mcNatives = new ModFile(OSInfo.nativesJar(), ModType.Native, OSInfo.nativesURL());
		mcNatives.isArchive = true;
		mcNatives.setVersion(new VersionData(0));
		fileList.add(mcJar);
		fileList.add(mcNatives);
		
	}
	
	public void resetLogin() {
		RESPONSE = null;
	}
}
