package com.github.vortexellauncher.workers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.github.vortexellauncher.Main;
import com.github.vortexellauncher.VersionData;
import com.github.vortexellauncher.util.NetUtils;
import com.github.vortexellauncher.util.OSUtils;

public class UpdateCheckGuiWorker extends SwingWorker<String, Float> {

	@Override
	protected String doInBackground() throws Exception {
		String str = NetUtils.downloadString(new URL(Main.UPDATE_URL).openConnection());
		if (str == null || str.equals(""))
			return "false|Null string";
		String[] split = str.split("|");
		VersionData nvd = VersionData.safeCreate(split[0]);
		if (nvd == null)
			return "false|Bad version";
		if (nvd.compareTo(Main.VERSION) > 0) {
			return "true|" + nvd + "|" + split[1];
		} else {
			return "false|Up to date";
		}
	}
	
	@Override
	public void done() {
		try {
			String[] split = get().split("|");
			boolean update = Boolean.parseBoolean(split[0]);
			if (update) {
				String msg = "An update is available. The new version is " + split[1] + ".\nWould you like to update?"; 
				int rval = JOptionPane.showConfirmDialog(Main.frame(), msg, "Update Available", JOptionPane.YES_NO_OPTION);
				if (rval == JOptionPane.YES_OPTION) {
					OSUtils.openBrowser(new URL(split[2]));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			//ErrorUtils.printException(e, false);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
