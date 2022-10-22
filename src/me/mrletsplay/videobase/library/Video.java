package me.mrletsplay.videobase.library;

import java.nio.file.Path;

import me.mrletsplay.mrcore.json.JSONObject;

public class Video {

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

	@Override
	public String toString() {
		return path.toString();
	}

}
