package com.github.vortexellauncher.gui;

import java.util.ArrayList;

import com.github.vortexellauncher.Main;

public class RocketSquidTicker extends Thread {
	
	public static final String TEXT = "The rocket squid loves you."
			+ "It wants to stay with you forever because you are the best\nthing that has ever happened to it."
			+ "Don't deny the rocket squid.That would be cruelty to animals."
			+ "Just love the rocket squid.Take it on walks with you."
			+ "Make it feel special because it is special.It is the rocket squid."
			+ "There is much you can learn from the rocket squid."
			+ "It has degrees in history, philosophy, mathematics,\nand computer science.";
	public static final int SPACES = 110;
	private static String FULL_STR = TEXT;
	
	private long startDelay = 1000;
	private long typePause = 3500;
	
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
				int pos = 0, lastPos = 0;
				while(pos < FULL_STR.length()) {
					int i1 = FULL_STR.indexOf('.', lastPos+1)+1,
							i2 = FULL_STR.indexOf('\n', lastPos+1);
					if (i1 != -1 && i1 < i2 || i2 == -1) pos = i1;
					if (i2 != -1 && i2 < i1 || i1 == -1) pos = i2;
					if (i1 == -1 && i2 == -1) {
						lastPos = 0;
						pos = 0;
					}
					Main.frame().getSquidTicker().setText(FULL_STR.substring(lastPos, pos));
					Thread.sleep(typePause);
					lastPos = pos;
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
