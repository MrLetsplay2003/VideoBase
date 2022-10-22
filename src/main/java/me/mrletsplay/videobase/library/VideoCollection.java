package me.mrletsplay.videobase.library;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;

public class VideoCollection {

	private JSONObject metadata;
	private List<Video> videos;

	public VideoCollection(JSONObject metadata, List<Video> videos) {
		this.metadata = metadata;
		this.videos = videos;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public String getName() {
		return metadata.optString("name").orElse("Unnamed Collection");
	}

	@Override
	public String toString() {
		return videos.toString();
	}

}
