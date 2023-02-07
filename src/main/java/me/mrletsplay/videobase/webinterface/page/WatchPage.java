package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.videobase.webinterface.element.VideoPlayerElement;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;

public class WatchPage extends Page {

	public static final String URL = "/videobase/watch";

	public WatchPage() {
		super("Watch", URL, true);

		PageSection s = new PageSection();
		addSection(s);

		s.setGrid(new Grid().setColumns("1fr"));
		s.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			URLEncoded query = ctx.getRequestedPath().getQuery();
			String collectionID = query.getFirst("collection");
			String videoID = query.getFirst("video");
			if(collectionID == null || videoID == null) return;

			VideoCollection collection = VideoBase.getLibrary().getCollection(collectionID);
			if(collection == null) return;

			Video video = collection.getVideo(videoID);
			if(video == null) return;

			els.add(new TitleText("Watch " + video.getName()));
			els.add(new VideoPlayerElement(video));
		});
	}

}
