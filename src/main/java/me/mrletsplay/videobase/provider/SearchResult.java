package me.mrletsplay.videobase.provider;

import me.mrletsplay.mrcore.json.JSONObject;

public class SearchResult {

	public static final String
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail";

	private VideoProvider provider;
	private String collectionID;
	private JSONObject metadata;
	private VideoCollectionInfo cachedInfo;

	public SearchResult(VideoProvider provider, String collectionID, JSONObject metadata, VideoCollectionInfo info) {
		this.provider = provider;
		this.collectionID = collectionID;
		this.metadata = metadata;
		this.cachedInfo = info;
	}

	public SearchResult(VideoProvider provider, String collectionID, JSONObject metadata) {
		this(provider, collectionID, metadata, null);
	}

	public VideoProvider getProvider() {
		return provider;
	}

	public String getCollectionID() {
		return collectionID;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public VideoCollectionInfo loadInfo() {
		if(cachedInfo != null) return cachedInfo;
		return cachedInfo = provider.getCollectionInfo(collectionID);
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Collection " + collectionID);
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

}
