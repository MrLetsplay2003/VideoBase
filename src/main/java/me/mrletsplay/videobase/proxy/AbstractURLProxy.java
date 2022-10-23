package me.mrletsplay.videobase.proxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.videobase.exception.URLProxyException;

public abstract class AbstractURLProxy implements URLProxy {

	// Open a stream for a data URL (https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URLs)
	protected InputStream openDataURL(String url) throws IllegalArgumentException {
		if(!url.startsWith("data:")) throw new IllegalArgumentException("Not a data URL");
		String[] spl = url.substring("data:".length()).split(",", 2);
		if(spl.length != 2) throw new IllegalArgumentException("Invalid data URL");
		String mimeType = spl[0];
		boolean b64 = !mimeType.isEmpty() && mimeType.endsWith(";base64"); // empty == text/plain;charset=US-ASCII
		if(b64) mimeType = mimeType.substring(0, mimeType.length() - ";base64".length());
		byte[] data = b64 ? Base64.getDecoder().decode(spl[1]) : HttpUtils.urlDecode(spl[1]).getBytes(StandardCharsets.UTF_8);
		return new ByteArrayInputStream(data);
	}

	protected abstract InputStream openRegularURL(String url) throws URLProxyException;

	@Override
	public InputStream openStream(String url) throws URLProxyException {
		if(url.startsWith("data:")) {
			try {
				return openDataURL(url);
			}catch(IllegalArgumentException e) {
				throw new URLProxyException("Failed to open data URL", e);
			}
		}
		return openRegularURL(url);
	}

}
