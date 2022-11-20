package me.mrletsplay.videobase.provider.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.provider.VideoInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class ExampleVideoInfo extends VideoInfo {

	private static JSONObject createMeta(String name) {
		JSONObject meta = new JSONObject();
		meta.put(META_NAME, name);
		return meta;
	}

	public ExampleVideoInfo(VideoProvider provider, String id, String name) {
		super(provider, id, createMeta(name));
	}

}
