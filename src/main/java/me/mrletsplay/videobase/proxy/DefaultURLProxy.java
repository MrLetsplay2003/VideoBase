package me.mrletsplay.videobase.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collections;
import java.util.List;

import me.mrletsplay.videobase.exception.URLProxyException;

public class DefaultURLProxy extends AbstractURLProxy {

	private HttpClient client;

	public DefaultURLProxy(Proxy proxy) throws IllegalArgumentException {
		if(proxy.type() != Type.HTTP) throw new IllegalArgumentException("Not an HTTP proxy");
		this.client = HttpClient.newBuilder()
			.proxy(new ProxySelector() {

				@Override
				public List<Proxy> select(URI uri) {
					return Collections.singletonList(proxy);
				}

				@Override
				public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}
			})
			.followRedirects(HttpClient.Redirect.NORMAL)
			.version(Version.HTTP_2)
			.build();
	}

	@Override
	public void close() throws IOException {}

	@Override
	protected InputStream openRegularURL(String url) throws URLProxyException {
		try {
			return client.send(HttpRequest.newBuilder(URI.create(url)).build(), BodyHandlers.ofInputStream()).body();
		} catch (IOException | InterruptedException e) {
			throw new URLProxyException("Failed to open URL", e);
		}
	}

	@Override
	public HttpClient client() {
		return client;
	}

}
