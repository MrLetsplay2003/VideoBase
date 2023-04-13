package me.mrletsplay.videobase.provider.impl;

import java.time.temporal.ChronoUnit;
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

	private Cache<VideoSource> sourceCache = new Cache<>(1, ChronoUnit.HOURS);

	private static final ExampleVideoProvider INSTANCE = new ExampleVideoProvider();
	private static final List<VideoCollectionInfo> COLLECTIONS = new ArrayList<>();
	static {
		COLLECTIONS.add(new VideoCollectionInfo(INSTANCE, "one", new JSONObject("{\"name\":\"Collection One\"}"),
			Arrays.asList(
				new VideoInfo(INSTANCE, "video-1", new JSONObject("{\"name\":\"Video One\"}")),
				new VideoInfo(INSTANCE, "video-2", new JSONObject("{\"name\":\"Video Two\"}"))
			)
		));
		COLLECTIONS.add(new VideoCollectionInfo(INSTANCE, "two", new JSONObject("{\"name\":\"Collection Two\"}"),
			Arrays.asList(
				new VideoInfo(INSTANCE, "video-3", new JSONObject("{\"name\":\"Video Three\"}"))
			)
		));
	}

	@Override
	public String getID() {
		return "example";
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
	public List<VideoInfo> getCollectionVideos(String collectionID) {
		return Optional.ofNullable(getCollectionInfo(collectionID))
			.map(c -> c.loadVideos())
			.orElse(null);
	}

	@Override
	public VideoInfo getVideoInfo(String videoID) {
		return COLLECTIONS.stream()
			.flatMap(c -> c.loadVideos().stream())
			.filter(v -> v.getID().equals(videoID))
			.findFirst().orElse(null);
	}

	@Override
	public List<? extends VideoSource> getVideoSources(String videoID) {
		ExampleVideoSource source = new ExampleVideoSource();
		sourceCache.add(source.getID(), source);
		return Collections.singletonList(source);
	}

	@Override
	public VideoSource getVideoSource(String id) {
		return sourceCache.get(id);
	}

}
