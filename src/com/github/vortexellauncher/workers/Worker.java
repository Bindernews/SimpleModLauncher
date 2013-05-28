package com.github.vortexellauncher.workers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Worker<T> implements Future<T> {

	// this HAS to be a single thread executor because the correctness
	// of the done() method being called after the perform method is run
	// is dependent on the two tasks being run after each other.
	private ExecutorService executor = Executors.newSingleThreadExecutor(); 
	private Worker<T> self = this;
	private Future<T> future = null;
	private boolean didCancel = false;
	
	public Worker() {
	}

	public T perform() {return null;}

	public void execute() {
		if (didCancel || isStarted()) {
			return;
		}
		future = executor.submit(new Callable<T>() {
			public T call() {
				return self.perform();
			}
		});
		executor.submit(new Runnable() {
			public void run() {
				self.done();
			}
		});
	}
	
	public void done() {}
	
	public boolean isStarted() {
		return future != null;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (isStarted()) {
			return future.cancel(mayInterruptIfRunning);
		}
		else {
			didCancel = true;
			return didCancel;
		}
	}

	@Override
	public boolean isCancelled() {
		return isStarted() ? future.isCancelled() : didCancel;
	}

	@Override
	public boolean isDone() {
		return isStarted() ? future.isDone() : didCancel;
	}

	/**
	 * Functions exactly as Future.get() however execute() will be called if the task has not been started. 
	 */
	@Override
	public T get() throws InterruptedException, ExecutionException {
		if (!isStarted()) {
			execute();
		}
		return future.get();
	}

	/**
	 * Functions exactly as Future.get() however execute() will be called if the task has not been started.
	 */
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		if (!isStarted()) {
			execute();
		}
		return future.get(timeout, unit);
	}
}
