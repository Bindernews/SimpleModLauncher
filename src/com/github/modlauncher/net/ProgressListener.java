package com.github.modlauncher.net;



public interface ProgressListener {

	void onBegin(FileDownloader fd);
	void onUpdate(FileDownloader fd);
	void onComplete(FileDownloader fd);
	void onError(FileDownloader fd);
}
