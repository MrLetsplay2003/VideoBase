package me.mrletsplay.videobase.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.json.JSONObject;

public class Video {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail";

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

	public BufferedImage getThumbnailImage() {
		// TODO: generate thumbnail from collection thumbnail if available

		if(getThumbnail() == null) return null;

		Path thumbnailPath = Path.of(getThumbnail());
		if(!Files.exists(thumbnailPath)) return null;

		try {
			return ImageIO.read(thumbnailPath.toFile());
		}catch(IOException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return path.toString();
	}

}
