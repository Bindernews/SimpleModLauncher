package com.github.vortexellauncher.workers;

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

import com.github.vortexellauncher.Launch;
import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.gui.ArrayListModel;
import com.github.vortexellauncher.gui.DownloadManagerGui;
import com.github.vortexellauncher.launch.MinecraftLauncher;
import com.github.vortexellauncher.net.FileDownloader;
import com.github.vortexellauncher.net.NetResolver;
import com.github.vortexellauncher.pack.FileStatus;
import com.github.vortexellauncher.pack.ModFile;
import com.github.vortexellauncher.pack.PackCache;
import com.github.vortexellauncher.util.ErrorUtils;
import com.github.vortexellauncher.util.Utils;

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
	private PackCache packData;
	private File packDir;
	private List<ModFile> fileList;
	private List<FileStatus> statusList;
	private ExecutorService downloadService;
	private DownloadManagerGui dmanagerGui;
	private ArrayListModel<FileDownloader> alm = new ArrayListModel<FileDownloader>();
	private ArrayList<DownloadData> futureList = new ArrayList<DownloadData>();
	
	public GameUpdateWorker(PackCache cache, List<ModFile> flist, List<FileStatus> statList) {
		packData = cache;
		packDir = packData.getModpack().getFolder();
		downloadService = Executors.newFixedThreadPool(16);
		fileList = flist;
		statusList = statList;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		return setupPack();
	}
	
	public boolean setupPack() throws IOException, InvalidModpackException {
		dmanagerGui = new DownloadManagerGui(Launch.frame());
		dmanagerGui.getList().setModel(alm);
		dmanagerGui.setVisible(true);
		
		if (!makeDirectoriesExist()) {
			throw new IOException("Error creating directories");
		}
		
		try {
			Proxy prox = Launch.getSettings().getProxy(); 
			for(int i=0; i<fileList.size(); i++) {
				if (statusList.get(i).download == 0)
					continue;
				ModFile mod = fileList.get(i);
				URLConnection urlcon = NetResolver.resolveConnection(new URL(mod.url), prox);
				FileDownloader fd = new FileDownloader(urlcon, mod.type.getDir(packDir), mod.getFilename());
				Future<FileDownloader> fut = downloadService.submit(fd);
				futureList.add(new DownloadData(fut, mod));
				alm.add(fd);
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
			Launch.frame().setEnabled(false);
			for(DownloadData data : futureList) {
				try {
					FileDownloader fd = data.future.get();
					data.modfile.updateFilename(fd.getFile().getName());
					data.modfile.updateMD5(fd.getMD5());
					packData.updateMod(data.modfile);
					if (data.modfile.isArchive) {
						String archiveURLStr = fd.getFile().toURI().toURL().toExternalForm();
						for(File ff : Utils.extractZip(fd.getFile())) {
							ModFile mf = new ModFile(ff.getName(), data.modfile.type, archiveURLStr);
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
