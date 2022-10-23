package me.mrletsplay.videobase.provider;

import java.util.List;

public interface VideoProvider {

	public List<SearchResult> findVideoCollections(String query);

	public VideoCollectionInfo getCollectionInfo(String collectionID);

	public List<VideoInfo> getCollectionVideos(String collectionID);

	public List<VideoSource> getVideoSources(String videoID);

}
