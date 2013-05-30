package com.github.vortexellauncher.launch;

import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import com.github.vortexellauncher.Launch;
import com.github.vortexellauncher.OS;
import com.github.vortexellauncher.Settings;
import com.github.vortexellauncher.pack.ModFile;
import com.github.vortexellauncher.pack.ModType;
import com.github.vortexellauncher.pack.Modpack;

public class MinecraftLauncher {
	
	public static Process launchMinecraft(Modpack pack, String username, String sessid) throws IOException {
		
		File basepath = pack.getFolder();
		File workingDir = new File(basepath, "bin/");
		
		ArrayList<String> cpModList = new ArrayList<String>();
		for(int i=0; i<pack.getMods().size(); i++) {
			ModFile mf = pack.getMods().get(i); 
			if (mf.type == ModType.Jar) {
				cpModList.add(new File(mf.type.getDir(basepath), mf.getFilename()).getAbsolutePath());
			}
		}
		for(File f : workingDir.listFiles()) {
			if (f.getName().endsWith(".jar") && f.isFile()) {
				cpModList.add(f.getAbsolutePath());
			}
			if (f.getName().equals("minecraft.jar")) {
				killMetaInf(f);
			}
		}
		
		StringBuilder cpBuilder = new StringBuilder();
		for(int i=0; i<cpModList.size(); i++) {
			cpBuilder.append(cpModList.get(i));
			cpBuilder.append(File.pathSeparatorChar);
		}
		
		String jvmPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		if (OS.getOS() == OS.Windows) {
			jvmPath += "w";
		}
		
		ArrayList<String> procArgs = new ArrayList<String>();
		procArgs.add(jvmPath);
		procArgs.addAll(Launch.getSettings().getVMParams());
		procArgs.add("-Xmx" + Launch.getSettings().getRamMax() + "M");
		procArgs.add("-XX:+UseConcMarkSweepGC");
		procArgs.add("-XX:+CMSIncrementalMode");
		procArgs.add("-XX:+AggressiveOpts");
		procArgs.add("-XX:+CMSClassUnloadingEnabled");
		procArgs.add("-XX:MaxPermSize=128m");
		procArgs.add("-cp");
		procArgs.add(System.getProperty("java.class.path") + File.pathSeparator + cpBuilder.toString());
		procArgs.add(MinecraftLauncher.class.getName());
		procArgs.add(basepath.getAbsolutePath());
		procArgs.add(username);
		procArgs.add(sessid);
		procArgs.add(cpBuilder.toString());
		ProcessBuilder procBuilder = new ProcessBuilder(procArgs);
		procBuilder.environment().put("APPDATA", basepath.getParent());
		if (Settings.isDebugMode()) {
			procBuilder.redirectError(new File("debug.log")).redirectOutput(new File("debug.log"));
		} else {
			procBuilder.inheritIO();
		}
		return procBuilder.start();
	}
	
	public static void main(String[] args) {
		try {
			String basepathStr = args[0],
					username = args[1],
					sessid = args[2];
			File basepath = new File(basepathStr);
			File workDir = new File(basepath, "bin/");
			String[] classpath = args[3].split(File.pathSeparator);
			
			
			URL[] classUrls = new URL[classpath.length];
			for(int i=0; i<classpath.length; i++) {
				classUrls[i] = new URL("file://" + classpath[i]);
			}
			
			// load natives
			String nativesDir = new File(workDir, "natives/").getAbsolutePath();
			System.setProperty("org.lwjgl.librarypath", nativesDir);
			System.setProperty("net.java.games.input.librarypath", nativesDir);
			System.setProperty("user.home", basepath.getAbsolutePath());
			
			URLClassLoader cl = new URLClassLoader(classUrls, MinecraftLauncher.class.getClassLoader());
			System.out.println(Arrays.toString(cl.getURLs()));
			
			Class<?> mcClass = cl.loadClass("net.minecraft.client.Minecraft");
			for(Field ff : mcClass.getFields()) {
				if (ff.getType() != File.class) {
					continue;
				}
				if (Modifier.isPrivate(ff.getModifiers()) && Modifier.isStatic(ff.getModifiers())) {
					ff.setAccessible(true);
					ff.set(null, workDir);
					break;
				}
			}
			String mcDir = mcClass.getMethod("a", String.class).invoke(null, (Object) "minecraft").toString();
			System.out.println(mcDir);
			
			try {
				Class<?> MCAppletClass = cl.loadClass("net.minecraft.client.MinecraftApplet");
				Applet mcapplet = (Applet)MCAppletClass.newInstance();
				MinecraftFrame mcFrame = new MinecraftFrame("Minecraft", null);
				mcFrame.start(mcapplet, username, sessid);
			}
			catch(Exception e) {
				// go to backup
				mcClass.getMethod("main", String[].class).invoke(null, (Object)new String[]{username, sessid});
			}
			cl.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static String getCurrentJar() {
		String val = new File(Launch.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getAbsolutePath();
		System.out.println(val);
		return val;
	}
	
	public static void killMetaInf(File inputFile) throws IOException {
		boolean hasMetaInf = false;
		JarFile jf = new JarFile(inputFile);
		Enumeration<JarEntry> entriesEnum = jf.entries();
		while(entriesEnum.hasMoreElements()) {
			if (entriesEnum.nextElement().getName().contains("META-INF")) {
				hasMetaInf = true;
				break;
			}
		}
		jf.close();
		if (!hasMetaInf)
			return;
		File outputFile = new File(inputFile.getName()+".tmp");
		JarInputStream jis = new JarInputStream(new FileInputStream(inputFile));
		JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputFile));
		JarEntry entry;
		int readBytes = 0;
		byte[] buf = new byte[1024*5];
		
		while((entry = jis.getNextJarEntry()) != null) {
			if (entry.getName().contains("META-INF"))
				continue;
			jos.putNextEntry(entry);
			while((readBytes = jis.read(buf)) != -1) {
				jos.write(buf, 0, readBytes);
			}
			jos.closeEntry();
		}
		jis.close();
		jos.close();
		inputFile.delete();
		outputFile.renameTo(inputFile);
	}
}
