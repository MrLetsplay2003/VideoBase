package me.mrletsplay.videobase.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.exception.LibraryUpdateException;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class VideoCollection {

	public static final String
		INDEX_FILE_NAME = "index.json",
		META_NAME = "name",
		META_THUMBNAIL = "thumbnail",
		META_REMOTE_PROVIDER = "remote.provider",
		META_REMOTE_ID = "remote.id";

	private String id;
	private Path path;
	private JSONObject metadata;
	private List<Video> videos;

	public VideoCollection(String id, Path path, JSONObject metadata) {
		this.id = id;
		this.path = path;
		this.metadata = metadata;
		this.videos = new ArrayList<>();
	}

	public String getID() {
		return id;
	}

	public Path getPath() {
		return path;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public void addVideo(Video video) {
		this.videos.add(video);
	}

	public Video addNewVideo(String videoID, Path videoPath, JSONObject metadata) throws LibraryUpdateException {
		try {
			Path indexPath = path.resolve(VideoCollection.INDEX_FILE_NAME);
			JSONObject index = new JSONObject(Files.readString(indexPath));
			JSONArray videos = index.optJSONArray("videos").orElse(new JSONArray());
			JSONObject video = new JSONObject();
			video.put("id", videoID);
			video.put("path", path.relativize(videoPath).toString());
			video.put("metadata", metadata);
			videos.add(video);
			index.put("videos", videos);
			Files.writeString(indexPath, index.toFancyString(), StandardCharsets.UTF_8);
			Video v = new Video(this, videoID, videoPath, metadata);
			videos.add(v);
			return v;
		} catch (IOException e) {
			throw new LibraryUpdateException(e);
		}
	}

	public Video addNewVideoFromInfo(VideoInfo info, Path videoPath) throws LibraryUpdateException {
		return addNewVideo(info.getID(), videoPath, info.getMetadata()); // TODO: download thumbnail, create proper metadata
	}

	public List<Video> getVideos() {
		return videos;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Unnamed Collection");
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
	}

	public String getRemoteProviderID() {
		return metadata.optString(META_REMOTE_PROVIDER).orElse(null);
	}

	public String getRemoteID() {
		return metadata.optString(META_REMOTE_ID).orElse(null);
	}

	public BufferedImage getThumbnailImage() {
		if(getThumbnail() == null) return null;

		Path thumbnailPath = Path.of(path.toString(), getThumbnail());
		if(!Files.exists(thumbnailPath)) return null;

		try {
			return ImageIO.read(thumbnailPath.toFile());
		}catch(IOException e) {
			return null;
		}
	}

	public Video getVideo(String id) {
		return videos.stream()
			.filter(v -> v.getID().equals(id))
			.findFirst().orElse(null);
	}

	public VideoCollectionInfo retrieveInfo() {
		if(getRemoteID() == null) return null;

		String providerID = getRemoteProviderID();
		if(providerID == null) return null;

		VideoProvider provider = VideoBase.getProvider(providerID);
		if(provider == null) return null;

		return provider.getCollectionInfo(getRemoteID());
	}

	@Override
	public String toString() {
		return videos.toString();
	}

}
