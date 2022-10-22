package me.mrletsplay.videobase.library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
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
		try {
			List<Path> children = Files.walk(baseFolder, 1, FileVisitOption.FOLLOW_LINKS)
				.filter(Files::isDirectory)
				.collect(Collectors.toList());
			for(Path p : children) loadCollection(p);
		} catch (IOException e) {
			throw new LibraryLoadException(e);
		}
	}

	private void loadCollection(Path collectionFolder) throws LibraryLoadException {
		Path indexPath = Path.of(collectionFolder.toString(), "index.json");
		if(!Files.exists(indexPath)) return;
		try {
			JSONObject index = new JSONObject(Files.readString(indexPath, StandardCharsets.UTF_8));

			JSONObject indexMeta = index.optJSONObject("metadata").orElse(new JSONObject());
			JSONArray videos = index.optJSONArray("videos").orElse(null);
			if(videos == null) return;

			List<Video> videosList = new ArrayList<>();
			for(Object video : videos) {
				if(!(video instanceof JSONObject)) continue;
				JSONObject vid = (JSONObject) video;

				JSONObject meta = vid.optJSONObject("metadata").orElse(new JSONObject());
				String path = vid.optString("path").orElse(null);
				if(path == null) continue;

				Path videoPath = Path.of(collectionFolder.toString(), path);
				videosList.add(new Video(videoPath, meta));
			}

			collections.add(new VideoCollection(indexMeta, videosList));
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

	public List<VideoCollection> filter(String query) {
		if(query == null) return new ArrayList<>(collections);
		return collections
			.stream()
			.filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
			.collect(Collectors.toList());
	}

}
