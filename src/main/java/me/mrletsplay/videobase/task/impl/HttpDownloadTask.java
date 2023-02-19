package me.mrletsplay.videobase.task.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import me.mrletsplay.videobase.exception.TaskException;
import me.mrletsplay.videobase.exception.URLProxyException;
import me.mrletsplay.videobase.proxy.URLProxy;
import me.mrletsplay.videobase.task.Task;

public class HttpDownloadTask extends Task {

	private String url;
	private Path toPath;
	private URLProxy proxy;

	public HttpDownloadTask(String name, String url, Path toPath, URLProxy proxy) {
		super(UUID.randomUUID().toString(), name);
		this.url = url;
		this.toPath = toPath;
		this.proxy = proxy;
	}

	@Override
	protected void runTask() {
		InputStream in;
		try {
			if(proxy != null) {
					in = proxy.openStream(url.toString());
			}else {
				in = URI.create(url).toURL().openStream();
			}

			OutputStream out = Files.newOutputStream(toPath);
			in.transferTo(out);
		} catch (URLProxyException | IOException e) {
			throw new TaskException(e);
		}
	}

}
