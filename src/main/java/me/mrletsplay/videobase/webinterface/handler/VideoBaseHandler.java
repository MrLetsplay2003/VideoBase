package me.mrletsplay.videobase.webinterface.handler;

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

}
