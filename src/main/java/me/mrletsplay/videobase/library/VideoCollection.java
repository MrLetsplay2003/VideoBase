package me.mrletsplay.videobase.library;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoProvider;

public class VideoCollection {

	public static final String
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

	public List<Video> getVideos() {
		return videos;
	}

	public String getName() {
		return metadata.optString(META_NAME).orElse("Unnamed Collection");
	}

	public String getThumbnail() {
		return metadata.optString(META_THUMBNAIL).orElse(null);
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
		VideoProvider provider = metadata.optString(META_REMOTE_PROVIDER)
			.map(VideoBase::getProvider)
			.orElse(null);
		if(provider == null) return null;

		return metadata.optString(META_REMOTE_ID)
			.map(provider::getCollectionInfo)
			.orElse(null);
	}

	@Override
	public String toString() {
		return videos.toString();
	}

}
