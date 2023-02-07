package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.videobase.VideoBaseSettings;
import me.mrletsplay.webinterfaceapi.DefaultPermissions;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.page.element.SettingsPane;

public class SettingsPage extends me.mrletsplay.webinterfaceapi.page.SettingsPage {

	public static final String URL = "/videobase/settings";

	public SettingsPage() {
		super("Settings", "/sm/settings", DefaultPermissions.SETTINGS, new SettingsPane(
			Webinterface.getConfig(),
			VideoBaseSettings.INSTANCE.getSettingsCategories(),
			"videobase", "setSetting"));

		setIcon("mdi:cog");
	}

}
