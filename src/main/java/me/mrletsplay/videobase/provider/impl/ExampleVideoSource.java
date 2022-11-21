package me.mrletsplay.videobase.provider.impl;

import me.mrletsplay.videobase.provider.VideoSource;

public class ExampleVideoSource implements VideoSource {

	private String id;

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}

}
