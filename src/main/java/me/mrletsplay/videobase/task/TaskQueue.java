package me.mrletsplay.videobase.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskQueue {

	private Object lock = new Object();

	private ExecutorService executor;
	private List<Task> queuedTasks;
	private List<Task> runningTasks;
	private List<Task> finishedTasks;

	public TaskQueue(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
		this.queuedTasks = new ArrayList<>();
		this.runningTasks = new ArrayList<>();
		this.finishedTasks = new ArrayList<>();
		for(int i = 0; i < threads; i++) {
			executor.submit(() -> {
				while(!executor.isShutdown()) {
					if(!runNextTask()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
			});
		}
	}

	public void addTask(Task task) {
		synchronized (lock) {
			queuedTasks.add(task);
		}
	}

	public void stop(boolean cancelRunningTasks) {
		executor.shutdown();
		if(cancelRunningTasks) {
			synchronized (lock) {
				queuedTasks.stream()
					.filter(t -> t.getState() == TaskState.RUNNING)
					.forEach(t -> t.cancel());
			}
		}
	}

	public void awaitTermination(long timeout, TimeUnit unit) {
		try {
			if(!executor.awaitTermination(timeout, unit)) executor.shutdownNow();
		} catch (InterruptedException e) {}
	}

	private boolean runNextTask() {
		Task t;
		synchronized (lock) {
			if(queuedTasks.isEmpty()) return false;
			t = queuedTasks.remove(0);
			runningTasks.add(t);
		}

		if(t.getState() != TaskState.CANCELLED) t.run();

		synchronized (lock) {
			runningTasks.remove(t);
			finishedTasks.add(t);
		}
		return true;
	}

	public List<Task> getQueuedTasks() {
		synchronized (lock) {
			return new ArrayList<>(this.queuedTasks);
		}
	}

	public List<Task> getRunningTasks() {
		synchronized (lock) {
			return new ArrayList<>(this.runningTasks);
		}
	}

	public List<Task> getFinishedTasks() {
		synchronized (lock) {
			return new ArrayList<>(this.finishedTasks);
		}
	}

	public List<Task> getAllTasks() {
		synchronized (lock) {
			List<Task> allTasks = new ArrayList<>();
			allTasks.addAll(queuedTasks);
			allTasks.addAll(runningTasks);
			allTasks.addAll(finishedTasks);
			return allTasks;
		}
	}

}
