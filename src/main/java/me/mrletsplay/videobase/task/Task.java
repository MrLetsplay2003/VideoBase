package me.mrletsplay.videobase.task;

import java.util.ArrayList;
import java.util.List;

public abstract class Task {

	private Object lock = new Object();

	private String
		id,
		name,
		statusMessage;

	private TaskState state;
	private Exception exception;
	private double progress;
	private Thread runningThread;
	private List<Runnable> callbacks;

	public Task(String id, String name) {
		this.id = id;
		this.name = name;
		this.state = TaskState.QUEUED;
		this.callbacks = new ArrayList<>();
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public TaskState getState() {
		return state;
	}

	public Exception getException() {
		return exception;
	}

	public double getProgress() {
		return progress;
	}

	public void run() {
		synchronized(lock) {
			if(isDone()) throw new IllegalStateException("Task is in " + state + " state");
			state = TaskState.RUNNING;
			runningThread = Thread.currentThread();
		}

		try {
			runTask();
			if(state == TaskState.RUNNING) state = TaskState.FINISHED;
		}catch(Exception e) {
			error(e);
		}

		synchronized(lock) {
			runningThread = null;
			Thread.interrupted(); // Clear interrupted flag
		}
	}

	public void cancel() {
		synchronized(lock) {
			if(isDone()) return;

			if(state == TaskState.RUNNING) {
				runningThread.interrupt();
				runningThread = null;
			}

			state = TaskState.CANCELLED;
		}

		onCancel();

		runCallbacks();
	}

	public boolean isDone() {
		return state != TaskState.QUEUED && state != TaskState.RUNNING;
	}

	public void onSuccess(Runnable onSuccess) {
		callbacks.add(() -> {
			if(state != TaskState.FINISHED) return;
			onSuccess.run();
		});
	}

	public void onError(Runnable onError) {
		callbacks.add(() -> {
			if(state != TaskState.ERRORED) return;
			onError.run();
		});
	}

	public void onCancel(Runnable onCancel) {
		callbacks.add(() -> {
			if(state != TaskState.CANCELLED) return;
			onCancel.run();
		});
	}

	private void runCallbacks() {
		callbacks.forEach(Runnable::run);
	}

	protected void status(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	protected void progress(double progress) {
		this.progress = progress;
	}

	protected void error(Exception exception) {
		synchronized (lock) {
			if(isDone()) return;
			state = TaskState.ERRORED;
		}

		this.exception = exception;
		this.statusMessage = exception.getMessage();
		onError();

		runCallbacks();
	}

	protected abstract void runTask();

	protected void onCancel() {}

	protected void onError() {};

}
