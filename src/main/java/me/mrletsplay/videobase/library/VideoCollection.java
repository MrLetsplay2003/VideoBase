package me.mrletsplay.videobase.library;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class VideoCollection {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail",
		META_REMOTE_PROVIDER = "remote.provider",
		META_REMOTE_ID = "remote.id";

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

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

	public VideoCollectionInfo retrieveInfo() {
		VideoProvider provider = metadata.optString(META_REMOTE_PROVIDER)
			.map(VideoBase::getProvider)
			.orElse(null);
		if(provider == null) return null;

		return metadata.optString(META_REMOTE_ID)
			.map(provider::getCollectionInfo)
			.orElse(null);
	}

	@Override
	public String toString() {
		return videos.toString();
	}

}
