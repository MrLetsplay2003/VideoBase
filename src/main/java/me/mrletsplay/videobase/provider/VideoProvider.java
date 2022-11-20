package me.mrletsplay.videobase.provider;

import java.util.List;

public interface VideoProvider {

	public List<SearchResult> findVideoCollections(String query);

	public VideoCollectionInfo getCollectionInfo(String collectionID);

	public List<? extends VideoInfo> getCollectionVideos(String collectionID);

	public List<? extends VideoSource> getVideoSources(String videoID);

	public VideoSource getVideoSource(String id);

}
