package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.videobase.webinterface.element.VideoElement;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;

public class VideoCollectionPage extends Page {

	public static final String URL = "/videobase/collection";

	public VideoCollectionPage() {
		super("Collection", URL, true);

		PageSection section = new PageSection();
		section.addTitle(() -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String id = ctx.getRequestedPath().getQuery().getFirst("id");
			VideoCollection col = VideoBase.getLibrary().getCollection(id);
			if(col == null) {
				ctx.redirect(HttpStatusCodes.FOUND_302, "/");
				return "";
			}
			return col.getName();
		});

		section.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String id = ctx.getRequestedPath().getQuery().getFirst("id");
			VideoCollection col = VideoBase.getLibrary().getCollection(id);
			if(col == null) return;

			for(Video v : col.getVideos()) {
				els.add(new VideoElement(v));
			}
		});

		addSection(section);
	}

}
