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
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.utilities.PrefetchContentTypeUtilities;
import com.icelero.ias.ad.shared.utilities.TimeUtilities;

public class PolicyPanel extends ContentPanel {

	private ContentPanel PolicyDetailsPanel;
	private TabPanel PolicyRecordsTab;
	private ContentPanel recentDataPanel;
	private ContentPanel searchPanelTab;
	private ContentPanel searchDataPanel;
	private ContentPanel searchResult;
	private ButtonBar newCloneDeleteButtonbar;
	private ButtonBar saveResetButtonbar;
	private Button saveButton;
	private Button resetButton;
	private Button newButton;
	private Button cloneButton;
	private Button deleteButton;
	private Button archiveButton;
	private Button newSearchButton;
	private Button cloneSearchButton;
	private Button deleteSearchButton;
	private Button searchArchiveButton;
	private ButtonBar newCloneDeleteSearchButtonbar;
	private TextField<String> policyID;
	private TextField<String> policyDescription;
	private TextField<String> name;
	private TextField<String> searchField;
	private SimpleComboBox<String> mediaServerStartHH;
	private SimpleComboBox<String> mediaServerStartMM;
	private SimpleComboBox<String> mediaServerStopHH;
	private SimpleComboBox<String> mediaServerStopMM;
	private SimpleComboBox<String> prefetchStartHH;
	private SimpleComboBox<String> prefetchStartMM;
	private SimpleComboBox<String> prefetchStopMM;
	private SimpleComboBox<String> prefetchStopHH;
	private SimpleComboBox<String> prefetch;
	private NumberField bandwidth;

	Grid<PolicyID> policyGrid;
	Grid<PolicyID> searchGrid;
	List<ColumnConfig> configs;
	ColumnModel columnModel;

