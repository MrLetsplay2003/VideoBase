package me.mrletsplay.videobase.webinterface.element;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.simplehttpserver.dom.html.HtmlElement;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.webinterface.internal.VideoDocument;
import me.mrletsplay.webinterfaceapi.page.element.AbstractPageElement;

public class VideoPlayerElement extends AbstractPageElement {

	private Video video;

	public VideoPlayerElement(Video video) {
		this.video = video;
	}

	@Override
	public HtmlElement createElement() {
		HtmlElement player = new HtmlElement("video");
		player.setAttribute("autoplay");
		player.setAttribute("controls");
//		player.setAttribute("poster", "");
		player.setAttribute("preload", "auto");
		player.setAttribute("width", "1920");
		player.setAttribute("height", "1080");
		player.setAttribute("style", "width:100%;height:auto;max-width:1280px;max-height:720px;");

		HtmlElement source = new HtmlElement("source");
		source.setAttribute("src", VideoDocument.URL + "?collection=" + HttpUtils.urlEncode(video.getCollection().getID()) + "&video=" + HttpUtils.urlEncode(video.getID()));
		source.setAttribute("type", "video/mp4");
		player.appendChild(source);

		return player;
	}

}
