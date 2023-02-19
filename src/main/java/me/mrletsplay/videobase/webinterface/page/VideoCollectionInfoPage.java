package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoProvider;
import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.page.action.SendJSAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Image;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.TitleText;
import me.mrletsplay.webinterfaceapi.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;

public class VideoCollectionInfoPage extends Page {

	public static final String URL = "/videobase/collectioninfo";

	public VideoCollectionInfoPage() {
		super("Video Collection Info", URL);
		setHidden(true);

		PageSection infoSection = new PageSection();
		infoSection.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(infoSection);

		infoSection.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			String providerID = ctx.getRequestedPath().getQuery().getFirst("provider");
			String id = ctx.getRequestedPath().getQuery().getFirst("id");

			if(providerID == null || id == null) return;

			VideoProvider provider = VideoBase.getProvider(providerID);
			if(provider == null) return;

			VideoCollectionInfo info = provider.getCollectionInfo(id);
			if(info == null) return;

			WebinterfaceContext.getCurrentContext().getDocument().setTitle("Download " + info.getName());
			els.add(TitleText.builder()
				.text(info.getName())
				.fullWidth()
				.create());

			els.add(Image.builder()
				.src(info.getThumbnail())
				.style("max-width", "50vw")
				.create());

			els.add(Text.builder()
				.text("Lorem ipsum dolor sit amet")
				.align(Align.TOP_LEFT)
				.create());

			els.add(Button.builder()
				.text("Download all")
				.onClick(SendJSAction.of("videobase", "downloadAllVideos", ActionValue.object()
						.put("provider", ActionValue.string(providerID))
						.put("id", ActionValue.string(id)))
					.onSuccess(RedirectAction.to(DownloadsPage.URL))
				)
				.fullWidth()
				.create());
		});
	}

}
