package me.mrletsplay.videobase.provider.impl;

import java.nio.file.Path;
import java.util.UUID;

import me.mrletsplay.videobase.provider.VideoSource;
import me.mrletsplay.videobase.proxy.URLProxy;
import me.mrletsplay.videobase.task.Task;
import me.mrletsplay.videobase.task.impl.HttpDownloadTask;

public class ExampleVideoSource implements VideoSource {

	private String id;

	public ExampleVideoSource() {
		this.id = UUID.randomUUID().toString();
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public Task download(Path toPath, URLProxy proxy) {
		return new HttpDownloadTask("Download Goblin", "https://cringe-studios.com/dev/videobase/lqGOBLIN.mp4", toPath, proxy);
	}

}
