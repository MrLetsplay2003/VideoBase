package me.mrletsplay.videobase.webinterface.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.exception.HttpNotFoundException;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.webinterfaceapi.session.Session;

public class VideoDocument implements HttpDocument {

	public static final String URL = "/_internal/video";

	@Override
	public void createContent() {
		if(!Session.requireSession()) return;

		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		URLEncoded query = ctx.getRequestedPath().getQuery();
		String collectionID = query.getFirst("collection");
		String videoID = query.getFirst("video");
		if(collectionID == null || videoID == null) throw new HttpNotFoundException();

		VideoCollection collection = VideoBase.getLibrary().getCollection(collectionID);
		if(collection == null) throw new HttpNotFoundException();

		Video video = collection.getVideo(videoID);
		if(video == null) throw new HttpNotFoundException();

		File videoFile = video.getPath().toFile();
		try {
			ctx.getServerHeader().setCompressionEnabled(false);
			ctx.getServerHeader().setContent("video/mp4", new FileInputStream(videoFile), videoFile.length());
		} catch (FileNotFoundException e) {
			throw new HttpNotFoundException();
		}
	}

}
