package me.mrletsplay.videobase.library;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONParseException;
import me.mrletsplay.videobase.exception.LibraryLoadException;
import me.mrletsplay.videobase.exception.LibraryUpdateException;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoInfo;

public class Library {

	private Path baseFolder;
	private List<VideoCollection> collections;

	public Library(Path baseFolder) throws LibraryLoadException {
		this.baseFolder = baseFolder;
		this.collections = new ArrayList<>();
		load();
	}

	private synchronized void load() throws LibraryLoadException {
		for(File f : baseFolder.toFile().listFiles()) {
			if(!f.isDirectory()) continue;
			loadCollection(f.toPath());
		}
	}

	private void loadCollection(Path collectionFolder) throws LibraryLoadException {
		Path indexPath = collectionFolder.resolve(VideoCollection.INDEX_FILE_NAME);
		if(!Files.exists(indexPath)) return;
		try {
			JSONObject index = new JSONObject(Files.readString(indexPath, StandardCharsets.UTF_8));

			JSONObject indexMeta = index.optJSONObject("metadata").orElse(new JSONObject());
			JSONArray videos = index.optJSONArray("videos").orElse(null);
			if(videos == null) return;

			VideoCollection collection = new VideoCollection(collectionFolder.getFileName().toString(), collectionFolder, indexMeta);
			for(Object video : videos) {
				if(!(video instanceof JSONObject)) continue;
				JSONObject vid = (JSONObject) video;

				String id = vid.optString("id").orElse(null);
				if(id == null) continue;
				String path = vid.optString("path").orElse(null);
				if(path == null) continue;
				JSONObject meta = vid.optJSONObject("metadata").orElse(new JSONObject());

				Path videoPath = collectionFolder.resolve(path);
				if(!Files.exists(videoPath)) {
					System.out.println("Ignoring non-existing video at " + videoPath);
					continue;
				}

				collection.addVideo(new Video(collection, id, videoPath, meta));
			}

			collections.add(collection);
		} catch (IOException | JSONParseException | ClassCastException e) {
			throw new LibraryLoadException(e);
		}
	}

	public synchronized VideoCollection createVideoCollection(String collectionID, JSONObject metadata) throws LibraryUpdateException {
		Path collectionFolder = baseFolder.resolve(collectionID);
		int i = 0;
		while(Files.exists(collectionFolder)) {
			collectionFolder = baseFolder.resolve(collectionID + "_" + (i++) + ".mp4");
		}

		Path indexPath = collectionFolder.resolve(VideoCollection.INDEX_FILE_NAME);
		try {
			Files.createDirectories(collectionFolder);
			JSONObject index = new JSONObject();
			index.put("metadata", metadata);
			index.put("videos", new JSONArray());
			Files.writeString(indexPath, index.toFancyString(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new LibraryUpdateException(e);
		}

		VideoCollection collection = new VideoCollection(collectionID, collectionFolder, metadata);
		collections.add(collection);
		return collection;
	}

	public VideoCollection createVideoCollectionFromInfo(VideoCollectionInfo info) throws LibraryUpdateException {
		JSONObject metadata = new JSONObject();
		metadata.put(VideoCollection.META_NAME, info.getName());
		metadata.put(VideoCollection.META_REMOTE_PROVIDER, info.getProvider().getID());
		metadata.put(VideoCollection.META_REMOTE_ID, info.getID());
		metadata.put(VideoCollection.META_THUMBNAIL, null); // TODO: download thumbnail if available
		return createVideoCollection(info.getID(), metadata);
	}

	public Path getBaseFolder() {
		return baseFolder;
	}

	public synchronized List<VideoCollection> getCollections() {
		return new ArrayList<>(collections);
	}

	public synchronized VideoCollection getCollection(String id) {
		return collections.stream()
			.filter(c -> c.getID().equals(id))
			.findFirst().orElse(null);
	}

	public synchronized VideoCollection getCollectionByRemoteID(String providerID, String collectionID) {
		return collections.stream()
			.filter(c -> c.getRemoteProviderID() != null && c.getRemoteProviderID().equals(providerID)
				&& c.getRemoteID() != null && c.getRemoteID().equals(collectionID))
			.findFirst().orElse(null);
	}

	public synchronized List<VideoCollection> filter(String query) {
		if(query == null) return new ArrayList<>(collections);
		return collections
			.stream()
			.filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
			.collect(Collectors.toList());
	}

	public synchronized Video getVideoByRemoteID(String providerID, String collectionID, String videoID) {
		VideoCollection collection = getCollectionByRemoteID(providerID, collectionID);
		if(collection == null) return null;
		return collection.getVideos().stream()
			.filter(v -> v.getRemoteID() != null && v.getRemoteID().equals(videoID))
			.findFirst().orElse(null);
	}

	public synchronized Video addNewVideo(VideoCollection collection, String videoID, Path tempVideoPath, JSONObject metadata) throws LibraryUpdateException {
		try {
			Path collectionPath = collection.getPath();

			Path videoPath = collectionPath.resolve(videoID + ".mp4");
			int i = 0;
			while(Files.exists(videoPath)) {
				videoPath = collectionPath.resolve(videoID + "_" + (i++) + ".mp4");
			}
			Files.move(tempVideoPath, videoPath);

			Path indexPath = collectionPath.resolve(VideoCollection.INDEX_FILE_NAME);
			JSONObject index = new JSONObject(Files.readString(indexPath));
			JSONArray videos = index.optJSONArray("videos").orElse(new JSONArray());
			JSONObject video = new JSONObject();
			video.put("id", videoID);
			video.put("path", collectionPath.relativize(videoPath).toString());
			video.put("metadata", metadata);
			videos.add(video);
			index.put("videos", videos);
			Files.writeString(indexPath, index.toFancyString(), StandardCharsets.UTF_8);
			Video v = new Video(collection, videoID, videoPath, metadata);
			collection.addVideo(v);
			return v;
		} catch (IOException e) {
			throw new LibraryUpdateException(e);
		}
	}

	public synchronized Video addNewVideoFromInfo(VideoCollectionInfo collectionInfo, VideoInfo info, Path tempVideoPath) throws LibraryUpdateException {
		VideoCollection collection = getCollectionByRemoteID(collectionInfo.getProvider().getID(), collectionInfo.getID());
		if(collection == null) collection = createVideoCollectionFromInfo(collectionInfo);

		JSONObject metadata = new JSONObject();
		metadata.put(Video.META_NAME, info.getName());
		metadata.put(Video.META_REMOTE_ID, info.getID());
		metadata.put(Video.META_THUMBNAIL, null); // TODO: download thumbnail if available
		return addNewVideo(collection, info.getID(), tempVideoPath, metadata); // TODO: download thumbnail, create proper metadata
	}

}
