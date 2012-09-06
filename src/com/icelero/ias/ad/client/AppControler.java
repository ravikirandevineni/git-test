package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

public class AppControler extends Controller {
	private AppView appView;

	public AppControler() {
		registerEventTypes(AppEvents.LOGIN);
		registerEventTypes(AppEvents.LoginSuccess);
		registerEventTypes(AppEvents.search);
	}

	public void initialize() {
		appView = new AppView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.LOGIN) {
			forwardToView(appView, event);
		} else if (type == AppEvents.LoginSuccess) {
			forwardToView(appView, event);
		} else if (type == AppEvents.search) {
			forwardToView(appView, event);
		}
	}

}
