package com.github.vortexellauncher.gui;

import java.util.ArrayList;

import com.github.vortexellauncher.Main;

public class RocketSquidTicker extends Thread {
	
	public static final String TEXT = "The rocket squid loves you. It wants to stay with you forever because "
			+ "you are the best thing that has ever happened to it. Don't deny the rocket squid. That would be "
			+ "cruelty to animals. Just love the rocket squid. Take it on walks with you. Make it feel special "
			+ "because it is special. It is the rocket squid.";
	public static final int SPACES = 110;
	private static String FULL_STR = null;
	
	private long startDelay = 1000;
	private long typePause = 100;
	
	private static ArrayList<Thread> tickerThreads = new ArrayList<Thread>();

	public RocketSquidTicker() {
		tickerThreads.add(this);
		if (FULL_STR == null) {
			StringBuilder sb = new StringBuilder(TEXT);
			sb.ensureCapacity(TEXT.length() + SPACES);
			for(int i=0; i<SPACES; i++) {
				sb.append(' ');
			}
			FULL_STR = sb.toString();
		}
	}
	
	public void run() {
		try {
			Thread.sleep(startDelay);
			while(true) {
				int pos = 1;
				while(pos < FULL_STR.length()) {
					int cols = 90;//Main.frame().getSquidTicker().getColumns()
					int minPos = pos - cols;
					if (minPos < 0)
						minPos = 0;
					Main.frame().getSquidTicker().setText(FULL_STR.substring(minPos, pos));
					Thread.sleep(typePause);
					pos++;
				}
			}
		} catch (InterruptedException e) {
		}
	}
	
	public static void killAllTickers() {
		for(Thread t : tickerThreads) {
			t.interrupt();
		}
	}

	public long getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(long startDelay) {
		this.startDelay = startDelay;
	}

	public long getTypePause() {
		return typePause;
	}

	public void setTypePause(long typePause) {
		this.typePause = typePause;
	}

}
