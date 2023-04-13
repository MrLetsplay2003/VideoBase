package me.mrletsplay.videobase.proxy;

import java.io.InputStream;
import java.net.http.HttpClient;

import me.mrletsplay.videobase.exception.URLProxyException;

public interface URLProxy {

	public InputStream openStream(String url) throws URLProxyException;

	public HttpClient client();

}
