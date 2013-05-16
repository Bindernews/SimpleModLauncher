package com.github.modlauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	
	public static byte[] getMD5(File f) throws IOException {
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
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	private static final String HEX = "0123456789abcdef";
	public static String bytesToString(byte[] buf) {
		StringBuilder sb = new StringBuilder(buf.length * 2);
		for(int i=0; i<buf.length; i++) {
			byte b = buf[i];
			sb.append(HEX.charAt(b/16));
			sb.append(HEX.charAt(b%16));
		}
		return sb.toString();
	}
}
