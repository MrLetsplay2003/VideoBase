package me.mrletsplay.videobase.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskQueue {

	private Object lock = new Object();

	private ExecutorService executor;
	private List<Task> tasks;
	private List<Task> finishedTasks;

	public TaskQueue(int threads) {
		this.executor = Executors.newFixedThreadPool(threads);
		this.tasks = new ArrayList<>();
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
			tasks.add(task);
		}
	}

	public void stop(boolean cancelRunningTasks) {
		executor.shutdown();
		if(cancelRunningTasks) {
			synchronized (lock) {
				tasks.stream()
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
			if(tasks.isEmpty()) return false;
			t = tasks.get(0);
		}

		 if(t.getState() != TaskState.CANCELLED) t.run();

		synchronized (lock) {
			tasks.remove(t);
			finishedTasks.add(t);
		}
		return true;
	}

	public List<Task> getTasks() {
		synchronized (lock) {
			return new ArrayList<>(this.tasks);
		}
	}

	public List<Task> getFinishedTasks() {
		synchronized (lock) {
			return new ArrayList<>(this.finishedTasks);
		}
	}

	// TODO: add getTasks(), ...

}
