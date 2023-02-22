package me.mrletsplay.videobase.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.provider.VideoInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class Video {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail",
		META_REMOTE_ID = "remote.id";

	private VideoCollection collection;
	private String id;
	private Path path;
	private JSONObject metadata;

	public Video(VideoCollection collection, String id, Path path, JSONObject metadata) {
		this.collection = collection;
		this.id = id;
		this.path = path;
		this.metadata = metadata;
	}

	public VideoCollection getCollection() {
		return collection;
	}

	public String getID() {
		return id;
	}

	public Path getPath() {
		return path;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Unnamed Video");
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

	public String getRemoteID() {
		return metadata.optString(META_REMOTE_ID).orElse(null);
	}

	public BufferedImage getThumbnailImage() {
		// TODO: generate thumbnail from collection thumbnail if available

		if(getThumbnail() == null) return null;

		Path thumbnailPath = collection.getPath().resolve(getThumbnail());
		if(!Files.exists(thumbnailPath)) return null;

		try {
			return ImageIO.read(thumbnailPath.toFile());
		}catch(IOException e) {
			return null;
		}
	}

	public VideoInfo retrieveInfo() {
		if(getRemoteID() == null) return null;

		String providerID = collection.getRemoteProviderID();
		if(providerID == null) return null;

		VideoProvider provider = VideoBase.getProvider(providerID);
		if(provider == null) return null;

		return provider.getVideoInfo(getRemoteID());
	}

	@Override
	public String toString() {
		return path.toString();
	}

}
