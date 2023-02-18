package me.mrletsplay.videobase.webinterface.page;

import me.mrletsplay.videobase.VideoBase;
import me.mrletsplay.videobase.task.Task;
import me.mrletsplay.webinterfaceapi.page.Page;
import me.mrletsplay.webinterfaceapi.page.PageSection;
import me.mrletsplay.webinterfaceapi.page.element.Text;

public class DownloadsPage extends Page {

	public static final String URL = "/videobase/downloads";

	public DownloadsPage() {
		super("Downloads", URL);
		setIcon("mdi:download");

		PageSection s = new PageSection();
		s.dynamic(els -> {
			for(Task t : VideoBase.getTaskQueue().getTasks()) {
				els.add(Text.builder()
					.text(t.getName())
					.fullWidth()
					.create());
			}
		});
	}

}
