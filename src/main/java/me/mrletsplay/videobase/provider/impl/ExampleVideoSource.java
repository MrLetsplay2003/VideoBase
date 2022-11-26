package me.mrletsplay.videobase.provider.impl;

import java.util.UUID;

import me.mrletsplay.videobase.provider.VideoSource;

public class ExampleVideoSource implements VideoSource {

	private String id;

	public ExampleVideoSource() {
		this.id = UUID.randomUUID().toString();
	}

	@Override
	public String getID() {
		return id;
	}

}
