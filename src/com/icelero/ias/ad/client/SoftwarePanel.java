package com.icelero.ias.ad.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Software;

public class SoftwarePanel extends ContentPanel {

	private ContentPanel softwareDetailsPanel;
	private TabPanel softwareRecordsTab;
	private ContentPanel recentDataPanel;
	private ContentPanel serachPanelTab;
	private ContentPanel searchDataPanel;
	private FormPanel softwareDetailsFormPanel;
	private ContentPanel searchResult;
	private ButtonBar newCloneDeleteButtonbar;
	private ButtonBar saveResetButtonbar;
	private Button saveButton;
	private Button resetButton;
	private Button newButton;
	private Button cloneButton;
	private Button deleteButton;
	private ButtonBar newCloneDeleteSearchButtonbar;
	private TextField<String> searchField;
	private Button archiveButton;
	private TextField<String> url;
	private TextField<String> softwareID;
	private TextArea softwareReleaseNotes;
	private TextField<String> version;
	private DateField releaseDate;
	private CheckBox mandatort;
	private TextField<String> build;

	private Grid<SoftwareID> softwareIDSGrid;
	private Grid<SoftwareID> searchGrid;
	private List<ColumnConfig> configs;
	private ColumnModel columnModel;
	private Button newSearchButton;
	private Button cloneSearchButton;
	private Button deleteSearchButton;

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

