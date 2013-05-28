package com.github.vortexellauncher.pack;

import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.exceptions.HashComparionException;
import com.github.vortexellauncher.exceptions.InvalidModpackException;
import com.github.vortexellauncher.util.JsonUtils;
import com.google.gson.JsonObject;

public class ModFile {

	public static enum Download {
		Direct,
		Browser,
	}
	
	public ModType type = null;
	public String name = null;
	public String url = null;
	public boolean isArchive = false;
	public boolean newerThanCache = false;
	
	private String md5 = null;
	private VersionData version = null;
	private String filename;
	
	private String cacheFilename = null;
	private String cacheMD5 = null;

	public ModFile() {
	}
	
	public ModFile(String mname, ModType mtype, String murl) {
		this(mname, mtype, murl, false);
	}
	
	public ModFile(String mname, ModType mtype, String murl, boolean archive) {
		name = mname;
		type = mtype;
		url = murl;
		isArchive = archive;
	}
	
	public void loadJson(JsonObject obj, String oname) throws InvalidModpackException {
		// required
		name = oname;
		url = JsonUtils.validateString(obj,"url",true);
		version = VersionData.create(JsonUtils.validateString(obj, "version", false));
		type = new JsonUtils.TL<ModType>(ModType.class).safeGetEnum(obj, "type");
		
		// optional
		md5 = JsonUtils.validateString(obj,"md5",false);
		filename = JsonUtils.validateString(obj,"filename",false);
	}
	
	public boolean compareMD5(String hash) {
		if (md5 == null) {
			return true;
		} else {
			return md5.equals(hash);
		}
	}
	
	public boolean updateMD5(String hash) {
		if (md5 == null) {
			md5 = hash;
			return true;
		}
		else {
			if (md5.equals(hash))
				return false;
			else
				throw new HashComparionException("MD5s were not equal");
		}
	}
	
	/**
	 * Tries to set the filename. If the filename is already set this does nothing.
	 * @param fname New filename
	 * @return true if the filename was changed false otherwise
	 */
	public boolean updateFilename(String fname) {
		if (filename == null) {
			filename = fname;
			return true;
		}
		return false;
	}
	
	public String getMD5() {
		return md5 != null ? md5 : cacheMD5;
	}

	public void setMD5(String md5) {
		this.md5 = md5;
	}

	public String getFilename() {
		return filename != null ? filename : cacheFilename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public VersionData getVersion() {
		return version;
	}

	public void setVersion(VersionData version) {
		this.version = version;
	}
	
	public void setCacheFilename(String fname) {
		this.cacheFilename = fname;
	}
	
	public void setCacheMD5(String hash) {
		this.cacheMD5 = hash;
	}
}
