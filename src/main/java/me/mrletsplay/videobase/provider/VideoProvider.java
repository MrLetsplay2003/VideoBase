package me.mrletsplay.videobase.provider;

import java.util.List;

public interface VideoProvider {

	/**
	 * @return This provider's unique identifier
	 */
	public String getID();

	/**
	 * Searches for video collections containing a specific query
	 * @param query The search query
	 * @return A list of matching video sources
	 */
	public List<SearchResult> findVideoCollections(String query);

	/**
	 * Retrieves a video collection's info by its id
	 * @param collectionID The id of the collection
	 * @return The video collection's info
	 */
	public VideoCollectionInfo getCollectionInfo(String collectionID);

	/**
	 * Retrieves a video collection's videos by its id
	 * @param collectionID The id of the collection
	 * @return The video collection's videos
	 */
	public List<? extends VideoInfo> getCollectionVideos(String collectionID);

	/**
	 * Retrieves a video's info by its id
	 * @param videoID The id of the video
	 * @return The video's info
	 */
	public VideoInfo getVideoInfo(String videoID);

	/**
	 * Provides the sources of a video. Should return the sources sorted by best to worst.
	 * @param videoID The id of the video to retrieve video sources of
	 * @return The available sources for the video
	 */
	public List<? extends VideoSource> getVideoSources(String videoID);

	/**
	 * Retrieves a video source by its id
	 * @param id The id of the video source
	 * @return The video source
	 */
	public VideoSource getVideoSource(String id);

}
