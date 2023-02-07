package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.mrcore.http.HttpUtils;
import me.mrletsplay.simplehttpserver.http.HttpStatusCodes;
import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.Video;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.videobase.webinterface.element.VideoElement;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.element.Image;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;

public class VideoCollectionPage extends Page {

	public static final String URL = "/videobase/collection";

	public VideoCollectionPage() {
		super("Collection", URL, true);

		PageSection info = new PageSection();
		info.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(info);

		info.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String id = ctx.getRequestedPath().getQuery().getFirst("id");
			VideoCollection col = VideoBase.getLibrary().getCollection(id);
			if(col == null) {
				ctx.redirect(HttpStatusCodes.FOUND_302, "/");
				return;
			}

			els.add(TitleText.builder()
				.text(col.getName())
				.fullWidth()
				.create());

			els.add(Image.builder()
				.src("/_internal/thumbnail/collection?collection=" + HttpUtils.urlEncode(id))
				.style("max-width", "50vw")
				.create());

			els.add(Text.builder()
				.text("Lorem ipsum dolor sit amet")
				.align(Align.TOP_LEFT)
				.create());
		});

		PageSection videos = new PageSection();
		videos.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(videos);

		videos.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String id = ctx.getRequestedPath().getQuery().getFirst("id");
			VideoCollection col = VideoBase.getLibrary().getCollection(id);
			if(col == null) return;

			for(Video v : col.getVideos()) {
				els.add(new VideoElement(v));
			}
		});
	}

}
