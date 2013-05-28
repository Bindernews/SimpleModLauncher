package com.github.vortexellauncher.pack;

import java.util.EnumSet;

public enum FileStatus {
	/** All is well */
	Valid(true,true,true,0),
	Update(true,true,true,1), // there's an update to download
	FoundButNotCached(true,false,true,2), // a file with the same name was found but it's not in the cache. download=2 means confirm download
	HashError(true,false,false,1), // file's hash and cached hash weren't the same
	NotFound(false,false,false,1), // not in cache but it specifies a filename so we checked but the file doesn't exist. it could be new...
	NotCached(false,false,false,1), // file wasn't in the cache and we don't have the filename
	IOError(false,false,false,1),
	NotFoundButCached(false,false,false,1), // it's in the cache but not found
	;
	
	public static final EnumSet<FileStatus> StatusExists = EnumSet.of(Valid, Update, FoundButNotCached, HashError),
			StatusIsValid = EnumSet.of(Valid, Update),
			StatusCouldPlay = EnumSet.of(Valid, Update, FoundButNotCached),
			StatusDoDownload = EnumSet.complementOf(EnumSet.of(Valid));
	
	public final boolean exists, isVaild, couldPlay;
	public final int download;
	private FileStatus(boolean exists, boolean isValid, boolean playable, int download) {
		this.exists = exists;
		this.isVaild = isValid;
		couldPlay = playable;
		this.download = download;
	}
}
