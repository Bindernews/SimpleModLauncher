package com.github.modlauncher.workers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FutureCallable<V> implements Future<V>, Callable<V> {

	private final FutureTask<V> task;
	private final FutureCallable<V> self;
	
	public FutureCallable() {
		self = this;
		task = new FutureTask<V>(this) {
			@Override
			protected void done() {
				self.done();
			}
		};
		
	}
	
	
	@Override
	public abstract V call() throws Exception;
	
	/**
	 * Called when the task is complete
	 */
	protected void done() {}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return task.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return task.isCancelled();
	}

	@Override
	public boolean isDone() {
		return task.isDone();
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return task.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return task.get(timeout, unit);
	}
	
	/**
	 * Begins running this task
	 */
	public void execute() {
		try {
			task.get(1, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
