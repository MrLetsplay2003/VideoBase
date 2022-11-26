package me.mrletsplay.videobase.provider;

import java.time.temporal.TemporalUnit;
import java.util.List;

import me.mrletsplay.videobase.util.Cache;

public abstract class AbstractCachingVideoProvider implements VideoProvider {

	private Cache<VideoCollectionInfo> videoCollectionInfoCache;
	private Cache<List<? extends VideoInfo>> videoInfoCache;
	private Cache<List<? extends VideoSource>> videoSourceByVideoCache; // TODO: improve
	private Cache<VideoSource> videoSourceCache;

	public AbstractCachingVideoProvider(long timeout, TemporalUnit unit) {
		this.videoCollectionInfoCache = new Cache<>(timeout, unit);
		this.videoInfoCache = new Cache<>(timeout, unit);
		this.videoSourceByVideoCache = new Cache<>(timeout, unit);
		this.videoSourceCache = new Cache<>(timeout, unit);
	}

	public abstract VideoCollectionInfo retrieveCollectionInfo(String collectionID);

	@Override
	public VideoCollectionInfo getCollectionInfo(String collectionID) {
		VideoCollectionInfo info = videoCollectionInfoCache.get(collectionID);
		if(info != null) return info;
		info = retrieveCollectionInfo(collectionID);
		videoCollectionInfoCache.add(collectionID, info);
		return info;
	}

	public abstract List<? extends VideoInfo> retrieveCollectionVideos(String collectionID);

	@Override
	public List<? extends VideoInfo> getCollectionVideos(String collectionID) {
		List<? extends VideoInfo> vids = videoInfoCache.get(collectionID);
		if(vids != null) return vids;
		vids = retrieveCollectionVideos(collectionID);
		videoInfoCache.add(collectionID, vids);
		return vids;
	}

	public abstract List<? extends VideoSource> retrieveVideoSources(String videoID);

	@Override
	public List<? extends VideoSource> getVideoSources(String videoID) {
		List<? extends VideoSource> srcs = videoSourceByVideoCache.get(videoID);
		if(srcs != null) return srcs;
		srcs = retrieveVideoSources(videoID);
		videoSourceByVideoCache.add(videoID, srcs);
		srcs.forEach(s -> videoSourceCache.add(s.getID(), s));
		return srcs;
	}

	public abstract VideoSource retrieveVideoSource(String videoID);

	@Override
	public VideoSource getVideoSource(String id) {
		VideoSource src = videoSourceCache.get(id);
		if(src != null) return src;
		src = retrieveVideoSource(id);
		if(src == null) return null; // Sources are not required to be retrievable
		videoSourceCache.add(id, src);
		return src;
	}

}
