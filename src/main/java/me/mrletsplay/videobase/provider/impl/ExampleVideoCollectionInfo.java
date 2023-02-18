package me.mrletsplay.videobase.provider.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class ExampleVideoCollectionInfo extends VideoCollectionInfo {

	private static JSONObject createMeta(String name) {
		JSONObject meta = new JSONObject();
		meta.put(META_NAME, name);
		meta.put(META_THUMBNAIL, "https://testimages.org/img/testimages_screenshot.jpg");
		return meta;
	}

	public ExampleVideoCollectionInfo(VideoProvider provider, String id, String name, List<ExampleVideoInfo> infos) {
		super(provider, id, createMeta(name), infos);
	}

}
