package com.github.vortexellauncher.io;

import java.io.IOException;
import java.io.OutputStream;

public class MultiOutputStream extends OutputStream {

	private OutputStream[] subStreams;
	
	public MultiOutputStream(OutputStream... outs) {
		subStreams = outs;
	}
	
	@Override
	public void write(int b) throws IOException {
		for(OutputStream o : subStreams) {
			o.write(b);
		}
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		for(OutputStream o : subStreams) {
			o.write(b);
		}
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		for(OutputStream o : subStreams) {
			o.write(b, off, len);
		}
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		for(OutputStream o : subStreams) {
			o.close();
		}
	}

}
