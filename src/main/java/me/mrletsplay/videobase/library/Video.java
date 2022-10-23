package me.mrletsplay.videobase.library;

import java.nio.file.Path;

import me.mrletsplay.mrcore.json.JSONObject;

public class Video {

	public static final String
		META_NAME = "name";

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

	@Override
	public String toString() {
		return path.toString();
	}

}
