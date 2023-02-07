package me.mrletsplay.videobase.webinterface.element;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.webinterface.page.WatchPage;
import me.mrletsplay.webinterfaceapi.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.Image;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;

public class VideoElement extends Group {

	public VideoElement(Video video) {
		setGrid(new Grid().setColumns("1fr"));
		addElement(Image.builder()
			.src("/_internal/thumbnail/video?collection=" + HttpUtils.urlEncode(video.getCollection().getID()) + "&video=" + HttpUtils.urlEncode(video.getID()))
			.create());
		addElement(Button.builder()
			.text(video.getName())
			.onClick(RedirectAction.to(ActionValue.string(WatchPage.URL + "?collection=")
				.plus(ActionValue.string(video.getCollection().getID()).urlEncoded())
				.plus(ActionValue.string("&video="))
				.plus(ActionValue.string(video.getID()).urlEncoded())))
			.create());
	}

}
