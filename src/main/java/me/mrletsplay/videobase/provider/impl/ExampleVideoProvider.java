package me.mrletsplay.videobase.provider.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.provider.SearchResult;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoInfo;
import me.mrletsplay.videobase.provider.VideoProvider;
import me.mrletsplay.videobase.provider.VideoSource;
import me.mrletsplay.videobase.util.Cache;

public class ExampleVideoProvider implements VideoProvider {

	private Cache<VideoSource> sourceCache;

	private static final ExampleVideoProvider INSTANCE = new ExampleVideoProvider();
	private static final List<VideoCollectionInfo> COLLECTIONS = new ArrayList<>();
	static {
		COLLECTIONS.add(new ExampleVideoCollectionInfo(INSTANCE, "Collection One",
			Arrays.asList(
				new ExampleVideoInfo(INSTANCE, "video-1", "Video One"),
				new ExampleVideoInfo(INSTANCE, "video-2", "Video Two")
			)));
		COLLECTIONS.add(new ExampleVideoCollectionInfo(INSTANCE, "Collection Two",
				Arrays.asList(
					new ExampleVideoInfo(INSTANCE, "video-3", "Video Three"),
					new ExampleVideoInfo(INSTANCE, "video-4", "Video Four")
			)));
	}

	@Override
	public List<SearchResult> findVideoCollections(String query) {
		return COLLECTIONS.stream()
			.filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
			.map(c -> {
				JSONObject meta = new JSONObject();
				meta.put(SearchResult.META_NAME, c.getName());
				return new SearchResult(INSTANCE, c.getID(), meta);
			})
			.collect(Collectors.toList());
	}

	@Override
	public VideoCollectionInfo getCollectionInfo(String collectionID) {
		return COLLECTIONS.stream()
			.filter(c -> c.getID().equals(collectionID))
			.findFirst().orElse(null);
	}

	@Override
	public List<? extends VideoInfo> getCollectionVideos(String collectionID) {
		return Optional.ofNullable(getCollectionInfo(collectionID))
			.map(c -> c.loadVideos())
			.orElse(null);
	}

	@Override
	public List<? extends VideoSource> getVideoSources(String videoID) {
		ExampleVideoSource source = new ExampleVideoSource();
		String id = sourceCache.add(source);
		source.setID(id);
		return Collections.singletonList(source);
	}

	@Override
	public VideoSource getVideoSource(String id) {
		return null;
	}

}
