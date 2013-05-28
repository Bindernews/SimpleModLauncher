package com.github.vortexellauncher.workers;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class CallWorker<T> implements Callable<T> {
 
	private ExecutionException wrappedException = null;
	private Exception resultException = null;
	private T resultValue = null;
	private volatile boolean hasStarted = false;
	private AtomicBoolean hasFinished = new AtomicBoolean(false);
	
	public CallWorker() {
	}

	public abstract T perform() throws Exception;
	
	public void done() {}

	@Override
	public T call() throws Exception {
		runPerform();
		if (resultException != null) {
			throw resultException;
		} else {
			return resultValue;
		}
	}
	
	/**
	 * Runs the perform() method and returns the result or throws an ExecutionException if a problem occurred.
	 * @return
	 * @throws ExecutionException
	 */
	public T runHere() throws ExecutionException {
		runPerform();
		return get();
	}
	
	public T get() throws ExecutionException {
		waitForFinish();
		if (wrappedException != null) {
			throw wrappedException;
		} else {
			return resultValue;
		}
	}
	
	private void runPerform() {
		if (hasStarted) {
			waitForFinish();
			return;
		}
		hasStarted = true;
		try {
			resultValue = perform();
		}
		catch (Exception e) {
			resultException = e;
			wrappedException = new ExecutionException(e);
		}
		synchronized (hasFinished) {
			hasFinished.set(true);
			hasFinished.notifyAll();
		}
		done();
	}
	
	private void waitForFinish() {
		synchronized (hasFinished) {
			while(!hasFinished.get()) {
				try {
					hasFinished.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
