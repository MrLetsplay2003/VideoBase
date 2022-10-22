package me.mrletsplay.videobase;

import java.nio.file.Path;

import me.mrletsplay.videobase.exception.LibraryLoadException;
import me.mrletsplay.videobase.library.Library;
import me.mrletsplay.videobase.webinterface.page.HomePage;
import me.mrletsplay.videobase.webinterface.page.LibraryPage;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.page.PageCategory;

public class VideoBase {

	private static Library library;

	public static void init() throws LibraryLoadException {
		library = new Library(Path.of("vids"));
	}

	public static Library getLibrary() {
		return library;
	}

	public static void main(String[] args) throws LibraryLoadException {
		init();

		Webinterface.start();

		PageCategory cat = Webinterface.createCategory("VideoBase");
		cat.addPage(new HomePage());
		cat.addPage(new LibraryPage());
	}

}
