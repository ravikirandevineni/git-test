package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.widget.Composite;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class MainPanel extends Composite {

	public MainPanel() {

		LayoutContainer layoutContainer = new LayoutContainer();
		initComponent(layoutContainer);
		layoutContainer.setBorders(true);

	}

}
