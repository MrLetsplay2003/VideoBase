package me.mrletsplay.videobase.webinterface.handler;

import java.util.List;

import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.provider.VideoCollectionInfo;
import me.mrletsplay.videobase.provider.VideoInfo;
import me.mrletsplay.videobase.provider.VideoProvider;
import me.mrletsplay.videobase.provider.VideoSource;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.page.action.ActionEvent;
import me.mrletsplay.webinterfaceapi.page.action.ActionHandler;
import me.mrletsplay.webinterfaceapi.page.action.ActionResponse;
import me.mrletsplay.webinterfaceapi.page.action.WebinterfaceHandler;
import me.mrletsplay.webinterfaceapi.page.element.SettingsPane;

public class VideoBaseHandler implements ActionHandler {

	@WebinterfaceHandler(requestTarget = "videobase", requestTypes = "setSetting")
	public ActionResponse setSetting(ActionEvent event) {
		return SettingsPane.handleSetSettingRequest(Webinterface.getConfig(), event);
	}

	@WebinterfaceHandler(requestTarget = "videobase", requestTypes = "downloadAllVideos")
	public ActionResponse downloadAllVideos(ActionEvent event) {
		String providerID = event.getData().getString("provider");
		String collectionID = event.getData().getString("id");

		VideoProvider provider = VideoBase.getProvider(providerID);
		if(provider == null) return ActionResponse.error("Unknown provider");

		VideoCollectionInfo collectionInfo = provider.getCollectionInfo(collectionID);
		if(collectionInfo == null) return ActionResponse.error("Unknown video collection");

		List<? extends VideoInfo> videos = collectionInfo.loadVideos();
		for(VideoInfo video : videos) {
			List<? extends VideoSource> sources = video.loadSources();
			if(sources.isEmpty()) continue;
			VideoBase.startDownload(collectionInfo, video, sources.get(0));
		}

		return ActionResponse.success();
	}

}
