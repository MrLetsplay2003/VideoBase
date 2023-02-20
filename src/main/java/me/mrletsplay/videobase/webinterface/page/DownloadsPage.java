package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.webinterfaceapi.context.WebinterfaceContext;
import me.mrletsplay.webinterfaceapi.js.DefaultJSModule;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.PeriodicAction;
import me.mrletsplay.webinterfaceapi.page.action.UpdateElementAction;
import me.mrletsplay.webinterfaceapi.page.data.DataHandler;
import me.mrletsplay.webinterfaceapi.page.element.Group;
import me.mrletsplay.webinterfaceapi.page.element.Text;
import me.mrletsplay.webinterfaceapi.page.element.layout.DefaultLayoutOption;

public class DownloadsPage extends Page {

	public static final String URL = "/videobase/downloads";

	public DownloadsPage() {
		super("Downloads", URL);
		setIcon("mdi:download");

		PageSection s = new PageSection();
		s.addLayoutOptions(DefaultLayoutOption.FULL_WIDTH);
		addSection(s);
		s.dynamic(els -> {
			WebinterfaceContext.getCurrentContext().requireModule(DefaultJSModule.BASE_ACTIONS);
		});

		Group g = Group.builder()
			.fullWidth()
			.dataHandler(DataHandler.of("videobase", "getTasks", "tasks"))
			.templateElement(Text.builder()
				.template(true)
				.enableMarkdown(true)
				.text("**${name}** (${progress}%)\n${status}")
				.markdownPostProcessor((node, element) -> {
					if(element.getType().equalsIgnoreCase("p")) {
						element.appendAttribute("style", "margin:0;");
					}
					return element;
				})
				.create())
			.create();

		g.getStyle().setProperty("grid-template-columns", "repeat(auto-fill, minmax(265px, min-content))");
		g.getStyle().setProperty("justify-content", "space-between");
		g.getMobileStyle().setProperty("grid-template-columns", "1fr");
		g.getMobileStyle().setProperty("justify-content", "unset");

		s.addElement(g);

		addPeriodicAction(new PeriodicAction(UpdateElementAction.of(g), 500, false));
	}

}
