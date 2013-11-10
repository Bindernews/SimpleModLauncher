package com.github.vortexellauncher.workers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.vortexellauncher.pack.FileStatus;
import com.github.vortexellauncher.pack.ModFile;
import com.github.vortexellauncher.pack.PackCache;
import com.github.vortexellauncher.util.Utils;

public class PackValidator extends CallWorker<List<FileStatus>> {
	
	private PackCache packData;
	private List<ModFile> fileList;
	
	public PackValidator(PackCache cache, List<ModFile> flist) {
		packData = cache;
		fileList = flist;
	}

	@Override
	public List<FileStatus> perform() throws Exception {
		List<FileStatus> statuses = new ArrayList<FileStatus>(fileList.size());
		for(ModFile mf : fileList) {
			statuses.add(validateFile(mf));
		}
		return statuses;
	}
	
	protected FileStatus validateFile(ModFile mf) {
		if (!packData.isModDefined(mf)) {
			if (mf.getFilename() != null) {
				File tfile = new File(mf.type.getDir(packData.getModpack().mcversion, packData.getModpack().getFolder()), mf.getFilename());
				if (tfile.exists()) return FileStatus.FoundButNotCached;
				else return FileStatus.NotFound;
			}
			return FileStatus.NotCached;
		}
		File mfile = new File(mf.type.getDir(packData.getModpack().mcversion, packData.getModpack().getFolder()), packData.getFilename(mf));
		if (!mfile.exists())
			return FileStatus.NotFoundButCached;
		try {
			if (!Utils.getMD5(mfile).equals(packData.getMD5(mf)))
				return FileStatus.HashError;
		} catch (IOException e) {
			return FileStatus.IOError;
		}
		if (!packData.getURL(mf).equals(mf.url))
			return FileStatus.Update;
		if (mf.getVersion().compareTo(packData.getVersion(mf)) != 0)
			return FileStatus.Update;
		return FileStatus.Valid;
	}
}
