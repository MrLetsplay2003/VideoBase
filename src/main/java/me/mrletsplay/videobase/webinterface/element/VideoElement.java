package me.mrletsplay.videobase.webinterface.element;

import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Group;

public class VideoElement extends Group {

	public VideoElement(Video video) {
		addElement(Button.builder()
			.text(video.getName())
			.create());
	}

}
