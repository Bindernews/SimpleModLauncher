package com.github.modlauncher.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.modlauncher.Utils;


public class FileDownloader implements Runnable {
	
	public static final long BYTES_KB = 1024;
	
	private final URLConnection urlcon;
	private final long updateBytes = BYTES_KB * 2;
	private final Thread downloadThread = new Thread(this);

	private volatile Exception mError = null;
	private volatile float percentDone = 0.0f;
	private volatile String md5Sum = null;
	
	private List<ProgressListener> progressWatchers;
	private File file = null;
	private File directory = null;
	private String fileName;
	private boolean canLaunch = true;
	
	public FileDownloader(URLConnection turl, File dir, String fname, Collection<ProgressListener> plist) {
		urlcon = turl;
		fileName = fname;
		directory = dir;
		progressWatchers = new ArrayList<ProgressListener>(plist);
	}
	
	public URL getURL() {
		return urlcon.getURL();
	}
	
	public File getFile() {
		return file;
	}
	
	public File getDirectory() {
		return directory;
	}
	
	public float getPercentDone() {
		return percentDone;
	}
	
	public Exception getError() {
		return mError;
	}
	
	public String getMD5() {
		return md5Sum;
	}
	
	public void beginDownload() {
		downloadThread.start();
	}
	
	public boolean waitForComplete() {
		if (downloadThread.getState() == State.NEW)
			return false;
		if (downloadThread.getState() == State.TERMINATED)
			return true;
		try {
			downloadThread.join();
		} catch (InterruptedException e) {
		}
		return (downloadThread.getState() == State.TERMINATED);
	}
	
	public void cancel() {
		canLaunch = false;
		downloadThread.interrupt();
	}

	@Override
	public void run() {
		// ensure it hasn't been canceled before it began
		if (!canLaunch) return;
		canLaunch = false;
		
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		DigestInputStream digis = null;
		try {
			for(ProgressListener pl : progressWatchers) {
				pl.onBegin(this);
			}
			if (urlcon instanceof HttpURLConnection) {
				HttpURLConnection htcon = (HttpURLConnection)urlcon;
				htcon.setInstanceFollowRedirects(true);
			}
			urlcon.setDoInput(true);
			urlcon.setAllowUserInteraction(true);
			urlcon.connect();
			
			if (fileName == null) {
				fileName = getURL().getPath(); // set it as a fallback
				if (urlcon instanceof HttpURLConnection) {
					HttpURLConnection htcon = (HttpURLConnection)urlcon;
					String dispos = htcon.getHeaderField("content-disposition");
					if (dispos != null) {
						int i1 = dispos.indexOf("filename=\"");
						int i2 = dispos.indexOf('\"', i1 + 10);
						if (i1 != -1 && i2 != -1) {
							fileName = dispos.substring(i1, i2);
						}
					}
				}
			}
			file = new File(directory, fileName);
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			digis = new DigestInputStream(urlcon.getInputStream(), md);
			rbc = Channels.newChannel(digis);
			fos = new FileOutputStream(file);
			
			long bytesRead = 0;
			long writePos = 0;
			long dataLength = urlcon.getContentLengthLong();
			do {
				bytesRead = fos.getChannel().transferFrom(rbc, writePos, updateBytes);
				writePos += bytesRead;
				percentDone = (float)writePos / dataLength;
				for(ProgressListener pl : progressWatchers) {
					pl.onUpdate(this);
				}
			}
			while (bytesRead == updateBytes);
			fos.close();
			rbc.close();
			md5Sum = Utils.bytesToString(md.digest());
		}
		catch (IOException ioe) {
			mError = ioe;
			for(ProgressListener pl : progressWatchers) {
				pl.onError(this);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		for(ProgressListener pl : progressWatchers) {
			pl.onComplete(this);
		}
	}
}
