package com.github.vortexellauncher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {
	
	public static String getMD5(File f) throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(new FileInputStream(f), md);
			int bRead;
			byte[] buff = new byte[2048];
			while(true) {
				bRead = dis.read(buff);
				if (bRead != buff.length)
					break;
			}
			dis.close();
			return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	private static final String HEX = "0123456789abcdef";
	public static String bytesToHex(byte[] buf) {
		StringBuilder sb = new StringBuilder(buf.length * 2);
		for(int i=0; i<buf.length; i++) {
			int b = (buf[i] & 0xFF);
			sb.append(HEX.charAt(b/16));
			sb.append(HEX.charAt(b%16));
		}
		return sb.toString();
	}
	
	public static byte[] hexToBytes(String hex) {
		byte[] buf = new byte[hex.length() / 2];
		for(int i=0; i<hex.length(); i+=2) {
			int b = ((hex.charAt(i) - '0') << 4)
					| (hex.charAt(i+1) - '0');
			buf[i/2] = (byte)b;
		}
		return buf;
	}

	public static void simpleCryptOut(String in, File out) throws IOException {
		FileWriter w = new FileWriter(out);
		//fos.write(bytesToHex(in.getBytes()).getBytes());
		w.write(in);
		w.close();
	}
	
	public static String simpleCryptIn(File in) throws IOException {
		char[] buf = new char[2048];
		FileReader r = new FileReader(in);
		int br = r.read(buf);
		r.close();
		return new String(buf, 0, br);
	}
	
	public static List<File> extractJar(File inputFile) throws IOException {
		List<File> fileList = new ArrayList<File>();
		JarInputStream jis = new JarInputStream(new FileInputStream(inputFile));
		JarEntry entry;
		int readBytes = 0;
		byte[] buf = new byte[1024*5];
		
		while((entry = jis.getNextJarEntry()) != null) {
			if (entry.isDirectory())
				continue;
			File ofile = new File(inputFile.getParentFile(), entry.getName());
			fileList.add(ofile);
			ofile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(ofile);
			while((readBytes = jis.read(buf)) != -1) {
				fos.write(buf, 0, readBytes);
			}
			fos.close();
		}
		jis.close();
		return fileList;
	}
	
	public static List<File> extractZip(File inputFile) throws IOException {
		List<File> fileList = new ArrayList<File>();
		ZipInputStream fis = new ZipInputStream(new FileInputStream(inputFile));
		ZipEntry entry;
		int readBytes = 0;
		byte[] buf = new byte[1024*5];
		
		while((entry = fis.getNextEntry()) != null) {
			if (entry.isDirectory())
				continue;
			File ofile = new File(inputFile.getParentFile(), entry.getName());
			fileList.add(ofile);
			ofile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(ofile);
			while((readBytes = fis.read(buf)) != -1) {
				fos.write(buf, 0, readBytes);
			}
			fos.close();
		}
		fis.close();
		return fileList;
	}
	
	
}
