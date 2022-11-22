package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;

public class HomePage extends Page {

	public static final String URL = "/videobase/home";

	public HomePage() {
		super("Home", URL);
		setIcon("mdi:home");

		PageSection section = new PageSection();
		section.addTitle("Recently watched");
		addSection(section);
	}

}
