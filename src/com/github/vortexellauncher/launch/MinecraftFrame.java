/*
 * This file is part of FTB Launcher.
 *
 * Copyright © 2012-2013, FTB Launcher Contributors <https://github.com/Slowpoke101/FTBLaunch/>
 * FTB Launcher is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.vortexellauncher.launch;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import net.minecraft.Launcher;

@SuppressWarnings("serial")
public class MinecraftFrame extends JFrame {
	private Launcher appletWrap = null;

	public MinecraftFrame(String title, String imagePath) {
		super(title);

		if (imagePath != null)
			setIconImage(Toolkit.getDefaultToolkit().createImage(imagePath));
		super.setVisible(true);
		setResizable(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Thread() {
					public void run() {
						try {
							Thread.sleep(30000L);
						} catch (InterruptedException localInterruptedException) { }
						System.out.println("FORCING EXIT!");
						System.exit(0);
					}
				}.start();
				if (appletWrap != null) {
					appletWrap.stop();
					appletWrap.destroy();
				}
				System.exit(0);
			}
		});
	}

	public void start(Applet mcApplet, String user, String session) {
		try {
			appletWrap = new Launcher(mcApplet, new URL("http://www.minecraft.net/game"));
		} catch (MalformedURLException ignored) { }
		appletWrap.setParameter("username", user);
		appletWrap.setParameter("sessionid", session);
		appletWrap.setParameter("stand-alone", "true");
		mcApplet.setStub(appletWrap);
		add(appletWrap);

		appletWrap.setPreferredSize(new Dimension(854, 480));

		pack();
		validate();
		new Thread() {
			public void run() {
				appletWrap.init();
				appletWrap.start();
				setVisible(true);
			}
		}.start();
	}
}
