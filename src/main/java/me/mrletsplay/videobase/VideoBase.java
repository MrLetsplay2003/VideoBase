package me.mrletsplay.videobase;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.mrletsplay.videobase.exception.InitializationException;
import me.mrletsplay.videobase.exception.LibraryLoadException;
import me.mrletsplay.videobase.library.Library;
import me.mrletsplay.videobase.provider.SearchResult;
import me.mrletsplay.videobase.provider.VideoProvider;
import me.mrletsplay.videobase.provider.impl.ExampleVideoProvider;
import me.mrletsplay.videobase.proxy.URLProxy;
import me.mrletsplay.videobase.task.TaskQueue;
import me.mrletsplay.videobase.util.Cache;
import me.mrletsplay.videobase.webinterface.handler.VideoBaseHandler;
import me.mrletsplay.videobase.webinterface.internal.CollectionThumbnailDocument;
import me.mrletsplay.videobase.webinterface.internal.VideoDocument;
import me.mrletsplay.videobase.webinterface.internal.VideoThumbnailDocument;
import me.mrletsplay.videobase.webinterface.page.DownloadsPage;
import me.mrletsplay.videobase.webinterface.page.HomePage;
import me.mrletsplay.videobase.webinterface.page.LibraryPage;
import me.mrletsplay.videobase.webinterface.page.SearchPage;
import me.mrletsplay.videobase.webinterface.page.SettingsPage;
import me.mrletsplay.videobase.webinterface.page.VideoCollectionInfoPage;
import me.mrletsplay.videobase.webinterface.page.VideoCollectionPage;
import me.mrletsplay.videobase.webinterface.page.WatchPage;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.page.PageCategory;

public class VideoBase {

	private static final String
		LIBRARY_FOLDER_NAME = "library",
		TEMP_FOLDER_NAME = "temp";

	private static Library library;
	private static TaskQueue taskQueue;
	private static URLProxy proxy;
	private static Path libraryPath;
	private static Path tempPath;
	private static List<VideoProvider> providers;
	private static Cache<BufferedImage> thumbnailCache;

	public static void init(Path basePath) throws LibraryLoadException {
		libraryPath = basePath.resolve(LIBRARY_FOLDER_NAME);
		tempPath = basePath.resolve(TEMP_FOLDER_NAME);

		try {
			Files.createDirectories(libraryPath);
			Files.createDirectories(tempPath);
		} catch (IOException e) {
			throw new InitializationException(e);
		}

		library = new Library(libraryPath);
		taskQueue = new TaskQueue(10);
		providers = new ArrayList<>();
		thumbnailCache = new Cache<>(1, ChronoUnit.HOURS);

		addProvider(new ExampleVideoProvider());

		DefaultSettings.HOME_PAGE_PATH.defaultValue(HomePage.URL);
		Webinterface.start();

		Webinterface.getConfig().registerSettings(VideoBaseSettings.INSTANCE);
		Webinterface.registerActionHandler(new VideoBaseHandler());

		PageCategory cat = Webinterface.createCategory("VideoBase");
		cat.addPage(new HomePage());
		cat.addPage(new SearchPage());
		cat.addPage(new VideoCollectionInfoPage());
		cat.addPage(new LibraryPage());
		cat.addPage(new DownloadsPage());
		cat.addPage(new VideoCollectionPage());
		cat.addPage(new WatchPage());
		cat.addPage(new SettingsPage());

		Webinterface.getDocumentProvider().registerDocument(VideoDocument.URL, new VideoDocument());
		Webinterface.getDocumentProvider().registerDocument(VideoThumbnailDocument.URL, new VideoThumbnailDocument());
		Webinterface.getDocumentProvider().registerDocument(CollectionThumbnailDocument.URL, new CollectionThumbnailDocument());
	}

	public static Library getLibrary() {
		return library;
	}

	public static TaskQueue getTaskQueue() {
		return taskQueue;
	}

	public static void setProxy(URLProxy proxy) {
		VideoBase.proxy = proxy;
	}

	public static URLProxy getProxy() {
		return proxy;
	}

	public static Path getTemporaryFile() {
		return tempPath.resolve(UUID.randomUUID().toString());
	}

	public static void addProvider(VideoProvider provider) {
		providers.add(provider);
	}

	public static List<VideoProvider> getProviders() {
		return providers;
	}

	public static VideoProvider getProvider(String id) {
		return providers.stream()
			.filter(p -> p.getID().equals(id))
			.findFirst().orElse(null);
	}

	public static List<SearchResult> search(String query) {
		return providers.stream()
			.flatMap(p -> p.findVideoCollections(query).stream())
			.collect(Collectors.toList());
	}

	public static Cache<BufferedImage> getThumbnailCache() {
		return thumbnailCache;
	}

	public static void main(String[] args) throws LibraryLoadException {
		init(Path.of("vids"));
	}

}
