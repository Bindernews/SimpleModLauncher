package com.github.modlauncher.workers;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingWorker;

import com.github.modlauncher.Launchpad;
import com.github.modlauncher.MinecraftLauncher;
import com.github.modlauncher.OS;
import com.github.modlauncher.VersionData;
import com.github.modlauncher.exceptions.InvalidModpackException;
import com.github.modlauncher.gui.ArrayListModel;
import com.github.modlauncher.gui.DownloadManagerGui;
import com.github.modlauncher.net.FileDownloader;
import com.github.modlauncher.pack.ModFile;
import com.github.modlauncher.pack.ModType;
import com.github.modlauncher.pack.Modpack;
import com.github.modlauncher.pack.PackCache;
import com.github.modlauncher.util.ErrorUtils;
import com.github.modlauncher.util.Utils;

public class GameUpdateWorker extends SwingWorker<Boolean, Float> {

	protected static class DownloadData {
		public final Future<FileDownloader> future;
		public final ModFile modfile;
		public DownloadData(Future<FileDownloader> fd, ModFile mf) {
			future = fd;
			modfile = mf;
		}
	}
	
	// modpack fields
	private Modpack modpack;
	private PackCache packData;
	private File packDir;
	private ExecutorService downloadService;
	private DownloadManagerGui dmanagerGui;
	private ArrayListModel<FileDownloader> alm = new ArrayListModel<FileDownloader>();
	private ArrayList<DownloadData> futureList = new ArrayList<DownloadData>();
	
	public GameUpdateWorker(PackCache cache) {
		modpack = cache.getModpack();
		packDir = modpack.getFolder();
		downloadService = Executors.newFixedThreadPool(10);
		packData = cache;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return setupPack();
	}
	
	public boolean setupPack() throws IOException, InvalidModpackException {
		dmanagerGui = new DownloadManagerGui(Launchpad.i().getFrame());
		dmanagerGui.getList().setModel(alm);
		
		if (!makeDirectoriesExist()) {
			throw new IOException("Error creating directories");
		}
		
		try {
			List<ModFile> fileList = new ArrayList<ModFile>(modpack.getMods());
			addMCFiles(fileList);
			Proxy prox = Launchpad.getSettings().getProxy(); 
			for(ModFile mod : fileList) {
				File mfile;
				try {
					boolean skip = false;
					if (packData.isModDefined(mod)) {
						mfile = new File(mod.type.getDir(packDir), packData.getFilename(mod));
						if (packData.getURL(mod).equals(mod.url)
								&& Utils.getMD5(mfile).equals(packData.getMD5(mod))) {
							skip = true;
						}
						// else will re-download
					}
					if (skip)
						continue;
				}
				catch (IOException e) {}
				// if any problems occur it will ignore them and just try to redownload the file
				
				URLConnection urlcon;
				if (prox != null) urlcon = new URL(mod.url).openConnection(prox);
				else urlcon = new URL(mod.url).openConnection();
				FileDownloader fd = new FileDownloader(urlcon, mod.type.getDir(packDir), mod.getFilename());
				Future<FileDownloader> fut = downloadService.submit(fd);
				futureList.add(new DownloadData(fut, mod));
				alm.add(fd);
			}
			if (alm.size() > 0) {
				dmanagerGui.setVisible(true);
			}
			boolean allDone = false;
			while(!allDone) {
				allDone = true;
				for(DownloadData data : futureList) {
					allDone = allDone ? data.future.isDone() : false;
				}
				publish(0.0f);
			}
			dmanagerGui.dispose();
			Launchpad.i().getFrame().setEnabled(false);
			for(DownloadData data : futureList) {
				try {
					FileDownloader fd = data.future.get();
					data.modfile.updateFilename(fd.getFile().getName());
					data.modfile.updateMD5(fd.getMD5());
					packData.updateMod(data.modfile);
					if (data.modfile.type == ModType.Native) {
						for(File ff : Utils.extractZip(fd.getFile())) {
							ModFile mf = new ModFile(ff.getName(), ModType.Native, null);
							mf.setMD5(Utils.getMD5(ff));
							packData.updateMod(mf);
						}
					}
					if (data.modfile.name.equals("minecraft.jar")) {
						MinecraftLauncher.killMetaInf(fd.getFile());
						data.modfile.setMD5(Utils.getMD5(fd.getFile()));
						packData.updateMod(data.modfile);
					}
				} catch (ExecutionException e) {
					ErrorUtils.showException(e, false);
				}
			}
			packData.saveToFile();
			return true;
		}
		catch (IOException ioe) {
			ErrorUtils.showException(ioe, false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected void process(List<Float> data) {
		for(int i=0; i<alm.size(); i++) {
			alm.set(i, alm.get(i));
		}
	}
	
	protected void addMCFiles(List<ModFile> fileList) {
		String baseurl = "https://s3.amazonaws.com/MinecraftDownload/";
		String[] binFileNames = {"jinput.jar", "lwjgl.jar", "lwjgl_util.jar"};
		for(String bfn : binFileNames) {
			ModFile mf = new ModFile(bfn, ModType.Bin, baseurl + bfn);
			mf.setVersion(new VersionData(0));
			fileList.add(mf);
		}
		ModFile mcJar = new ModFile("minecraft.jar", ModType.Bin, modpack.mcversion.getJarUrl());
		mcJar.setVersion(new VersionData(0));
		ModFile mcNatives = new ModFile(OS.nativesJar(), ModType.Native, baseurl + OS.nativesJar());
		mcNatives.setVersion(new VersionData(0));
		fileList.add(mcJar);
		fileList.add(mcNatives);
	}
	
	public PackCache getCache() {
		return packData;
	}
	
	public File getWorkingDir() {
		return packDir; 
	}
	
	private boolean makeDirectoriesExist() {
		return ensureDirectory(packDir)
				&& ensureDirectory(new File(packDir, "coremods"))
				&& ensureDirectory(new File(packDir, "mods"))
				&& ensureDirectory(new File(packDir, "bin"))
				&& ensureDirectory(new File(packDir, "bin/natives"))
				&& ensureDirectory(new File(packDir, "config"))
				&& ensureDirectory(new File(packDir, "jarmods"));
	}
	
	private boolean ensureDirectory(File dir) {
		dir.mkdirs();
		return dir.isDirectory();
	}
}
