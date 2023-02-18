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

public class Library {

	private Path baseFolder;
	private List<VideoCollection> collections;

	public Library(Path baseFolder) throws LibraryLoadException {
		this.baseFolder = baseFolder;
		this.collections = new ArrayList<>();
		load();
	}

	private void load() throws LibraryLoadException {
		for(File f : baseFolder.toFile().listFiles()) {
			if(!f.isDirectory()) continue;
			loadCollection(f.toPath());
		}
	}

	private void loadCollection(Path collectionFolder) throws LibraryLoadException {
		Path indexPath = collectionFolder.resolve("index.json");
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

	public Path getBaseFolder() {
		return baseFolder;
	}

	public List<VideoCollection> getCollections() {
		return collections;
	}

	public VideoCollection getCollection(String id) {
		return collections.stream()
			.filter(c -> c.getID().equals(id))
			.findFirst().orElse(null);
	}

	public List<VideoCollection> filter(String query) {
		if(query == null) return new ArrayList<>(collections);
		return collections
			.stream()
			.filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
			.collect(Collectors.toList());
	}

}
