package me.mrletsplay.videobase.task.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
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
			URI uri = URI.create(url);
			HttpClient client = proxy == null ? HttpClient.newHttpClient() : proxy.client();
			HttpResponse<Void> head = client.send(HttpRequest.newBuilder(uri)
				.method("HEAD", BodyPublishers.noBody())
				.build(), BodyHandlers.discarding());

			long length = head.headers().firstValue("Content-Length")
				.map(Long::parseLong)
				.orElse(-1L);

			if(proxy != null) {
				in = proxy.openStream(url.toString());
			}else {
				in = URI.create(url).toURL().openStream();
			}

			try(OutputStream out = Files.newOutputStream(toPath)) {
				byte[] buf = new byte[4096];
				int len = 0;
				long totalLen = 0;
				while((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
					totalLen += len;
					if(length > 0) {
						progress((double) totalLen / length);
					}
				}
			}
		} catch (InterruptedException | URLProxyException | IOException e) {
			throw new TaskException(e);
		}
	}

}
