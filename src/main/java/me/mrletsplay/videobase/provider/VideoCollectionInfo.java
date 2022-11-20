package me.mrletsplay.videobase.provider;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;

public abstract class VideoCollectionInfo {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail";

	private VideoProvider provider;
	private String id;
	private JSONObject metadata;
	private List<? extends VideoInfo> cachedVideos;

	public VideoCollectionInfo(VideoProvider provider, JSONObject metadata, List<? extends VideoInfo> videos) {
		this.provider = provider;
		this.metadata = metadata;
		this.cachedVideos = videos;
	}

	public VideoCollectionInfo(VideoProvider provider, JSONObject metadata) {
		this(provider, metadata, null);
	}

	public String getID() {
		return id;
	}

	public List<? extends VideoInfo> loadVideos() {
		if(cachedVideos != null) return cachedVideos;
		return cachedVideos = provider.getCollectionVideos(id);
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Collection " + id);
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

}
