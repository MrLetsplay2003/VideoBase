package me.mrletsplay.videobase.provider;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;

public class VideoInfo {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail";

	private VideoProvider provider;
	private String id;
	private JSONObject metadata;

	public VideoInfo(VideoProvider provider, String id, JSONObject metadata) {
		this.provider = provider;
		this.id = id;
		this.metadata = metadata;
	}

	public VideoProvider getProvider() {
		return provider;
	}

	public String getID() {
		return id;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse(null);
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

	public List<? extends VideoSource> loadSources() {
		return provider.getVideoSources(id);
	}

}
