package com.github.vortexellauncher.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

import com.github.vortexellauncher.util.Utils;

public class FileDownloader implements Callable<FileDownloader> {
	
	private static final long TRANSFER_BYTES = 1024 * 5;
	
	private final URLConnection urlcon;
	private volatile String md5Sum = null;
	private volatile float progress = 0.0f;
	
	private File file = null;
	private File directory = null;
	private String fileName;
	
	public FileDownloader(URLConnection turl, File dir, String fname) {
		urlcon = turl;
		fileName = fname;
		directory = dir;
	}
	
	@Override
	public FileDownloader call() throws Exception {	
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		DigestInputStream digis = null;
		
		setupHttpConnection(urlcon);
		urlcon.connect();
		URLConnection ncon = followRedirect(urlcon);
		if (ncon != urlcon) {
			setupHttpConnection(ncon);
			ncon.connect();
		}
		
		if (fileName == null) {
			fileName = getURL().getPath(); // set it as a fallback
			if (ncon instanceof HttpURLConnection) {
				HttpURLConnection htcon = (HttpURLConnection)ncon;
				String dispos = htcon.getHeaderField("content-disposition");
				if (dispos != null) {
					final String searchStr = "filename=\"";
					int i1 = dispos.indexOf(searchStr);
					int i2 = dispos.indexOf('\"', i1 + searchStr.length());
					if (i1 != -1 && i2 != -1) {
						fileName = dispos.substring(i1 + searchStr.length(), i2);
					}
				}
				else {
					fileName = URLDecoder.decode(new File(fileName).getName(), "UTF-8");
				}
			}
			
		}
		file = new File(directory, fileName);
		file.getParentFile().mkdirs();
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		digis = new DigestInputStream(ncon.getInputStream(), md);
		rbc = Channels.newChannel(digis);
		fos = new FileOutputStream(file);
		
		long bytesRead = 0;
		long writePos = 0;
		long dataLength = ncon.getContentLengthLong();
		do {
			bytesRead = fos.getChannel().transferFrom(rbc, writePos, TRANSFER_BYTES);
			writePos += bytesRead;
			progress = (float)writePos / dataLength;
		}
		while (bytesRead == TRANSFER_BYTES);
		fos.close();
		rbc.close();
		md5Sum = Utils.bytesToHex(md.digest());
		return this;
	}
	
	protected URLConnection followRedirect(URLConnection urlcon) throws IOException {
		if (urlcon instanceof HttpURLConnection) {
			HttpURLConnection htcon = (HttpURLConnection)urlcon;
			if (htcon.getResponseCode() == 307) {
				return new URL(htcon.getHeaderField("Location")).openConnection();
			}
		}
		return urlcon;
	}
	
	protected void setupHttpConnection(URLConnection urlcon) {
		if (urlcon instanceof HttpURLConnection) {
			HttpURLConnection htcon = (HttpURLConnection)urlcon;
			htcon.setInstanceFollowRedirects(true);
			htcon.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		}
		urlcon.setDoInput(true);
		urlcon.setAllowUserInteraction(true);
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
	
	public String getMD5() {
		return md5Sum;
	}
	
	public synchronized float getProgress() {
		return progress;
	}
	
	protected synchronized void setProgress(float prog) {
		progress = prog;
	}
}
