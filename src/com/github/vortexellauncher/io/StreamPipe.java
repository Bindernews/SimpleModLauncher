package com.github.vortexellauncher.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPipe extends Thread {

	private InputStream read;
	private OutputStream write;
	
	public StreamPipe(InputStream sread, OutputStream swrite) {
		read = sread;
		write = swrite;
	}
	
	@Override
	public void run() {
		try {
			byte[] buf = new byte[1024];
			int num = 1;
			while(num > 0) {
				num = read.read(buf);
				if (num <=0)
					break;
				write.write(buf, 0, num);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
