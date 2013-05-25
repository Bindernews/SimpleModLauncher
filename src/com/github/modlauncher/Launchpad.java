package com.github.modlauncher;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.gui.MainFrame;
import com.github.modlauncher.gui.OfflineDialog;
import com.github.modlauncher.gui.Res;
import com.github.modlauncher.gui.StatusIcon;
import com.github.modlauncher.pack.Modpack;
import com.github.modlauncher.pack.PackCache;
import com.github.modlauncher.util.ErrorUtils;
import com.github.modlauncher.util.Utils;
import com.github.modlauncher.workers.GameUpdateWorker;
import com.github.modlauncher.workers.LoginWorker;
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
	private GameUpdateWorker updateWorker = null;
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
				runUpdater();
			}
		};
		worker.execute();
	}
	
	public void createOfflineLogin() {
		String user = frame.getLoginPanel().getUsername();
		RESPONSE = new LoginResponse("0: :" + user + ": "); 
	}
	
	public void runUpdater() {
		PackCache cache;
		try {
			cache = new PackCache(settings.getModpack());
			updateWorker = new GameUpdateWorker(cache) {
				protected void done() {
					workThread.execute(new Runnable() {
						public void run() {
							launchGame();
						}
					});
				}
			};
			updateWorker.execute();
		}
		catch (IOException e) {
			ErrorUtils.showException(e, true);
		}
	}

	
	protected void launchGame() {
		try {
			if (!updateWorker.get()) {
			}
			else { 
				MinecraftLauncher.launchMinecraft(settings.getModpack(), RESPONSE.username, RESPONSE.sessionID);
				frame.dispose();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			if (e.getCause() instanceof IOException) {
				frame.getLoginPanel().setStatus("Failed to download file(s)");
				frame.getLoginPanel().setStatusIcon(StatusIcon.Error);
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			ErrorUtils.showException(e, true);
		}
		
		System.exit(0);
	}
	
	public void resetLogin() {
		RESPONSE = null;
	}
	
	public MainFrame getFrame() {
		return frame;
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
	
	public static void main(String[] args) throws InvalidModpackException {
		try {
			Res.init();
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}
		catch (IOException ioe) {
			//TODO crash message
		}
		catch (UnsupportedLookAndFeelException e) {
			// it really doesn't matter if setting the L&F fails 
		}
		
		// create the launchpad and start the program
		try {
			instance = new Launchpad();
			//JsonElement elem = Utils.readJsonFile(Res.getURL("res/vortexel_pack.json"));
			//Modpack pack = new Modpack(elem.getAsJsonObject(), "vortexel_pack.json");
			JsonElement elem = Utils.readJsonFile(Res.getURL("res/modpack.json"));
			Modpack pack = new Modpack(elem.getAsJsonObject(), "modpack.json");
			instance.settings.setModpack(pack);
		} catch (IOException e) {
			ErrorUtils.showException(e, true);
		}
	}
}
