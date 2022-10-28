package me.mrletsplay.videobase.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskQueue {

	private Object lock;

	private ExecutorService executor;
	private List<Task> tasks;
	private List<Task> finishedTasks;

	public TaskQueue(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
		this.tasks = new ArrayList<>();
		this.finishedTasks = new ArrayList<>();
		for(int i = 0; i < threads; i++) executor.submit(() -> {
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

	public void addTask(Task task) {
		synchronized (lock) {
			tasks.add(task);
		}
	}

	public void stop(boolean awaitTermination) {
		executor.shutdown();
		try {
			if(!executor.awaitTermination(10, TimeUnit.SECONDS)) executor.shutdownNow();
		} catch (InterruptedException e) {}
	}

	private boolean runNextTask() {
		Task t;
		synchronized (lock) {
			if(tasks.isEmpty()) return false;
			t = tasks.get(0);
		}

		t.run();

		synchronized (lock) {
			tasks.remove(t);
			finishedTasks.add(t);
		}
		return true;
	}

	// TODO: add getTasks(), ...

}
