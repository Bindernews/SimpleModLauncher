package com.github.vortexellauncher;

import java.util.Arrays;

/**
 * This class is used to compare versions. It parses version strings of integers separated by periods (e.g. "1.0.1").
 * It will ignore any trailing 0s (e.g. "1.5.0" would be considered "1.5"). 
 * 
 * @author Binder News
 */
public class VersionData implements Comparable<VersionData> {

	private final int[] vers;
	
	public VersionData(int... nums) {
		if (nums.length == 0) {
			vers = new int[] {0};
		} else {
			vers = nums;
		}
	}

	/**
	 * @param other VersionData to compare to
	 * @return a positive value if this version is larger than other, negative if this is less than other, or 0 if the versions are the same.
	 */
	@Override
	public int compareTo(VersionData other) {
		int compareLen = Math.min(vers.length, other.vers.length);
		for(int i=0; i<compareLen; i++) {
			int val = vers[i] - other.vers[i];
			if (val != 0)
				return val;
		}
		return vers.length - other.vers.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<vers.length; i++) {
			if (i != 0)
				sb.append('.');
			sb.append(vers[i]);
		}
		return sb.toString();
	}
	
	public static VersionData create(String text) {
		if (text == null || text.length() == 0) {
			return new VersionData(0);
		}
		// split into version parts
		String[] splits = text.split("\\.");
		int[] buf = new int[splits.length]; 
		for(int i=0; i<splits.length; i++) {
			buf[i] = Integer.parseInt(splits[i], 10);
		}
		
		// cut off trailing zeros
		int cutoff;
		for(cutoff = buf.length; cutoff > 0; cutoff--) {
			if (buf[cutoff - 1] != 0)
				break;
		}
		if (cutoff != buf.length) {
			buf = Arrays.copyOf(buf, cutoff);
		}
		return new VersionData(buf);
	}
}
