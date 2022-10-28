package me.mrletsplay.videobase;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import me.mrletsplay.videobase.task.Task;
import me.mrletsplay.videobase.task.TaskQueue;
import me.mrletsplay.videobase.task.TaskState;

public class TaskQueueTest {

	private Task createNormalTask() {
		return new Task("normal", "Normal") {
			@Override
			protected void runTask() {
				for(int i = 0; i < 3; i++) {
					System.out.println("Hello #" + i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Interrupted");
						break;
					}
				}
			}
		};
	}

	private Task createErroringTask() {
		return new Task("erroring", "Erroring") {
			@Override
			protected void runTask() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
				throw new RuntimeException("Oh no, I crashed");
			}
		};
	}

	@Test
	public void testTaskQueueStop() {
		TaskQueue q = new TaskQueue(1);
		Task a = createNormalTask();
		q.addTask(a);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		q.stop(true);
		q.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(TaskState.CANCELLED, a.getState());

		q = new TaskQueue(1);
		Task b = createNormalTask();
		q.addTask(b);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		q.stop(false);
		q.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(TaskState.FINISHED, b.getState());
	}

	@Test
	public void testTaskQueueError() {
		TaskQueue q = new TaskQueue(1);
		Task a = createErroringTask();
		q.addTask(a);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		q.stop(false);
		q.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(TaskState.ERRORED, a.getState());
	}

}
