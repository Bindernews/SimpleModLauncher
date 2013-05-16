package com.github.modlauncher.pack;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import com.github.modlauncher.OSInfo;
import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.gui.ArrayListModel;
import com.github.modlauncher.gui.DownloadManagerGui;
import com.github.modlauncher.gui.ExceptionDialog;
import com.github.modlauncher.gui.MainFrame;
import com.github.modlauncher.net.FileDownloader;
import com.github.modlauncher.net.ProgressListener;

public class PackManager {

	// modpack fields
	private Modpack modpack;
	private PackCache packData;
	private File packDir,
		coreModsDir,
		modsDir,
		binDir,
		jarmodDir,
		configDir;
	
	// download fields
	private Proxy proxy;
	private DownloadManagerGui dmanagerGui;
	private ArrayListModel<FileDownloader> alm = new ArrayListModel<FileDownloader>(); 
	
	public PackManager(Modpack pack) throws IOException {
		modpack = pack;
		packDir = new File(OSInfo.dataDir(),modpack.folder);
		coreModsDir = new File(packDir, "coremods");
		modsDir = new File(packDir, "mods");
		binDir = new File(packDir, "bin");
		jarmodDir = new File(packDir, "jarmods");
		configDir = new File(packDir,"config");
		packData = new PackCache(new File(packDir, "cache.txt"));
	}
	
	/**
	 * Downloads all files, etc.
	 * @return
	 * @throws InvalidModpackException 
	 */
	public boolean setupPack() throws IOException, InvalidModpackException {
		dmanagerGui = new DownloadManagerGui();
		dmanagerGui.getList().setModel(alm);
		dmanagerGui.setVisible(true);
		
		if (!makeDirectoriesExist()) {
			throw new IOException("Error creating directories");
		}
		
		try {
			for(ModFile mod : modpack.getMods()) {
				new Downloader(mod).launch();
			}
		}
		catch (MalformedURLException mue) {
			throw new InvalidModpackException(mue);
		}
		catch (IOException ioe) {
			
		}
		return true;
	}
	
	public Proxy getProxy() {
		return proxy;
	}
	
	public void setProxy(Proxy aproxy) {
		proxy = aproxy;
	}
	
	public File getWorkingDir() {
		return packDir; 
	}
	
	public File getBinDir() {
		return binDir; 
	}
	
	private boolean makeDirectoriesExist() {
		return ensureDirectory(packDir)
				&& ensureDirectory(coreModsDir)
				&& ensureDirectory(modsDir)
				&& ensureDirectory(binDir)
				&& ensureDirectory(jarmodDir);
	}
	
	private boolean ensureDirectory(File dir) {
		dir.mkdir();
		return dir.isDirectory();
	}

	public class Downloader implements ProgressListener {
		
		protected ModFile mf;
		protected FileDownloader fd;
		
		public Downloader(ModFile mod) {
			mf = mod;
		}
		
		public void launch() throws MalformedURLException, IOException, InvalidModpackException {
			File dir;
			switch(mf.type) {
			case Jar:
				dir = jarmodDir;
				break;
			case Core:
				dir = coreModsDir;
				break;
			case Mod:
				dir = modsDir;
				break;
			case Config:
				dir = configDir;
				break;
			default:
				throw new InvalidModpackException("Invalid mod file type");
			}
			URLConnection urlcon;
			if (proxy != null)
				urlcon = new URL(mf.url).openConnection(proxy);
			else
				urlcon = new URL(mf.url).openConnection();
			fd = new FileDownloader(urlcon, dir, mf.filename, Arrays.asList((ProgressListener)this));
			alm.add(fd);
		}
		
		@Override
		public void onBegin(FileDownloader fd) {}
	
		@Override
		public void onUpdate(FileDownloader fd) {}
	
		@Override
		public void onComplete(FileDownloader fd) {
			for(ModFile mf : modpack.getMods()) {
				if (mf.url.equals(fd.getURL().toString())) {
					mf.filename = fd.getFile().getName();
					mf.md5 = fd.getMD5();
					packData.updateMod(mf);
					break;
				}
			}
		}
	
		@Override
		public void onError(FileDownloader fd) {
			ExceptionDialog exd = new ExceptionDialog(MainFrame.i(), "An Error Occured", fd.getError());
			exd.setVisible(true);
		}
	}
}
