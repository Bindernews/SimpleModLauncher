package com.github.vortexellauncher;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
	
	private static Logger logger = Logger.getLogger(Main.LOGGER_NAME);

	public static Logger getLogger() {
		return logger;
	}
	
	public static Logger i() {
		return logger;
	}

	public static void config(String msg) {
		logger.config(msg);
	}

	public static void entering(String sourceClass, String sourceMethod, Object param1) {
		logger.entering(sourceClass, sourceMethod, param1);
	}

	public static void entering(String sourceClass, String sourceMethod,
			Object[] params) {
		logger.entering(sourceClass, sourceMethod, params);
	}

	public static void entering(String sourceClass, String sourceMethod) {
		logger.entering(sourceClass, sourceMethod);
	}

	public static void exiting(String sourceClass, String sourceMethod, Object result) {
		logger.exiting(sourceClass, sourceMethod, result);
	}

	public static void exiting(String sourceClass, String sourceMethod) {
		logger.exiting(sourceClass, sourceMethod);
	}

	public static void fine(String msg) {
		logger.fine(msg);
	}

	public static void finer(String msg) {
		logger.finer(msg);
	}

	public static void finest(String msg) {
		logger.finest(msg);
	}

	public static Level getLevel() {
		return logger.getLevel();
	}

	public static String getName() {
		return logger.getName();
	}

	public static void info(String msg) {
		logger.info(msg);
	}

	public static boolean isLoggable(Level level) {
		return logger.isLoggable(level);
	}

	public static void log(Level level, String msg, Object param1) {
		logger.log(level, msg, param1);
	}

	public static void log(Level level, String msg, Object[] params) {
		logger.log(level, msg, params);
	}

	public static void log(Level level, String msg, Throwable thrown) {
		logger.log(level, msg, thrown);
	}

	public static void log(Level level, String msg) {
		logger.log(level, msg);
	}

	public static void log(LogRecord record) {
		logger.log(record);
	}

	public static void logp(Level level, String sourceClass, String sourceMethod,
			String msg, Object param1) {
		logger.logp(level, sourceClass, sourceMethod, msg, param1);
	}

	public static void logp(Level level, String sourceClass, String sourceMethod,
			String msg, Object[] params) {
		logger.logp(level, sourceClass, sourceMethod, msg, params);
	}

	public static void logp(Level level, String sourceClass, String sourceMethod,
			String msg, Throwable thrown) {
		logger.logp(level, sourceClass, sourceMethod, msg, thrown);
	}

	public static void logp(Level level, String sourceClass, String sourceMethod,
			String msg) {
		logger.logp(level, sourceClass, sourceMethod, msg);
	}

	public static void logrb(Level level, String sourceClass, String sourceMethod,
			String bundleName, String msg, Object param1) {
		logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
	}

	public static void logrb(Level level, String sourceClass, String sourceMethod,
			String bundleName, String msg, Object[] params) {
		logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
	}

	public static void logrb(Level level, String sourceClass, String sourceMethod,
			String bundleName, String msg, Throwable thrown) {
		logger.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
	}

	public static void logrb(Level level, String sourceClass, String sourceMethod,
			String bundleName, String msg) {
		logger.logrb(level, sourceClass, sourceMethod, bundleName, msg);
	}

	public static void setLevel(Level newLevel) throws SecurityException {
		logger.setLevel(newLevel);
	}

	public static void severe(String msg) {
		logger.severe(msg);
	}

	public static void throwing(String sourceClass, String sourceMethod,
			Throwable thrown) {
		logger.throwing(sourceClass, sourceMethod, thrown);
	}

	public static void warning(String msg) {
		logger.warning(msg);
	}

}
