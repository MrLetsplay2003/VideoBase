package me.mrletsplay.videobase.provider;

import java.nio.file.Path;

import me.mrletsplay.videobase.proxy.URLProxy;
import me.mrletsplay.videobase.task.Task;

public interface VideoSource {

	public String getID();

	public Task download(Path toPath, URLProxy proxy);

}
