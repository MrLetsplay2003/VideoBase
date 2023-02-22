package me.mrletsplay.videobase.webinterface.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.misc.FriendlyException;
import me.mrletsplay.simplehttpserver.http.document.HttpDocument;
import me.mrletsplay.simplehttpserver.http.exception.HttpNotFoundException;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.videobase.util.ThumbnailUtil;
import me.mrletsplay.webinterfaceapi.session.Session;

public class VideoThumbnailDocument implements HttpDocument {

	public static final String URL = "/_internal/thumbnail/video";

	private static final BufferedImage FALLBACK_THUMBNAIL = generateFallbackThumbnail();

	@Override
	public void createContent() {
		if(!Session.requireSession()) return;

		HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
		URLEncoded query = ctx.getRequestedPath().getQuery();
		String collectionID = query.getFirst("collection");
		String videoID = query.getFirst("video");
		if(collectionID == null || videoID == null) throw new HttpNotFoundException();

		String cacheID = collectionID + "/" + videoID;

		BufferedImage image = VideoBase.getThumbnailCache().get(cacheID);

		if(image == null) {
			VideoCollection collection = VideoBase.getLibrary().getCollection(collectionID);
			if(collection == null) throw new HttpNotFoundException();

			Video video = collection.getVideo(videoID);
			if(video == null) throw new HttpNotFoundException();

			image = video.getThumbnailImage();
			if(image == null) image = FALLBACK_THUMBNAIL;
			image = ThumbnailUtil.rescaleVideoThumbnail(image); // TODO: save rescaled thumbnail?

			VideoBase.getThumbnailCache().add(cacheID, image);
		}

		try {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bOut);
			ctx.getServerHeader().setCompressionEnabled(false);
			ctx.getServerHeader().setContent("image/png", bOut.toByteArray());
		} catch (FileNotFoundException e) {
			throw new HttpNotFoundException();
		} catch (IOException e) {
			throw new FriendlyException(e);
		}
	}

	private static BufferedImage generateFallbackThumbnail() {
		BufferedImage thumbnail = new BufferedImage(720, 480, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thumbnail.createGraphics();
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, 720, 480);
		return thumbnail;
	}

}
