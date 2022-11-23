package me.mrletsplay.videobase.library;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;

public class VideoCollection {

	public static final String
		META_NAME = "name";

	private String id;
	private JSONObject metadata;
	private List<Video> videos;

	public VideoCollection(String id, JSONObject metadata, List<Video> videos) {
		this.id = id;
		this.metadata = metadata;
		this.videos = videos;
	}

	public String getID() {
		return id;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Unnamed Collection");
	}

	@Override
	public String toString() {
		return videos.toString();
	}

}
