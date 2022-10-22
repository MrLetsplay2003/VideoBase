package me.mrletsplay.videobase.webinterface.element;

import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.Image;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;

public class VideoCollectionElement extends Group {

	public VideoCollectionElement(VideoCollection collection) {
		setGrid(new Grid().setColumns("1fr").setRows("auto min-content min-content"));
		addLayoutOptions(DefaultLayoutOption.NO_PADDING);

		addElement(Image.builder()
			.src("https://testimages.org/img/testimages_screenshot.jpg")
			.create());

		addElement(Text.builder()
			.text(collection.getName())
			.create());

		addElement(Button.builder()
			.text("View")
			.create());
	}

}
