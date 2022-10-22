package me.mrletsplay.videobase;

import java.nio.file.Path;

import me.mrletsplay.videobase.exception.LibraryLoadException;
import me.mrletsplay.videobase.library.Library;

public class VideoBase {

	public static void init() throws LibraryLoadException {
		Library lib = new Library(Path.of("vids"));
		System.out.println(lib.getCollections());
	}

	public static void main(String[] args) throws LibraryLoadException {
		init();
	}

}
