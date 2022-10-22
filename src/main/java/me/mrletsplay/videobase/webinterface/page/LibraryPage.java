package me.mrletsplay.videobase.webinterface.page;

import java.util.List;

import me.mrletsplay.simplehttpserver.http.request.HttpRequestContext;
import me.mrletsplay.simplehttpserver.http.request.urlencoded.URLEncoded;
import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.library.VideoCollection;
import me.mrletsplay.videobase.webinterface.element.VideoCollectionElement;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.action.RedirectAction;
import me.mrletsplay.webinterfaceapi.page.action.value.ActionValue;
import me.mrletsplay.webinterfaceapi.page.element.Button;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.HorizontalSpacer;
import me.mrletsplay.webinterfaceapi.page.element.InputField;
import me.mrletsplay.webinterfaceapi.page.element.builder.Align;
import me.mrletsplay.webinterfaceapi.page.element.layout.Grid;

public class LibraryPage extends Page {

	public static final String URL = "/videobase/library";

	public static final int PAGE_SIZE = 30;

	public LibraryPage() {
		super("Library", URL);
		setIcon("mdi:bookmark-multiple");

		PageSection s = new PageSection();
		addSection(s);

		s.addTitle("Library");

		s.dynamic(els -> {
			HttpRequestContext ctx = HttpRequestContext.getCurrentContext();
			URLEncoded query = ctx.getClientHeader().getPath().getQuery();

			String q = query.getFirst("q");
			els.add(InputField.builder()
				.fullWidth()
				.placeholder("Query")
				.initialValue(q)
				.onChange(in -> RedirectAction.to(ActionValue.string(URL + "?q=").plus(() -> "encodeURI(" + ActionValue.elementValue(in).toJavaScript() +")")))
				.create());

			List<VideoCollection> filtered = VideoBase.getLibrary().filter(q);
			int numPages = (filtered.size() + PAGE_SIZE - 1) / PAGE_SIZE;

			int p;
			try {
				p = Integer.parseInt(query.getFirst("p", "0"));
				if(p < 0) p = 0;
				if(p > numPages - 1) p = numPages - 1;
			}catch(NumberFormatException e) {
				p = 0;
			}

			Group buttonGroup = Group.builder()
				.fullWidth()
				.noPadding()
				.align(Align.CENTER)
				.grid(new Grid().setColumns("1fr max-content max-content max-content max-content max-content max-content max-content 1fr"))
				.create();

			buttonGroup.addElement(new HorizontalSpacer("0"));
			buttonGroup.addElement(Button.builder()
					.icon("mdi:chevron-left")
					.disabled(p == 0)
					.create());
			for(int i = 0; i < 5; i++) {
				int pg = p + i - 2;
				if(pg < 0 || pg >= numPages) {
					buttonGroup.addElement(Button.builder().text(pg < 0 ? "" : "" + (pg + 1)).disabled(true).create());
					continue;
				}
				buttonGroup.addElement(Button.builder()
					.text("" + (pg + 1))
					.create());
			}
			buttonGroup.addElement(Button.builder()
				.icon("mdi:chevron-right")
				.disabled(p == numPages - 1)
				.create());
			buttonGroup.addElement(new HorizontalSpacer("0"));

			els.add(buttonGroup);

			filtered.stream().skip(p * PAGE_SIZE).limit(PAGE_SIZE).forEach(col -> {
				els.add(new VideoCollectionElement(col));
			});

			els.add(buttonGroup);
		});
	}

}
