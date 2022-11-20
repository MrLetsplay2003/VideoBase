package me.mrletsplay.videobase.provider.impl;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class ExampleVideoCollectionInfo extends VideoCollectionInfo {

	private static JSONObject createMeta(String name) {
		JSONObject meta = new JSONObject();
		meta.put(META_NAME, name);
		return meta;
	}

	public ExampleVideoCollectionInfo(VideoProvider provider, String name, List<ExampleVideoInfo> infos) {
		super(provider, createMeta(name), infos);
	}

}
