package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class CenterPanel {
	public Widget getCenterPanel() {
		Viewport viewport = new Viewport();
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
		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH,
				150);
		// northData.setMargins(new Margins(5));
		northData.setCollapsible(false);
		northData.setSplit(false);
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 680);
		westData.setCollapsible(false);
		westData.setSplit(false);
		// westData.setMargins(new Margins(0, 5, 0, 5));
		FlexTable ft = new FlexTable();
		ft.setText(0, 0, "MSISDN:");
		ft.setText(0, 1, "State:");
		ft.setText(0, 2, "PolicyID:");
		ft.setWidth("1365px");
		ft.setHeight("10px");
		// ft.setSize("500", "75");
		ft.setBorderWidth(1);
		north.add(getSearchPanel());
		north.add(ft);
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 680);
		// eastData.setMargins(new Margins(0, 5, 0, 5));
		FlexTable polictTable = new FlexTable();

		polictTable.setText(0, 0, "PolicyID:");
		polictTable.setText(1, 0, "Name:");
		polictTable.setText(2, 0, "Policy:");
		polictTable.setText(3, 0, "Description:");
		polictTable.setWidth("679px");
		polictTable.setHeight("360px");
		// ft.setSize("500", "75");
		polictTable.setBorderWidth(1);
		east.add(polictTable);
		eastData.setCollapsible(false);
		eastData.setSplit(false);
		BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH,
				10);
		// southData.setMargins(new Margins(5));
		southData.setCollapsible(false);
		viewport.add(north, northData);
		viewport.add(west, westData);
		viewport.add(east, eastData);
		viewport.add(south, southData);
		// con.setCenterWidget(center, centerData);
		return viewport;
	}

	private static Widget createClientDetails() {
		LayoutContainer ct = new LayoutContainer();
		ct.setLayout(new BorderLayout());
		ContentPanel clientDetailsEast = new ContentPanel();
		ContentPanel clientDetailsWest = new ContentPanel();
		clientDetailsWest.setHeading("Clients");
		BorderLayoutData eastClientData = new BorderLayoutData(
				LayoutRegion.EAST, 500);
		eastClientData.setCollapsible(false);
		eastClientData.setSplit(false);
		BorderLayoutData westClientData = new BorderLayoutData(
				LayoutRegion.WEST, 230);
		westClientData.setCollapsible(false);
		westClientData.setSplit(false);
		ct.setBorders(false);
		ct.add(clientDetailsEast, eastClientData);
		ct.add(clientDetailsWest, westClientData);
		return ct;
	}

	private HorizontalPanel getSearchPanel() {
		HorizontalPanel searchBox = new HorizontalPanel();
		TextField<String> msisdnField = new TextField<String>();
		msisdnField.setFieldLabel("MSISDN");
		msisdnField.setWidth(200);
		msisdnField.setEmptyText("Enter MSISDN");
		Button searchButton = new Button("Search");
		searchBox.setShadow(true);
		searchBox.setSpacing(5);
		searchBox.add(msisdnField);
		searchBox.add(searchButton);
		searchBox.setHorizontalAlign(HorizontalAlignment.CENTER);
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {

			}
		});
		return searchBox;

	}

	/*private void handleSearchEvent() {
		Dispatcher.forwardEvent(AppEvents.search);
	}

	public static void searchResult() {
		LoginServiceAsync service = (LoginServiceAsync) Registry
				.get(Main.SERVICE);
		service.getSearchResults(msisdnText.getValue(),
				new AsyncCallback<Provision>() {
					@Override
					public void onSuccess(Provision result) {
						if (result != null)
							Registry.register("policyID", result.getPolicyid());
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Search Fail...");
					}
				});

	}*/
}
