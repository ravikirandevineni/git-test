package in.sample.app.client;

import in.sample.app.shared.Software;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SoftwarePanel extends ContentPanel {

	private FormPanel softwareDetailsPanel;
	private TabPanel softwareRecordsTab;
	private ContentPanel recentDataPanel;
	private ContentPanel serachPanelTab;
	private ContentPanel searchDataPanel;
	private ContentPanel searchResult;
	private ButtonBar newCloneDeleteButtonbar;
	private ButtonBar saveResetButtonbar;
	private Button saveButton;
	private Button resetButton;
	private Button newButton;
	private Button cloneButton;
	private Button deleteButton;

	TextField<String> softwareID;
	TextField<String> softwareDescription;
	TextField<String> version;

	Grid<SoftwareID> grid;
	List<ColumnConfig> configs;
	ColumnModel cm;

	public SoftwarePanel() {
		initialize();
	}

	private void initialize() {

		setLayout(new BorderLayout());
		setHeight(520);
		getSoftwareIDS();
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 720);
		eastData.setMargins(new Margins(5));
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 600);
		westData.setMargins(new Margins(5));
		add(getSoftwareDetailsPanel(), eastData);
		add(getSoftwareRecordDetailsPanel(), westData);
	}

	private Widget getSoftwareRecordDetailsPanel() {
		if (softwareRecordsTab == null) {
			softwareRecordsTab = new TabPanel();
			TabItem recent = new TabItem("Recent");
			TabItem search = new TabItem("Search");
			softwareRecordsTab.add(recent);
			softwareRecordsTab.add(search);

			softwareRecordsTab.addListener(Events.Select, new Listener<TabPanelEvent>() {
				public void handleEvent(TabPanelEvent be) {
					resetFields();
				}
			});

			recent.add(getRecentRecordsPanel());
			search.add(getSearchPanel());
		}
		return softwareRecordsTab;
	}

	private void resetFields() {

		softwareID.setValue("");
		version.setValue("");
		softwareDescription.setValue("");

	}

	private Widget getSearchPanel() {
		if (serachPanelTab == null) {
			serachPanelTab = new ContentPanel();
			serachPanelTab.setLayout(new BorderLayout());
			serachPanelTab.setHeading("SoftwareID's");
			serachPanelTab.setHeight(460);
			BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 60);
			BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
			serachPanelTab.add(getSearchTabPanel(), northData);
			serachPanelTab.add(getSearchRecords(), centerData);
		}
		return serachPanelTab;
	}

	private Widget getSearchRecords() {
		if (searchResult == null) {
			searchResult = new ContentPanel();
			searchResult.setLayout(new RowLayout());
			searchResult.setHeading("SearchResults");
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			// searchResult.add(getNewCloneDeleteButtonbar());
		}
		return searchResult;
	}

	private Widget getSearchTabPanel() {
		if (searchDataPanel == null) {
			searchDataPanel = new ContentPanel();
			searchDataPanel.setHeaderVisible(false);
			searchDataPanel.setLayout(new ColumnLayout());
			searchDataPanel.add(new TextField<String>(), new ColumnData(.6));
			searchDataPanel.add(new Button("Search"), new ColumnData(.1));
		}
		return searchDataPanel;
	}

	private Widget getRecentRecordsPanel() {
		if (recentDataPanel == null) {
			recentDataPanel = new ContentPanel();
			recentDataPanel.setHeight(460);
			recentDataPanel.setHeading("SoftwareID's");
			// recentDataPanel.setLayout(new RowLayout());
			configs = new ArrayList<ColumnConfig>();
			ColumnConfig column = new ColumnConfig();
			column.setId("sid");
			column.setWidth(200);
			configs.add(column);
			cm = new ColumnModel(configs);
			List<SoftwareID> softwareIDs = new ArrayList<SoftwareID>();
			ListStore<SoftwareID> clientList = new ListStore<SoftwareID>();
			clientList.add(softwareIDs);
			grid = new Grid<SoftwareID>(clientList, cm);
			grid.setStyleAttribute("borderTop", "none");
			grid.setAutoExpandColumn("sid");
			grid.setBorders(true);
			grid.setLoadMask(true);
			grid.setStripeRows(true);

			grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<SoftwareID>() {

				@Override
				public void selectionChanged(SelectionChangedEvent<SoftwareID> se) {
					// Window.alert(""+grid.getSelectionModel().getSelectedItem().getSid());
					getSoftwareDetails(grid.getSelectionModel().getSelectedItem().getSid());
				}
			});
			recentDataPanel.add(grid);
			recentDataPanel.add(getNewCloneDeleteButtonbar());
		}
		return recentDataPanel;
	}

	private void getSoftwareDetails(String selectedSoftwareID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getSoftwareDetails(selectedSoftwareID, new AsyncCallback<Software>() {
			@Override
			public void onSuccess(Software result) {
				Registry.register("softwareDetails", result);
				setSoftwareDetalisValues();
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	private void setSoftwareDetalisValues() {
		if (Registry.get("softwareDetails") != null) {
			Software result = (Software) Registry.get("softwareDetails");
			softwareID.setValue(result.getSoftwareID());
			version.setValue(result.getVersion());
			softwareDescription.setValue(result.getNotes());
		}
	}

	private Widget getSoftwareDetailsPanel() {
		if (softwareDetailsPanel == null) {
			softwareDetailsPanel = new FormPanel();
			softwareDetailsPanel.setHeading("SoftwareDetails");
			softwareDetailsPanel.setButtonAlign(HorizontalAlignment.CENTER);
			softwareID = new TextField<String>();
			softwareID.setFieldLabel("ID");
			softwareDescription = new TextField<String>();
			softwareDescription.setFieldLabel("Description");
			TextField<String> createDate = new TextField<String>();
			createDate.setFieldLabel("CreatedDate");
			TextField<String> modifiedDate = new TextField<String>();
			modifiedDate.setFieldLabel("ModifiedDate");
			version = new TextField<String>();
			version.setFieldLabel("Version");
			softwareDetailsPanel.add(softwareID, new FormData("100%"));
			softwareDetailsPanel.add(softwareDescription, new FormData("100%"));
			softwareDetailsPanel.add(createDate, new FormData("100%"));
			softwareDetailsPanel.add(modifiedDate, new FormData("100%"));
			softwareDetailsPanel.add(version, new FormData("100%"));
			softwareDetailsPanel.add(saveResetButtonbar());
		}
		return softwareDetailsPanel;
	}

	private ButtonBar saveResetButtonbar() {
		if (saveResetButtonbar == null) {
			saveResetButtonbar = new ButtonBar();
			saveResetButtonbar.setAlignment(HorizontalAlignment.CENTER);
			saveResetButtonbar.add(getSaveButton());
			saveResetButtonbar.add(getResetButton());
		}
		return saveResetButtonbar;
	}

	private Button getSaveButton() {

		if (saveButton == null) {
			saveButton = new Button("Save");
			saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {

					updateSoftwareDetails();
				}
			});

		}
		return saveButton;
	}

	private void updateSoftwareDetails() {

		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		Software softwareDetails=new Software();
		softwareDetails.setSoftwareID(softwareID.getValue());
		softwareDetails.setNotes(softwareDescription.getValue());
		softwareDetails.setVersion(version.getValue());
		
		service.updateSoftwareDetails(softwareDetails, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				
				Window.alert("Success");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				Window.alert("Faild to update");
				
			}
		});
		
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setSoftwareDetalisValues();
				}
			});
		}
		return resetButton;
	}

	private ButtonBar getNewCloneDeleteButtonbar() {
		if (newCloneDeleteButtonbar == null) {
			newCloneDeleteButtonbar = new ButtonBar();
			newCloneDeleteButtonbar.add(getNewButton());
			newCloneDeleteButtonbar.add(getCloneButton());
			newCloneDeleteButtonbar.add(getDeleteButton());

		}
		return newCloneDeleteButtonbar;
	}

	private Button getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new Button("Delete");
		}

		return deleteButton;
	}

	private Button getCloneButton() {

		if (cloneButton == null) {
			cloneButton = new Button("Clone");
			cloneButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getSelectionModel().getSelectedItem() != null) {
						cloneSoftware();
					} else {
						MessageBox.alert("Failed to clone", "Please select the clientID", null);
					}
				}
			});
		}

		return cloneButton;
	}

	private void cloneSoftware() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		String softwareID = grid.getSelectionModel().getSelectedItem().getSid();
		service.getSoftwareDetails(softwareID, new AsyncCallback<Software>() {

			@Override
			public void onSuccess(Software result) {

				CloneSoftwareDialog cloneSoftware = new CloneSoftwareDialog(result);
				cloneSoftware.show();
				cloneSoftware.center();

			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	private Component getNewButton() {
		if (newButton == null) {
			newButton = new Button("New");
			newButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					AddSoftwareDialog addsoftwareDialog = new AddSoftwareDialog();
					addsoftwareDialog.show();
					addsoftwareDialog.center();

				}
			});
		}

		return newButton;
	}

	private void getSoftwareIDS() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getSoftwareIDS(new AsyncCallback<List<Software>>() {
			@Override
			public void onSuccess(List<Software> result) {
				if (result != null) {
					List<SoftwareID> softwareIDs = new ArrayList<SoftwareID>();
					for (Software softwareID : result) {
						SoftwareID si = new SoftwareID(softwareID.getSoftwareID());
						softwareIDs.add(si);
						grid.getStore().add(si);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Faild to Load");
			}
		});

	}

}
