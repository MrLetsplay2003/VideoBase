package me.mrletsplay.videobase.task.impl;

import java.util.UUID;

import me.mrletsplay.videobase.task.Task;

public class DummyTask extends Task {

	public DummyTask(String name) {
		super(UUID.randomUUID().toString(), name);
	}

	@Override
	protected void runTask() {
		int progress = 0;
		while(progress < 100) {
			progress++;
			progress(progress / 100d);
			status("Status");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
