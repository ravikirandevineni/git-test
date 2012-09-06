package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;


public class CenterPanelWidget {
	public Widget asWidget() {

		Viewport viewport=new Viewport();
		viewport.setLayout(new BorderLayout());
		ContentPanel north = new ContentPanel();
		ContentPanel west = new ContentPanel();
		ContentPanel center = new ContentPanel();
		center.setHeading("BorderLayout");
		ContentPanel east = new ContentPanel();
		ContentPanel south = new ContentPanel();
		east.setHeading("PolicyDetails");
		west.add(createClientDetails());
		west.setHeading("ClientDetails");
		north.setHeading("AccountDetails");
		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,100);
		// northData.setMargins(new Margins(5));
		northData.setCollapsible(false);
		northData.setSplit(false);
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST,680);
		westData.setCollapsible(false);
		westData.setSplit(false);
		// westData.setMargins(new Margins(0, 5, 0, 5));
		FlexTable ft = new FlexTable();
		ft.setText(0, 0, "MSISDN:");
		ft.setText(0, 1, "STATE:");
		ft.setText(0, 2, "POLICYID:");
		// ft.setSize("500", "75");
		ft.setBorderWidth(0);
		north.add(ft);
			
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST,680);
		// eastData.setMargins(new Margins(0, 5, 0, 5));
		eastData.setCollapsible(false);
		eastData.setSplit(false);
		BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH,10);
		// southData.setMargins(new Margins(5));
		southData.setCollapsible(false);
		viewport.add(north, northData);
		viewport.add(west, westData);
		viewport.add(east, eastData);
		viewport.add(south, southData);
		// con.setCenterWidget(center, centerData);
		return viewport;
	}

	private Widget createClientDetails() {
		Viewport viewport=new Viewport();
		viewport.setLayout(new BorderLayout());
		ContentPanel clientDetailsEast = new ContentPanel();
		ContentPanel clientDetailsWest = new ContentPanel();
		clientDetailsWest.setHeading("Clients");
		BorderLayoutData eastClientData = new BorderLayoutData(LayoutRegion.EAST,500);
		eastClientData.setCollapsible(false);
		eastClientData.setSplit(false);
		BorderLayoutData westClientData = new BorderLayoutData(LayoutRegion.WEST,230);
		westClientData.setCollapsible(false);
		westClientData.setSplit(false);
		viewport.add(clientDetailsEast, eastClientData);
		viewport.add(clientDetailsEast, eastClientData);
		return viewport;
	}

}
