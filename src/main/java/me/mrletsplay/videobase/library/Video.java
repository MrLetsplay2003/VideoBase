package me.mrletsplay.videobase.library;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import me.mrletsplay.mrcore.json.JSONObject;

public class Video {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail";

	private Path path;
	private JSONObject metadata;

	public Video(Path path, JSONObject metadata) {
		this.path = path;
		this.metadata = metadata;
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
		// TODO: use provided thumbnail or generate one
		return null;
	}

	@Override
	public String toString() {
		return path.toString();
	}

}
