package me.mrletsplay.videobase.proxy;

import java.io.Closeable;
import java.io.InputStream;
import java.net.http.HttpClient;

import me.mrletsplay.videobase.exception.URLProxyException;

public interface URLProxy extends Closeable {

	public InputStream openStream(String url) throws URLProxyException;

	public HttpClient client();

}
