package me.mrletsplay.videobase;

import me.mrletsplay.webinterfaceapi.config.setting.AutoSetting;
import me.mrletsplay.webinterfaceapi.config.setting.AutoSettings;
import me.mrletsplay.webinterfaceapi.config.setting.SettingsCategory;
import me.mrletsplay.webinterfaceapi.config.setting.impl.BooleanSetting;

public class VideoBaseSettings implements AutoSettings {

	public static final VideoBaseSettings INSTANCE = new VideoBaseSettings();

	@AutoSetting
	private static SettingsCategory
		general = new SettingsCategory("General");

	// General

	public static final BooleanSetting
		GENERATE_VIDEO_THUMBNAILS = general.addBoolean("videobase.thumbnails.generate-video", true, "Generate video thumbnails");

}