	public PolicyPanel() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setHeight(520);
		getPolicyIDS();
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 720);
		eastData.setMargins(new Margins(5));
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 600);
		westData.setMargins(new Margins(5));
		add(getPolicyDetailsPanel(), eastData);
		add(getPolicyRecordDetailsPanel(), westData);
	}

	private Widget getPolicyRecordDetailsPanel() {
		if (PolicyRecordsTab == null) {
			PolicyRecordsTab = new TabPanel();
			TabItem recent = new TabItem("Recent");
			TabItem search = new TabItem("Search");
			PolicyRecordsTab.add(recent);
			PolicyRecordsTab.add(search);
			PolicyRecordsTab.addListener(Events.Select, new Listener<TabPanelEvent>() {
				public void handleEvent(TabPanelEvent be) {
					resetFields();
				}
			});
			recent.add(getRecentRecordsPanel());
			search.add(getSearchPanel());
		}
		return PolicyRecordsTab;
	}

	private Widget getSearchPanel() {
		if (searchPanelTab == null) {
			searchPanelTab = new ContentPanel();
			searchPanelTab.setLayout(new BorderLayout());
			searchPanelTab.setHeading("Policy IDs");
			searchPanelTab.setHeight(460);
			BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 60);
			BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
			searchPanelTab.add(getSearchTabPanel(), northData);
			searchPanelTab.add(getSearchRecords(), centerData);
		}
		return searchPanelTab;
	}

	private Widget getSearchRecords() {
		if (searchResult == null) {
			searchResult = new ContentPanel();
			searchResult.setHeading("SearchResults");
			searchResult.add(getSearchResultsGrid());
			searchResult.add(getSearchButtonbar());
		}
		return searchResult;
	}

	private Grid<PolicyID> getSearchResultsGrid() {
		if (searchGrid == null) {
			searchGrid = new Grid<PolicyID>(getColumnModel(), columnModel);
			setSearchGridProperties();
			searchGrid.addListener(Events.OnClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent ce) {
					getPolicyDetails(searchGrid.getSelectionModel().getSelectedItem().getPid());
				}
			});
		}
		return searchGrid;
	}

	private void setSearchGridProperties() {
		searchGrid.setStyleAttribute("borderTop", "none");
		searchGrid.setAutoExpandColumn("pid");
		searchGrid.setBorders(true);
		searchGrid.setLoadMask(true);
		searchGrid.setStripeRows(true);
		searchGrid.setWidth(600);
		searchGrid.setHeight(300);
	}

	private ListStore<PolicyID> getColumnModel() {
		configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("pid");
		column.setWidth(290);
		column.setHeader("Polict ID");
		configs.add(column);
		column = new ColumnConfig();
		column.setId("pname");
		column.setWidth(300);
		column.setHeader("Policy Name");
		configs.add(column);
		columnModel = new ColumnModel(configs);
		List<PolicyID> softwareIDs = new ArrayList<PolicyID>();
		ListStore<PolicyID> softwareList = new ListStore<PolicyID>();
		softwareList.add(softwareIDs);
		return softwareList;
	}

	private ButtonBar getSearchButtonbar() {
		if (newCloneDeleteSearchButtonbar == null) {
			newCloneDeleteSearchButtonbar = new ButtonBar();
			newCloneDeleteSearchButtonbar.add(getSearchNewButton());
			newCloneDeleteSearchButtonbar.add(getSearchCloneButton());
			newCloneDeleteSearchButtonbar.add(getSearchDeleteButton());
			newCloneDeleteSearchButtonbar.add(getSearchArchiveButton());
			saveResetButtonbar.setAlignment(HorizontalAlignment.CENTER);
		}
		return newCloneDeleteSearchButtonbar;
	}

	private Button getSearchArchiveButton() {
		if (searchArchiveButton == null) {
			searchArchiveButton = new Button("Archive");
			searchArchiveButton.setWidth(70);
		}
		return searchArchiveButton;
	}

	private Button getSearchNewButton() {
		if (newSearchButton == null) {
			newSearchButton = new Button("New");
			newSearchButton.setWidth(70);
			newSearchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					AddPolicyDialog addPolicyDialog = new AddPolicyDialog();
					addPolicyDialog.show();
					addPolicyDialog.center();
					addPolicyDialog.setClosable(false);
					addPolicyDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
						public void handleEvent(WindowEvent be) {
							refreshStore();
							getPolicyIDS();
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
						clonePolicy(searchGrid.getSelectionModel().getSelectedItem().getPid());
					} else {
						MessageBox.alert("Failed to clone", "Please select the policy ID", null);
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
					// getConformationBox();
					if (searchGrid.getSelectionModel().getSelectedItem() != null) {
						getConformationBox();
					} else {
						MessageBox.alert("Failed to clone", "Please select the policy ID", null);
					}
				}
			});
		}

		return deleteSearchButton;
	}

	private void getConformationBox() {
		final String policyID = searchGrid.getSelectionModel().getSelectedItem().getPid();
		MessageBox.confirm("Confirm", "Are you sure to delete Policy ID [" + policyID + "]", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					deleteSearchedPolicyID(policyID);
				}
			}
		});

		if (searchGrid.getStore().getCount() == 0) {
			deleteSearchButton.disable();
		}
	}

	private void deleteSearchedPolicyID(final String policyID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.deletePolicyID(policyID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					resetFields();
					MessageBox.alert("Success", "Policy ID [" + policyID + "] deleted successfully", null);
					searchGrid.getStore().remove(searchGrid.getSelectionModel().getSelectedItem());
					policyGrid.getStore().remove(searchGrid.getSelectionModel().getSelectedItem());
					refreshStore();
					getPolicyIDS();
				} else {
					MessageBox.alert("Error", "Error to delete Policy ID [" + policyID + "]", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Error to delete Policy ID [" + policyID + "]", null);
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
		service.searchPolicyID(softwareID, new AsyncCallback<List<Policy>>() {
			@Override
			public void onSuccess(List<Policy> result) {
				if (result.size() > 0) {
					searchGrid.getStore().removeAll();
					for (Policy pid : result) {
						PolicyID sID = new PolicyID(pid.getPolicyID(), pid.getName());
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

	private Widget getRecentRecordsPanel() {
		if (recentDataPanel == null) {
			recentDataPanel = new ContentPanel();
			recentDataPanel.setHeight(460);
			recentDataPanel.setHeaderVisible(false);
			configs = new ArrayList<ColumnConfig>();
			ColumnConfig column = new ColumnConfig();
			column.setId("pid");
			column.setWidth(290);
			column.setHeader("Policy ID");
			configs.add(column);

			column = new ColumnConfig();
			column.setId("pname");
			column.setWidth(300);
			column.setHeader("Policy Name");
			configs.add(column);

			columnModel = new ColumnModel(configs);
			List<PolicyID> softwareIDs = new ArrayList<PolicyID>();
			ListStore<PolicyID> clientList = new ListStore<PolicyID>();
			clientList.add(softwareIDs);
			policyGrid = new Grid<PolicyID>(clientList, columnModel);
			policyGrid.setBorders(true);
			policyGrid.setLoadMask(true);
			policyGrid.setStripeRows(true);
			policyGrid.setHeight(390);
			policyGrid.setWidth(600);
			policyGrid.addListener(Events.OnClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent ce) {
					getPolicyDetails(policyGrid.getSelectionModel().getSelectedItem().getPid());
				}
			});
			recentDataPanel.add(policyGrid);
			recentDataPanel.add(getNewCloneDeleteButtonbar());
		}
		return recentDataPanel;
	}

	private void getPolicyDetails(String policyID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getPolicyDetails(policyID, new AsyncCallback<Policy>() {
			@Override
			public void onSuccess(Policy result) {
				if (result != null) {
					Registry.register("policyDetails", result);
					setPolicyDetalisValues();
				} else {
					MessageBox.alert("Faild", "Faild to fetch policy details", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	private void setPolicyDetalisValues() {
		if (Registry.get("policyDetails") != null) {
			Policy result = (Policy) Registry.get("policyDetails");
			policyID.setValue(result.getPolicyID());
			name.setValue(result.getName());
			policyDescription.setValue(result.getDescription());
			mediaServerStartHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(result.getMediaServerStartTime()));
			mediaServerStartMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(result.getMediaServerStartTime()));
			mediaServerStopHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(result.getMediaServerStopTime()));
			mediaServerStopMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(result.getMediaServerStopTime()));
			prefetchStartHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(result.getPrefetchStartTime()));
			prefetchStartMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(result.getPrefetchStartTime()));
			prefetchStopHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(result.getPrefetchStopTime()));
			prefetchStopMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(result.getPrefetchStopTime()));
			bandwidth.setValue(result.getBandwidth());
			prefetch.setSimpleValue(result.getPrefetchContentType());
		}
	}

	private Widget getPolicyDetailsPanel() {
		if (PolicyDetailsPanel == null) {
			PolicyDetailsPanel = new ContentPanel();
			PolicyDetailsPanel.setHeaderVisible(false);
			FormPanel policyFormPanel = new FormPanel();
			policyFormPanel.setHeading("Policy Details");
			policyFormPanel.setBodyBorder(disabled);
			policyFormPanel.setBorders(false);
			policyFormPanel.setLabelWidth(270);
			policyFormPanel.setWidth(720);
			policyFormPanel.setButtonAlign(HorizontalAlignment.CENTER);
			policyID = new TextField<String>();
			policyID.setFieldLabel("Policy ID");
			policyID.setEnabled(false);
			policyDescription = new TextField<String>();
			policyDescription.setFieldLabel("Description");
			name = new TextField<String>();
			name.setFieldLabel("Name");
			policyFormPanel.add(policyID, new FormData("100%"));
			policyFormPanel.add(name, new FormData("100%"));
			policyFormPanel.add(policyDescription, new FormData("100%"));
			policyFormPanel.add(getInnerFormDetails());
			policyFormPanel.add(saveResetButtonbar());
			PolicyDetailsPanel.add(policyFormPanel);
		}
		return PolicyDetailsPanel;
	}

	private ContentPanel getInnerFormDetails() {
		ContentPanel innerPanel = new ContentPanel();
		innerPanel.setHeaderVisible(false);
		innerPanel.setLayout(new FitLayout());
		FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, new Label("Media Server Start Time (HH MM)"));
		flexTable.setWidget(0, 1, getMediaServerStartHours());
		flexTable.setWidget(0, 2, getMediaServerStartMinutes());
		flexTable.setWidget(1, 0, new Label("Media Server Stop Time (HH MM)"));
		flexTable.setWidget(1, 1, getMediaServerStopHours());
		flexTable.setWidget(1, 2, getMediaServerStopMinutes());
		flexTable.setWidget(2, 0, new Label("Bandwidth (MB)"));
		flexTable.setWidget(2, 1, getBandwidthField());
		flexTable.setWidget(3, 0, new Label("Prefetch Content Type"));
		flexTable.setWidget(3, 1, getPrefetchComboBox());
		flexTable.setWidget(4, 0, new Label("Prefetch Start Time (HH MM)"));
		flexTable.setWidget(4, 1, getPrefetchStartHours());
		flexTable.setWidget(4, 2, getPrefetchStartMinutes());
		flexTable.setWidget(5, 0, new Label("Prefetch Stop Time (HH MM)"));
		flexTable.setWidget(5, 1, getPrefetchStopHours());
		flexTable.setWidget(5, 2, getPrefetchStopMinutes());
		innerPanel.add(flexTable);
		return innerPanel;
	}

	private NumberField getBandwidthField() {
		if (bandwidth == null) {
			bandwidth = new NumberField();
			bandwidth.setPropertyEditorType(Integer.class);
		}
		return bandwidth;
	}

	private SimpleComboBox<String> getPrefetchStartMinutes() {
		if (prefetchStartMM == null) {
			prefetchStartMM = new SimpleComboBox<String>();
			prefetchStartMM.add(TimeUtilities.getMinuteList());
			prefetchStartMM.setEmptyText("Minutes");
			prefetchStartMM.setTriggerAction(TriggerAction.ALL);
		}
		return prefetchStartMM;
	}

	private SimpleComboBox<String> getPrefetchStopHours() {
		if (prefetchStopHH == null) {
			prefetchStopHH = new SimpleComboBox<String>();
			prefetchStopHH.add(TimeUtilities.getHourList());
			prefetchStopHH.setEmptyText("Hours");
			prefetchStopHH.setTriggerAction(TriggerAction.ALL);
		}
		return prefetchStopHH;
	}

	private SimpleComboBox<String> getPrefetchStopMinutes() {
		if (prefetchStopMM == null) {
			prefetchStopMM = new SimpleComboBox<String>();
			prefetchStopMM.add(TimeUtilities.getMinuteList());
			prefetchStopMM.setEmptyText("Minutes");
			prefetchStopMM.setTriggerAction(TriggerAction.ALL);
		}
		return prefetchStopMM;
	}

	private SimpleComboBox<String> getPrefetchStartHours() {
		if (prefetchStartHH == null) {
			prefetchStartHH = new SimpleComboBox<String>();
			prefetchStartHH.add(TimeUtilities.getHourList());
			prefetchStartHH.setEmptyText("Hours");
			prefetchStartHH.setTriggerAction(TriggerAction.ALL);
		}
		return prefetchStartHH;
	}

	private SimpleComboBox<String> getPrefetchComboBox() {
		if (prefetch == null) {
			prefetch = new SimpleComboBox<String>();
			prefetch.add(PrefetchContentTypeUtilities.getContentTypes());
			prefetch.setEmptyText("Prefetch Content");
			prefetch.setTriggerAction(TriggerAction.ALL);
		}

		return prefetch;
	}

	private SimpleComboBox<String> getMediaServerStopMinutes() {
		if (mediaServerStopMM == null) {
			mediaServerStopMM = new SimpleComboBox<String>();
			mediaServerStopMM.add(TimeUtilities.getMinuteList());
			mediaServerStopMM.setEmptyText("Minutes");
			mediaServerStopMM.setTriggerAction(TriggerAction.ALL);
		}
		return mediaServerStopMM;
	}

	private SimpleComboBox<String> getMediaServerStopHours() {
		if (mediaServerStopHH == null) {
			mediaServerStopHH = new SimpleComboBox<String>();
			mediaServerStopHH.add(TimeUtilities.getHourList());
			mediaServerStopHH.setEmptyText("Hours");
			mediaServerStopHH.setTriggerAction(TriggerAction.ALL);
		}
		return mediaServerStopHH;
	}

	private SimpleComboBox<String> getMediaServerStartMinutes() {
		if (mediaServerStartMM == null) {
			mediaServerStartMM = new SimpleComboBox<String>();
			mediaServerStartMM.add(TimeUtilities.getMinuteList());
			mediaServerStartMM.setEmptyText("Minutes");
			mediaServerStartMM.setTriggerAction(TriggerAction.ALL);
		}
		return mediaServerStartMM;
	}

	private SimpleComboBox<String> getMediaServerStartHours() {
		if (mediaServerStartHH == null) {
			mediaServerStartHH = new SimpleComboBox<String>();
			mediaServerStartHH.add(TimeUtilities.getHourList());
			mediaServerStartHH.setEmptyText("Hours");
			mediaServerStartHH.setTriggerAction(TriggerAction.ALL);
		}
		return mediaServerStartHH;
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
					updatePolicyDetails();
				}
			});

		}
		return saveButton;
	}

	private void updatePolicyDetails() {
		if (isValidFormData()) {
			LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
			Policy policyDetails = new Policy();
			policyDetails.setPolicyID(policyID.getValue());
			policyDetails.setDescription(policyDescription.getValue());
			policyDetails.setName(name.getValue());
			policyDetails.setBandwidth((Integer) bandwidth.getValue());
			policyDetails.setPrefetchContentType(prefetch.getSimpleValue());
			policyDetails.setMediaServerStartTime(TimeUtilities.setHoursToSeconds(mediaServerStartHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(mediaServerStartMM.getRawValue()));
			policyDetails.setMediaServerStopTime(TimeUtilities.setHoursToSeconds(mediaServerStopHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(mediaServerStopMM.getRawValue()));
			policyDetails.setPrefetchStartTime(TimeUtilities.setHoursToSeconds(prefetchStartHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(prefetchStartMM.getRawValue()));
			policyDetails.setPrefetchStopTime(TimeUtilities.setHoursToSeconds(prefetchStopHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(prefetchStopMM.getSimpleValue()));
			service.updatePolicyDetails(policyDetails, Registry.get("loggedUser").toString(), new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						MessageBox.alert("Success", "Policy Details Updated Successfully", null);
						refreshStore();
						getPolicyIDS();
						getPolicyDetails(policyID.getValue());

					} else {
						MessageBox.alert("Faild", "Faild to Updated Policy Details", null);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					MessageBox.alert("Faild to update", "Policy Details Are Faild TO Update Please Try Again", null);
				}
			});

		}
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
			resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					setPolicyDetalisValues();
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
			saveResetButtonbar.setAlignment(HorizontalAlignment.CENTER);

		}
		return newCloneDeleteButtonbar;
	}

	private Button getArchiveButton() {
		if (archiveButton == null) {
			archiveButton = new Button("Archive");
			archiveButton.setWidth(70);
		}
		return archiveButton;
	}

	private Button getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new Button("Delete");
			deleteButton.setWidth(70);
			deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// getDeleteConformation();
					if (policyGrid.getSelectionModel().getSelectedItem() != null) {
						getDeleteConformation();
					} else {
						MessageBox.alert("Failed to clone", "Please select the policy ID", null);
					}
				}
			});
		}

		return deleteButton;
	}

	private void getDeleteConformation() {
		final String policyID = policyGrid.getSelectionModel().getSelectedItem().getPid();
		MessageBox.confirm("Confirm", "Are you sure to delete Policy ID [" + policyID + "]", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					deletePolicyID(policyID);
				}
			}
		});
		if (policyGrid.getStore().getCount() == 0) {
			deleteButton.disable();
		}
	}

	private void deletePolicyID(final String policyID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.deletePolicyID(policyID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					resetFields();
					MessageBox.alert("Success", "Policy ID [" + policyID + "] deleted successfully", null);
					policyGrid.getStore().remove(policyGrid.getSelectionModel().getSelectedItem());
				} else {
					MessageBox.alert("Failed", "Error to delete Software ID [" + policyID + "]", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Error to delete Software ID [" + policyID + "]", null);
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
					if (policyGrid.getSelectionModel().getSelectedItem() != null) {
						clonePolicy(policyGrid.getSelectionModel().getSelectedItem().getPid());
					} else {
						MessageBox.alert("Failed to clone", "Please select the policy ID", null);
					}
				}
			});
		}

		return cloneButton;
	}

	private void clonePolicy(String policyID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getPolicyDetails(policyID, new AsyncCallback<Policy>() {
			@Override
			public void onSuccess(Policy result) {
				ClonePolicyDialog clonePolicy = new ClonePolicyDialog(result);
				clonePolicy.show();
				clonePolicy.center();
				clonePolicy.addListener(Events.Hide, new Listener<WindowEvent>() {
					public void handleEvent(WindowEvent be) {
						refreshStore();
						getPolicyIDS();
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Failed", "Failed to clone the policy", null);
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
					AddPolicyDialog addPolicyDialog = new AddPolicyDialog();
					addPolicyDialog.show();
					addPolicyDialog.center();
					addPolicyDialog.addListener(Events.Hide, new Listener<WindowEvent>() {
						public void handleEvent(WindowEvent be) {
							refreshStore();
							getPolicyIDS();
						}
					});

				}
			});
		}

		return newButton;
	}

	private void getPolicyIDS() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getPolicyIDS(new AsyncCallback<List<Policy>>() {
			@Override
			public void onSuccess(List<Policy> result) {
				if (result != null) {
					List<PolicyID> policyIDs = new ArrayList<PolicyID>();
					for (Policy policyID : result) {
						PolicyID pi = new PolicyID(policyID.getPolicyID(), policyID.getName());
						policyIDs.add(pi);
						policyGrid.getStore().add(pi);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Failed to load", "Failed to Retrive Policy IDs", null);
			}
		});
	}

	private void refreshStore() {
		policyGrid.getStore().removeAll();
	}

	private void resetFields() {
		policyID.clear();
		name.clear();
		policyDescription.clear();
		mediaServerStartHH.clearSelections();
		mediaServerStartMM.clearSelections();
		mediaServerStopHH.clearSelections();
		mediaServerStopMM.clearSelections();
		prefetchStartHH.clearSelections();
		prefetchStopMM.clearSelections();
		prefetchStopHH.clearSelections();
		prefetchStartHH.clearSelections();
		prefetch.clearSelections();
		bandwidth.clear();
		searchField.clear();
		searchGrid.getStore().removeAll();
	}

	private boolean isValidFormData() {
		boolean status = false;
		if (policyID.getValue() != null && policyID.getValue().trim().length() > 0) {
			status = true;
		} else {
			status = false;
			MessageBox.alert("Failed", "Please Select Policy First", null);
		}
		if (status) {
			if (name.getValue() != null && name.getValue().trim().length() > 0) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please Enter Name", null);
			}
		}
		if (status) {
			if (policyDescription.getValue() != null && policyDescription.getValue().trim().length() > 0) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please Enter Description", null);
			}
		}
		if (status) {
			if (mediaServerStartHH.getSimpleValue() != null && mediaServerStartHH.getSimpleValue().trim().length() > 0
					&& mediaServerStartMM.getSimpleValue() != null && mediaServerStartMM.getSimpleValue().trim().length() > 0
					&& TimeUtilities.isValidHour(mediaServerStartHH.getSimpleValue())
					&& TimeUtilities.isValidMinute(mediaServerStartMM.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid media server start time", null);
			}
		}
		if (status) {
			if (mediaServerStopHH.getSimpleValue() != null && mediaServerStopHH.getSimpleValue().trim().length() > 0
					&& mediaServerStopMM.getSimpleValue() != null && mediaServerStopMM.getSimpleValue().trim().length() > 0
					&& TimeUtilities.isValidHour(mediaServerStopHH.getSimpleValue())
					&& TimeUtilities.isValidMinute(mediaServerStopMM.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid media server stop  time", null);
			}
		}
		if (status) {
			if (prefetchStartHH.getSimpleValue() != null && prefetchStartHH.getSimpleValue().trim().length() > 0
					&& prefetchStartMM.getSimpleValue() != null && prefetchStartMM.getSimpleValue().trim().length() > 0
					&& TimeUtilities.isValidHour(prefetchStartHH.getSimpleValue())
					&& TimeUtilities.isValidMinute(prefetchStartMM.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid  prefetch start time", null);
			}
		}
		if (status) {
			if (prefetchStopHH.getSimpleValue() != null && prefetchStopHH.getSimpleValue().trim().length() > 0
					&& prefetchStopMM.getSimpleValue() != null && prefetchStopMM.getSimpleValue().trim().length() > 0
					&& TimeUtilities.isValidHour(prefetchStopHH.getSimpleValue())
					&& TimeUtilities.isValidMinute(prefetchStopMM.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid  prefetch stop time", null);
			}
		}
		if (status) {
			if (bandwidth.getValue() != null) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid bandwidth", null);
			}
		}
		if (status) {
			if (prefetch.getSimpleValue() != null && prefetch.getSimpleValue().trim().length() > 0
					&& PrefetchContentTypeUtilities.isValidContentType(prefetch.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please select valid  content type", null);
			}
		}
		if (status) {
			if ((TimeUtilities.setHoursToSeconds(prefetchStopHH.getSimpleValue()) + TimeUtilities.setMinutesToSeconds(prefetchStopMM
					.getSimpleValue())) > (TimeUtilities.setHoursToSeconds(prefetchStartHH.getSimpleValue()) + TimeUtilities
					.setMinutesToSeconds(prefetchStartMM.getSimpleValue()))) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Prefetch stop time should grater than start time", null);
			}
		}
		if (status) {
			if ((TimeUtilities.setHoursToSeconds(mediaServerStopHH.getSimpleValue()) + TimeUtilities.setMinutesToSeconds(mediaServerStopMM
					.getSimpleValue())) > (TimeUtilities.setHoursToSeconds(mediaServerStartHH.getSimpleValue()) + TimeUtilities
					.setMinutesToSeconds(mediaServerStartMM.getSimpleValue()))) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "mediaserver stop time should grater than start time", null);
			}
		}
		return status;
	}
}