	private Widget getSoftwareDetailsPanel() {
		if (softwareDetailsPanel == null) {
			softwareDetailsPanel = new ContentPanel();
			softwareDetailsPanel.setHeaderVisible(false);
			softwareDetailsPanel.add(getSoftwareDetailsFormPanel());
		}
		return softwareDetailsPanel;
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

	private Widget getRecentRecordsPanel() {
		if (recentDataPanel == null) {
			recentDataPanel = new ContentPanel();
			recentDataPanel.setHeight(460);
			recentDataPanel.setHeaderVisible(false);
			recentDataPanel.add(getSoftwareIDSGrid());
			recentDataPanel.add(getNewCloneDeleteButtonbar());
		}
		return recentDataPanel;
	}

	private Widget getSearchPanel() {
		if (serachPanelTab == null) {
			serachPanelTab = new ContentPanel();
			serachPanelTab.setLayout(new BorderLayout());
			serachPanelTab.setHeading("Software IDs");
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
			searchResult.setHeading("Search Results");
			searchResult.setWidth(200);
			searchResult.setBodyBorder(true);
			searchResult.add(getSearchGrid());
			searchResult.add(getSearchButtonbar());
		}
		return searchResult;
	}

	private Grid<SoftwareID> getSearchGrid() {
		if (searchGrid == null) {
			searchGrid = new Grid<SoftwareID>(getSoftwareIDSList(), getColumnModel());
			setSearchGridProperties();
			searchGrid.addListener(Events.OnClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent ce) {
					getSoftwareDetails(searchGrid.getSelectionModel().getSelectedItem().getSid());
				}
			});
		}
		return searchGrid;
	}

	private void setSearchGridProperties() {
		searchGrid.setStyleAttribute("borderTop", "none");
		searchGrid.setAutoExpandColumn("sid");
		searchGrid.setBorders(true);
		searchGrid.setLoadMask(true);
		searchGrid.setStripeRows(true);
		searchGrid.setWidth(595);
		searchGrid.setHeight(300);
	}

	private ButtonBar getSearchButtonbar() {
		if (newCloneDeleteSearchButtonbar == null) {
			newCloneDeleteSearchButtonbar = new ButtonBar();
			newCloneDeleteSearchButtonbar.add(getSearchNewButton());
			newCloneDeleteSearchButtonbar.add(getSearchCloneButton());
			newCloneDeleteSearchButtonbar.add(getSearchDeleteButton());
		}
		return newCloneDeleteSearchButtonbar;
	}

	private Button getSearchNewButton() {
		if (newSearchButton == null) {
			newSearchButton = new Button("New");
			newSearchButton.setWidth(70);

			newSearchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					AddSoftwareDialog addsoftwareDialog = new AddSoftwareDialog();
					addsoftwareDialog.show();
					addsoftwareDialog.center();
					addsoftwareDialog.setClosable(false);
					addsoftwareDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
						public void handleEvent(WindowEvent be) {
							refreshStore();
							getSoftwareIDS();
						}
					});
				}
			});
		}
		return newSearchButton;
	}

	private Button getSearchCloneButton() {
		if (cloneSearchButton == null) {
			cloneSearchButton = new Button("Clone");
			cloneSearchButton.setWidth(70);
			cloneSearchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (searchGrid.getSelectionModel().getSelectedItem() != null) {
						cloneSoftware(searchGrid.getSelectionModel().getSelectedItem().getSid());
					} else {
						MessageBox.alert("Failed to clone", "Please select the software id", null);
					}
				}
			});
		}
		return cloneSearchButton;
	}

	private Button getSearchDeleteButton() {
		if (deleteSearchButton == null) {
			deleteSearchButton = new Button("Delete");
			deleteSearchButton.setWidth(70);
			deleteSearchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// getSearchAndDeleteConformation();
					if (searchGrid.getSelectionModel().getSelectedItem() != null) {
						getSearchAndDeleteConformation();
					} else {
						MessageBox.alert("Failed to clone", "Please select the software id", null);
					}
				}
			});
		}
		return deleteSearchButton;
	}

	private void getSearchAndDeleteConformation() {
		final String softwareID = searchGrid.getSelectionModel().getSelectedItem().getSid();
		MessageBox.confirm("Confirm", "Are you sure to delete Software ID [" + softwareID + "]", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					deleteSearchedSoftwareID(softwareID);
				}
			}
		});
		if (searchGrid.getStore().getCount() == 0) {
			deleteSearchButton.disable();
		}
	}

	private void deleteSearchedSoftwareID(final String softwareID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.deleteSoftwareID(softwareID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					resetFields();
					MessageBox.alert("Success", "Software ID [" + softwareID + "] deleted successfully", null);
					searchGrid.getStore().remove(searchGrid.getSelectionModel().getSelectedItem());
					softwareIDSGrid.getStore().remove(searchGrid.getSelectionModel().getSelectedItem());
					refreshStore();
					getSoftwareIDS();
				} else {
					MessageBox.alert("Error", "Error to delete Software ID [" + softwareID + "]", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Error to delete Software ID [" + softwareID + "]", null);
			}
		});
	}

	private Widget getSearchTabPanel() {
		if (searchDataPanel == null) {
			searchDataPanel = new ContentPanel();
			searchDataPanel.setHeaderVisible(false);
			searchDataPanel.setLayout(new ColumnLayout());
			searchField = new TextField<String>();
			KeyListener keyListener = new KeyListener() {
				public void componentKeyUp(ComponentEvent event) {
					search();
				}
			};
			searchField.addKeyListener(keyListener);
			searchDataPanel.add(searchField, new ColumnData(.6));
			Button searchButton = new Button("Search");
			searchButton.setWidth(70);
			searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					search();
				}
			});
			searchDataPanel.add(searchButton);
		}
		return searchDataPanel;
	}

	protected void search() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		final String softwareID = searchField.getValue();
		service.searchSoftwareID(softwareID, new AsyncCallback<List<Software>>() {
			@Override
			public void onSuccess(List<Software> result) {
				if (result.size() > 0) {
					searchGrid.getStore().removeAll();
					for (Software sid : result) {
						SoftwareID sID = new SoftwareID(sid.getSoftwareID(), sid.getVersion());
						searchGrid.getStore().add(sID);
					}
				} else {
					resetAccountDetails();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				resetAccountDetails();
			}
		});
	}

	private void resetAccountDetails() {
		searchGrid.getStore().removeAll();
	}

	private Grid<SoftwareID> getSoftwareIDSGrid() {
		if (softwareIDSGrid == null) {
			softwareIDSGrid = new Grid<SoftwareID>(getSoftwareIDSList(), getColumnModel());
			setGridProperties();
			softwareIDSGrid.addListener(Events.OnClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent ce) {
					getSoftwareDetails(softwareIDSGrid.getSelectionModel().getSelectedItem().getSid());
				}
			});
		}
		return softwareIDSGrid;
	}

	private ColumnModel getColumnModel() {
		configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("sid");
		column.setWidth(290);
		column.setHeader("Software ID");
		configs.add(column);
		column = new ColumnConfig();
		column.setId("sversion");
		column.setWidth(300);
		column.setHeader("Software Version");
		configs.add(column);
		columnModel = new ColumnModel(configs);
		return columnModel;
	}

	private ListStore<SoftwareID> getSoftwareIDSList() {
		List<SoftwareID> softwareIDs = new ArrayList<SoftwareID>();
		ListStore<SoftwareID> softwareIDSList = new ListStore<SoftwareID>();
		softwareIDSList.add(softwareIDs);
		return softwareIDSList;
	}

	private void setGridProperties() {
		softwareIDSGrid.setBorders(true);
		softwareIDSGrid.setLoadMask(true);
		softwareIDSGrid.setStripeRows(true);
		softwareIDSGrid.setAutoWidth(false);
		softwareIDSGrid.setHeight(390);
		softwareIDSGrid.setWidth(600);
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
			softwareReleaseNotes.setValue(result.getNotes());
			releaseDate.setValue(result.getReleaseDate());
			url.setValue(result.getUrl());
			build.setValue(result.getBuild());
			if (result.getMandatory().equalsIgnoreCase("y")) {
				mandatort.setValue(true);
			} else {
				mandatort.setValue(false);
			}
		}
	}

	private FormPanel getSoftwareDetailsFormPanel() {
		if (softwareDetailsFormPanel == null) {
			softwareDetailsFormPanel = new FormPanel();
			softwareDetailsFormPanel.setBorders(false);
			softwareDetailsFormPanel.setBodyBorder(disabled);
			softwareDetailsFormPanel.setHeading("Software Details");
			softwareDetailsFormPanel.setButtonAlign(HorizontalAlignment.CENTER);
			softwareDetailsFormPanel.setLabelWidth(120);
			softwareDetailsFormPanel.add(getSoftwareIDField(), new FormData("100%"));
			softwareDetailsFormPanel.add(getVersionField(), new FormData("100%"));
			softwareDetailsFormPanel.add(getBuild(), new FormData("100%"));
			softwareDetailsFormPanel.add(getMandatoryField(), new FormData("20%"));
			softwareDetailsFormPanel.add(getURL(), new FormData("100%"));
			softwareDetailsFormPanel.add(getReleaseDateField(), new FormData("100%"));
			softwareDetailsFormPanel.add(getReleaseNotesField(), new FormData("100%"));
			softwareDetailsFormPanel.add(saveResetButtonbar());
		}
		return softwareDetailsFormPanel;
	}

	private TextField<String> getVersionField() {
		if (version == null) {
			version = new TextField<String>();
			version.setFieldLabel("Version");
		}
		return version;
	}

	private CheckBox getMandatoryField() {
		if (mandatort == null) {
			mandatort = new CheckBox();
			mandatort.setFieldLabel("Mandatory");
		}
		return mandatort;
	}

	private DateField getReleaseDateField() {
		if (releaseDate == null) {
			releaseDate = new DateField();
			DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy");
			releaseDate.getPropertyEditor().setFormat(dtf);
			releaseDate.setFieldLabel("Release Date");
			releaseDate.setEditable(false);

		}
		return releaseDate;
	}

	private TextField<String> getReleaseNotesField() {
		if (softwareReleaseNotes == null) {
			softwareReleaseNotes = new TextArea();
			softwareReleaseNotes.setFieldLabel("Release Notes");
			softwareReleaseNotes.setSize(75, 75);
		}
		return softwareReleaseNotes;
	}

	private TextField<String> getURL() {
		if (url == null) {
			url = new TextField<String>();
			url.setFieldLabel("URL");

		}
		return url;
	}

	private TextField<String> getSoftwareIDField() {
		if (softwareID == null) {
			softwareID = new TextField<String>();
			softwareID.setFieldLabel("Software ID");
			softwareID.setEnabled(false);
		}
		return softwareID;
	}

	private TextField<String> getBuild() {
		if (build == null) {
			build = new TextField<String>();
			build.setFieldLabel("Build");
		}
		return build;
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
			saveButton.setWidth(70);
			saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (softwareID.getValue() != null) {

						if (isValidFormData()) {
							updateSoftwareDetails();
						}
					} else {
						MessageBox.alert("Failed", "Please select the valid Software ID", null);
					}
				}
			});
		}
		return saveButton;
	}

	private void updateSoftwareDetails() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		Software softwareDetails = new Software();
		softwareDetails.setSoftwareID(softwareID.getValue());
		softwareDetails.setNotes(softwareReleaseNotes.getValue());
		softwareDetails.setVersion(version.getValue());
		softwareDetails.setReleaseDate(releaseDate.getValue());
		softwareDetails.setBuild(build.getValue());
		softwareDetails.setReleaseDate(releaseDate.getValue());
		softwareDetails.setUrl(url.getValue());
		if (mandatort.getValue() == true) {
			softwareDetails.setMandatory("y");
		} else {
			softwareDetails.setMandatory("n");
		}
		service.updateSoftwareDetails(softwareDetails, Registry.get("loggedUser").toString(), new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				MessageBox.alert("Success", "Software details updated successfully", null);
				refreshStore();
				getSoftwareIDS();
				getSoftwareDetails(softwareID.getValue());
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Failed", "Please update again", null);
			}
		});

	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
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
			newCloneDeleteButtonbar.add(getArchiveButton());

		}
		return newCloneDeleteButtonbar;
	}

	private Button getArchiveButton() {
		if (archiveButton == null) {
			archiveButton = new Button("Archive");
			archiveButton.setWidth(70);
			archiveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {

					archive();

				}
			});
		}
		return archiveButton;
	}

	private void archive() {

		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.archiveSoftware(softwareIDSGrid.getSelectionModel().getSelectedItem().getSid(), new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					MessageBox.alert("Success", "Software Archived successfully", null);
					resetFields();
					softwareIDSGrid.getStore().remove(softwareIDSGrid.getSelectionModel().getSelectedItem());
				} else {
					MessageBox.alert("Faild", "Software Archived Faild", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Software Archived Faild", null);
			}
		});
	}

	private Button getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new Button("Delete");
			deleteButton.setWidth(70);
			deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// deleteSoftwareIDConformation();
					if (softwareIDSGrid.getSelectionModel().getSelectedItem() != null) {
						deleteSoftwareIDConformation();
					} else {
						MessageBox.alert("Failed to clone", "Please select the software id", null);
					}
				}
			});
		}
		return deleteButton;
	}

	private void deleteSoftwareIDConformation() {
		final String softwareID = softwareIDSGrid.getSelectionModel().getSelectedItem().getSid();
		MessageBox.confirm("Confirm", "Are you sure to delete Software ID [" + softwareID + "]", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					deleteSoftwareID(softwareID);
				}
			}
		});
		if (softwareIDSGrid.getStore().getCount() == 0) {
			deleteButton.disable();
		}
	}

	private void deleteSoftwareID(final String softwareID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.deleteSoftwareID(softwareID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					resetFields();
					MessageBox.alert("Success", "Software ID [" + softwareID + "] deleted successfully", null);
					softwareIDSGrid.getStore().remove(softwareIDSGrid.getSelectionModel().getSelectedItem());
				} else {
					MessageBox.alert("Error", "Error to delete Software ID [" + softwareID + "]", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Error to delete Software ID [" + softwareID + "]", null);
			}
		});
	}

	private Button getCloneButton() {

		if (cloneButton == null) {
			cloneButton = new Button("Clone");
			cloneButton.setWidth(70);
			cloneButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (softwareIDSGrid.getSelectionModel().getSelectedItem() != null) {
						cloneSoftware(softwareIDSGrid.getSelectionModel().getSelectedItem().getSid());
					} else {
						MessageBox.alert("Failed to clone", "Please select the software id", null);
					}
				}
			});
		}

		return cloneButton;
	}

	private void cloneSoftware(String softwareID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getSoftwareDetails(softwareID, new AsyncCallback<Software>() {

			@Override
			public void onSuccess(Software result) {

				CloneSoftwareDialog cloneSoftwareDialog = new CloneSoftwareDialog(result);
				cloneSoftwareDialog.show();
				cloneSoftwareDialog.center();
				cloneSoftwareDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
					public void handleEvent(WindowEvent be) {
						refreshStore();
						getSoftwareIDS();
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	private Component getNewButton() {
		if (newButton == null) {
			newButton = new Button("New");
			newButton.setWidth(70);
			newButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					AddSoftwareDialog addsoftwareDialog = new AddSoftwareDialog();
					addsoftwareDialog.show();
					addsoftwareDialog.center();
					addsoftwareDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
						public void handleEvent(WindowEvent be) {
							refreshStore();
							getSoftwareIDS();
						}
					});
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
						SoftwareID si = new SoftwareID(softwareID.getSoftwareID(), softwareID.getVersion());
						softwareIDs.add(si);
						softwareIDSGrid.getStore().add(si);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MessageBox.alert("Load Data Failed", "Failed to load data from datbase", null);
			}
		});
	}

	private void refreshStore() {
		softwareIDSGrid.getStore().removeAll();
	}

	private void resetFields() {
		softwareID.setValue("");
		version.setValue("");
		softwareReleaseNotes.setValue("");
		build.setValue("");
		releaseDate.setValue(null);
		mandatort.setValue(false);
		searchField.clear();
		searchGrid.getStore().removeAll();
	}

	private boolean isValidFormData() {
		if (softwareReleaseNotes.getValue() != null && softwareReleaseNotes.getValue().trim().length() > 0 && version.getValue() != null
				&& version.getValue().trim().length() > 0 && build.getValue() != null && build.getValue().trim().length() > 0
				&& releaseDate.getValue() != null) {
			return true;
		}
		MessageBox.alert("Failed to login", "All fields are mandatory", null);
		return false;
	}
}
