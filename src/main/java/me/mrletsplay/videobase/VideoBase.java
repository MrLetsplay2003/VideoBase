package me.mrletsplay.videobase;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.mrletsplay.videobase.exception.LibraryLoadException;
import me.mrletsplay.videobase.library.Library;
import me.mrletsplay.videobase.provider.SearchResult;
import me.mrletsplay.videobase.provider.VideoProvider;
import me.mrletsplay.videobase.provider.impl.ExampleVideoProvider;
import me.mrletsplay.videobase.proxy.URLProxy;
import me.mrletsplay.videobase.webinterface.page.HomePage;
import me.mrletsplay.videobase.webinterface.page.LibraryPage;
import me.mrletsplay.videobase.webinterface.page.SearchPage;
import me.mrletsplay.videobase.webinterface.page.VideoCollectionPage;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.page.PageCategory;

public class VideoBase {

	private static Library library;
	private static URLProxy proxy;
	private static List<VideoProvider> providers;

	public static void init(Path libraryPath) throws LibraryLoadException {
		library = new Library(libraryPath);
		providers = new ArrayList<>();
	}

	public static Library getLibrary() {
		return library;
	}

	public static void setProxy(URLProxy proxy) {
		VideoBase.proxy = proxy;
	}

	public static URLProxy getProxy() {
		return proxy;
	}

	public static void addProvider(VideoProvider provider) {
		providers.add(provider);
	}

	public static List<VideoProvider> getProviders() {
		return providers;
	}

	public static List<SearchResult> search(String query) {
		return providers.stream()
			.flatMap(p -> p.findVideoCollections(query).stream())
			.collect(Collectors.toList());
	}

	public static void main(String[] args) throws LibraryLoadException {
		init(Path.of("vids"));
		addProvider(new ExampleVideoProvider());

		DefaultSettings.HOME_PAGE_PATH.defaultValue(HomePage.URL);
		Webinterface.start();

		PageCategory cat = Webinterface.createCategory("VideoBase");
		cat.addPage(new HomePage());
		cat.addPage(new LibraryPage());
		cat.addPage(new SearchPage());
		cat.addPage(new VideoCollectionPage());
	}

}
