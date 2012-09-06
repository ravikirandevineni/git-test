package com.icelero.ias.ad.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.Provision;
import com.icelero.ias.ad.shared.utilities.AccountStateUtilities;
import com.icelero.ias.ad.shared.utilities.PrefetchContentTypeUtilities;
import com.icelero.ias.ad.shared.utilities.TimeUtilities;

public class AccountPanel extends ContentPanel {
	private ContentPanel detailPanel;
	private HorizontalPanel searchPanel;
	private FormPanel accountDataPanel;
	private ContentPanel clientDataPanel;
	private FormPanel policyDataPanel;

	private TextField<String> imsiSearchField;
	private TextField<String> imsiValueField;
	private SimpleComboBox<String> state;
	private TextField<String> accountDetailspolicyID;

	private TextField<String> policyID;
	private TextField<String> name;
	private TextField<String> description;
	private SimpleComboBox<String> mediaServerStartHH;
	private SimpleComboBox<String> mediaServerStartMM;
	private SimpleComboBox<String> mediaServerStopHH;
	private SimpleComboBox<String> mediaServerStopMM;
	private SimpleComboBox<String> prefetchStartHH;
	private SimpleComboBox<String> prefetchStartMM;
	private SimpleComboBox<String> prefetchStopMM;
	private SimpleComboBox<String> prefetchStopHH;
	private SimpleComboBox<String> prefetch;
	private TextField<Integer> bandwidth;
	private FlexTable flexTable;
	private Button searchButton;
	private KeyListener enterKeyListener;
	private Grid<ClientID> clientIDSGrid;
	private List<ColumnConfig> configs;
	private ColumnModel columnModel;
	private Button deleteClientButton;
	private Button updateState;
	private Button resetState;
	private ButtonBar saveResetButtonbar;

	public AccountPanel() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setHeight(510);
		BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 50);
		northData.setMargins(new Margins(5));
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(5));
		add(getSearchPanel(), northData);
		add(getSearchDetailPanel(), centerData);
	}

	private Widget getSearchPanel() {
		if (searchPanel == null) {
			searchPanel = new HorizontalPanel();
			searchPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
			searchPanel.add(getMSISDNField());
			searchPanel.add(getSearchButton());
		}
		return searchPanel;
	}

	private ButtonBar getSaveResetButtonbar() {
		if (saveResetButtonbar == null) {
			saveResetButtonbar = new ButtonBar();
			saveResetButtonbar.setAlignment(HorizontalAlignment.CENTER);
			saveResetButtonbar.add(getUpdateButton());
			saveResetButtonbar.add(getResetButton());
		}
		return saveResetButtonbar;
	}

	private TextField<String> getMSISDNField() {
		if (imsiSearchField == null) {
			imsiSearchField = new TextField<String>();
			imsiSearchField.setFieldLabel("IMSI");
			imsiSearchField.setEmptyText("Enter IMSI");
			imsiSearchField.addKeyListener(getEnterKeyListener());
		}
		return imsiSearchField;
	}

	private KeyListener getEnterKeyListener() {
		if (enterKeyListener == null) {
			enterKeyListener = new KeyListener() {
				public void componentKeyDown(ComponentEvent event) {
					if (event.getKeyCode() == 13) {
						search();
					}
				}
			};
		}
		return enterKeyListener;
	}

	private Button getSearchButton() {
		if (searchButton == null) {
			searchButton = new Button("Search");
			searchButton.setWidth(70);
			searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					search();
				}
			});
		}
		return searchButton;
	}

	private Widget getSearchDetailPanel() {
		if (detailPanel == null) {
			detailPanel = new ContentPanel();
			detailPanel.setHeaderVisible(false);
			HBoxLayout layout = new HBoxLayout();
			layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
			detailPanel.setLayout(layout);
			detailPanel.add(getAccountDataPanel());
			detailPanel.add(getClientDataPanel());
			detailPanel.add(getPolicyDataPanel());
		}
		return detailPanel;
	}

	private Widget getAccountDataPanel() {
		if (accountDataPanel == null) {
			accountDataPanel = new FormPanel();
			accountDataPanel.setHeading("Account Details");
			accountDataPanel.setLabelWidth(70);
			accountDataPanel.setWidth(340);
			accountDataPanel.add(getIMSI(), new FormData("80%"));
			accountDataPanel.add(getStateField(), new FormData("80%"));
			accountDataPanel.add(getPolicyIDField(), new FormData("80%"));
			accountDataPanel.add(getSaveResetButtonbar());
		}
		return accountDataPanel;
	}

	private Widget getClientDataPanel() {
		if (clientDataPanel == null) {
			clientDataPanel = new ContentPanel();
			clientDataPanel.setHeading("Clients");
			clientDataPanel.setPixelSize(290, 450);
			clientDataPanel.add(getClienIDSGrid());
			clientDataPanel.add(deleteClientButton());
			clientDataPanel.setButtonAlign(HorizontalAlignment.RIGHT);
		}
		return clientDataPanel;
	}

	private Widget getPolicyDataPanel() {
		if (policyDataPanel == null) {
			policyDataPanel = new FormPanel();
			policyDataPanel.setHeading("Policy Details");
			policyDataPanel.setLabelWidth(260);
			policyDataPanel.setWidth(700);
			policyDataPanel.add(getIDField(), new FormData("100%"));
			policyDataPanel.add(getNameField(), new FormData("100%"));
			policyDataPanel.add(getDescriptionField(), new FormData("100%"));
			ContentPanel innerComboxPanel = new ContentPanel();
			innerComboxPanel.setLayout(new FitLayout());
			innerComboxPanel.setHeaderVisible(false);
			innerComboxPanel.add(getFlexTable());
			policyDataPanel.add(innerComboxPanel);
		}
		return policyDataPanel;
	}

	private FlexTable getFlexTable() {
		if (flexTable == null) {
			flexTable = new FlexTable();

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
		}
		return flexTable;
	}

	private TextField<Integer> getBandwidthField() {
		if (bandwidth == null) {
			bandwidth = new TextField<Integer>();
		}
		return bandwidth;
	}

	private SimpleComboBox<String> getPrefetchStartMinutes() {
		if (prefetchStartMM == null) {
			prefetchStartMM = new SimpleComboBox<String>();
			prefetchStartMM.add(TimeUtilities.getMinuteList());
			prefetchStartMM.setEmptyText("Minutes");
			prefetchStartMM.setTriggerAction(TriggerAction.ALL);
			prefetchStartMM.setReadOnly(true);
		}
		return prefetchStartMM;
	}

	private SimpleComboBox<String> getPrefetchStopHours() {
		if (prefetchStopHH == null) {
			prefetchStopHH = new SimpleComboBox<String>();
			prefetchStopHH.add(TimeUtilities.getHourList());
			prefetchStopHH.setEmptyText("Hours");
			prefetchStopHH.setTriggerAction(TriggerAction.ALL);
			prefetchStopHH.setReadOnly(true);

		}
		return prefetchStopHH;
	}

	private SimpleComboBox<String> getPrefetchStopMinutes() {
		if (prefetchStopMM == null) {
			prefetchStopMM = new SimpleComboBox<String>();
			prefetchStopMM.add(TimeUtilities.getMinuteList());
			prefetchStopMM.setEmptyText("Minutes");
			prefetchStopMM.setTriggerAction(TriggerAction.ALL);
			prefetchStopMM.setReadOnly(true);
		}
		return prefetchStopMM;
	}

	private SimpleComboBox<String> getPrefetchStartHours() {
		if (prefetchStartHH == null) {
			prefetchStartHH = new SimpleComboBox<String>();
			prefetchStartHH.add(TimeUtilities.getHourList());
			prefetchStartHH.setEmptyText("Hours");
			prefetchStartHH.setTriggerAction(TriggerAction.ALL);
			prefetchStartHH.setReadOnly(true);
		}
		return prefetchStartHH;
	}

	private SimpleComboBox<String> getPrefetchComboBox() {
		if (prefetch == null) {
			prefetch = new SimpleComboBox<String>();
			prefetch.add(PrefetchContentTypeUtilities.getContentTypes());
			prefetch.setTriggerAction(TriggerAction.ALL);
			prefetch.setReadOnly(true);
			prefetch.setEmptyText("Prefetch Content ");
		}

		return prefetch;
	}

	private SimpleComboBox<String> getMediaServerStopMinutes() {
		if (mediaServerStopMM == null) {
			mediaServerStopMM = new SimpleComboBox<String>();
			mediaServerStopMM.add(TimeUtilities.getMinuteList());
			mediaServerStopMM.setEmptyText("Minutes");
			mediaServerStopMM.setTriggerAction(TriggerAction.ALL);
			mediaServerStopMM.setReadOnly(true);
		}
		return mediaServerStopMM;
	}

	private SimpleComboBox<String> getMediaServerStopHours() {
		if (mediaServerStopHH == null) {
			mediaServerStopHH = new SimpleComboBox<String>();
			mediaServerStopHH.add(TimeUtilities.getHourList());
			mediaServerStopHH.setEmptyText("Hours");
			mediaServerStopHH.setTriggerAction(TriggerAction.ALL);
			mediaServerStopHH.setReadOnly(true);
		}
		return mediaServerStopHH;
	}

	private SimpleComboBox<String> getMediaServerStartMinutes() {
		if (mediaServerStartMM == null) {
			mediaServerStartMM = new SimpleComboBox<String>();
			mediaServerStartMM.add(TimeUtilities.getMinuteList());
			mediaServerStartMM.setEmptyText("Minutes");
			mediaServerStartMM.setTriggerAction(TriggerAction.ALL);
			mediaServerStartMM.setReadOnly(true);
		}
		return mediaServerStartMM;
	}

	private SimpleComboBox<String> getMediaServerStartHours() {
		if (mediaServerStartHH == null) {
			mediaServerStartHH = new SimpleComboBox<String>();
			mediaServerStartHH.add(TimeUtilities.getHourList());
			mediaServerStartHH.setEmptyText("Hours");
			mediaServerStartHH.setTriggerAction(TriggerAction.ALL);
			mediaServerStartHH.setReadOnly(true);
		}
		return mediaServerStartHH;
	}

	private Button deleteClientButton() {
		if (deleteClientButton == null) {
			deleteClientButton = new Button("Delete");
			deleteClientButton.setWidth(70);
			deleteClientButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// deleteClientConformation();
					if (clientIDSGrid.getSelectionModel().getSelectedItem() != null) {
						deleteClientConformation();
					} else {
						MessageBox.alert("Failed to clone", "Please select the client id", null);
					}
				}
			});
		}
		return deleteClientButton;
	}

	private void deleteClientConformation() {
		final String clientID = clientIDSGrid.getSelectionModel().getSelectedItem().getCid();
		MessageBox.confirm("Confirm", "Are you sure to delete Client ID [" + clientID + "]", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				Button btn = be.getButtonClicked();
				if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
					deleteClientID(clientID);
				}
			}
		});
		if (clientIDSGrid.getStore().getCount() == 0) {
			deleteClientButton.disable();
		}
	}

	private void deleteClientID(final String clientID) {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.deleteClientID(clientID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					MessageBox.alert("Success", "Client ID [" + clientID + "] deleted successfully", null);
					clientIDSGrid.getStore().remove(clientIDSGrid.getSelectionModel().getSelectedItem());
				} else {
					MessageBox.alert("Error", "Error to delete Client ID [" + clientID + "]", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Error to delete Client ID [" + clientID + "]", null);
			}
		});
	}

	private Grid<ClientID> getClienIDSGrid() {
		if (clientIDSGrid == null) {
			configs = new ArrayList<ColumnConfig>();
			ColumnConfig column = new ColumnConfig();
			column.setId("cid");
			column.setWidth(293);
			column.setHeader("Client IDs");
			configs.add(column);
			columnModel = new ColumnModel(configs);
			List<ClientID> clientIDs = new ArrayList<ClientID>();
			ListStore<ClientID> clientList = new ListStore<ClientID>();
			clientList.add(clientIDs);
			clientIDSGrid = new Grid<ClientID>(clientList, columnModel);
			setGridProperties();
		}
		return clientIDSGrid;
	}

	private void setGridProperties() {
		clientIDSGrid.setStyleAttribute("borderTop", "none");
		clientIDSGrid.setAutoExpandColumn("cid");
		clientIDSGrid.setBorders(true);
		clientIDSGrid.setLoadMask(true);
		clientIDSGrid.setStripeRows(true);
		clientIDSGrid.setHeight(320);
	}

	protected void search() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		final String imsi = imsiSearchField.getValue();
		service.getSearchResults(imsi, new AsyncCallback<Provision>() {
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MessageBox.alert("Failed", "Invalid IMSI Number [" + imsi + "]", null);
				resetAccountDetails();
			}

			@Override
			public void onSuccess(Provision provision) {
				if (provision != null) {
					Registry.register("policy", provision);
					setAccountDetailsValues(provision);

				} else {
					resetAccountDetails();
					MessageBox.alert("Failed", "Invalid IMSI Number [" + imsi + "]", null);
				}
			}
		});
	}

	private void setAccountDetailsValues(Provision provision) {
		setAccountFieldValues(provision);
		Policy policy = provision.getPolicy();
		if (policy != null) {
			setPolicyFieldValues(policy);
		}
		clientIDSGrid.getStore().removeAll();
		List<String> cLists = provision.getClientIDS();
		if (cLists != null) {
			List<ClientID> clientIDs = new ArrayList<ClientID>();
			for (String clientID : cLists) {
				ClientID ci = new ClientID(clientID);
				clientIDs.add(ci);
				clientIDSGrid.getStore().add(ci);
			}
		}
	}

	private void setPolicyFieldValues(Policy policy) {
		policyID.setValue(policy.getPolicyID());
		name.setValue(policy.getName());
		description.setValue(policy.getDescription());
		mediaServerStartHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(policy.getMediaServerStartTime()));
		mediaServerStartMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(policy.getMediaServerStartTime()));
		mediaServerStopHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(policy.getMediaServerStopTime()));
		mediaServerStopMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(policy.getMediaServerStopTime()));
		prefetchStartHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(policy.getPrefetchStartTime()));
		prefetchStartMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(policy.getPrefetchStartTime()));
		prefetchStopHH.setSimpleValue(TimeUtilities.getHoursFromSeconds(policy.getPrefetchStopTime()));
		prefetchStopMM.setSimpleValue(TimeUtilities.getMinutesFromSeconds(policy.getPrefetchStopTime()));
		bandwidth.setValue(policy.getBandwidth());
		prefetch.setSimpleValue(policy.getPrefetchContentType());
	}

	private void setAccountFieldValues(Provision provision) {
		imsiValueField.setValue(provision.getMsisdn());
		state.setSimpleValue(provision.getState());
		accountDetailspolicyID.setValue(provision.getPolicyid());
	}

	private void resetAccountDetails() {
		imsiValueField.clear();
		state.clearSelections();
		mediaServerStartHH.clearSelections();
		mediaServerStartMM.clearSelections();
		mediaServerStopHH.clearSelections();
		mediaServerStopMM.clearSelections();
		prefetchStartHH.clearSelections();
		prefetchStopMM.clearSelections();
		prefetchStopHH.clearSelections();
		prefetchStartHH.clearSelections();
		prefetch.clearSelections();
		name.clear();
		accountDetailspolicyID.clear();
		policyID.clear();
		description.clear();
		bandwidth.clear();
		clientIDSGrid.getStore().removeAll();
	}

	private TextField<String> getIMSI() {
		if (imsiValueField == null) {
			imsiValueField = new TextField<String>();
			imsiValueField.setFieldLabel("IMSI");
			imsiValueField.setEnabled(false);
		}
		return imsiValueField;
	}

	private SimpleComboBox<String> getStateField() {
		if (state == null) {
			state = new SimpleComboBox<String>();
			state.setFieldLabel("State");
			state.add(AccountStateUtilities.getState());
			state.setTriggerAction(TriggerAction.ALL);
			state.setEmptyText("Select state");
		}
		return state;
	}

	private TextField<String> getPolicyIDField() {
		if (accountDetailspolicyID == null) {
			accountDetailspolicyID = new TextField<String>();
			accountDetailspolicyID.setFieldLabel("Policy ID");
			accountDetailspolicyID.setEnabled(false);
		}
		return accountDetailspolicyID;
	}

	private TextField<String> getDescriptionField() {
		if (description == null) {
			description = new TextField<String>();
			description.setFieldLabel("Description");
			description.setEnabled(false);
		}
		return description;
	}

	private Button getUpdateButton() {
		if (updateState == null) {
			updateState = new Button("Save");
			updateState.setWidth(70);
			updateState.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (imsiValueField.getValue() != null && imsiValueField.getValue().trim().length() > 0) {
						updateAccountState();
					} else {

						MessageBox.alert("Failed", "Please search and then update state", null);
					}
				}
			});

		}
		return updateState;
	}

	private Button getResetButton() {
		if (resetState == null) {
			resetState = new Button("Reset");
			resetState.setWidth(70);
			resetState.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					resetAccountState();
				}
			});

		}
		return resetState;
	}

	private void resetAccountState() {

		/*
		 * if (Registry.get("policy") != null) { Provision policy = (Provision)
		 * Registry.get("policy"); state.setSimpleValue(policy.getState()); }
		 */
		search();
	}

	private void updateAccountState() {

		if (AccountStateUtilities.isValidState(state.getSimpleValue())) {
			LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
			service.updateAccountState(imsiValueField.getValue(), state.getSimpleValue(), new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						MessageBox.alert("Success", "Account State Updated Successfully", null);
					} else {

						MessageBox.alert("Failed", "Failed to update the account", null);
					}

				}

				@Override
				public void onFailure(Throwable caught) {
					MessageBox.alert("Failed", "Please Update Later", null);

				}
			});

		} else {

			MessageBox.alert("Failed", "Select valid state", null);
		}

	}

	private TextField<String> getNameField() {
		if (name == null) {
			name = new TextField<String>();
			name.setFieldLabel("Name");
			name.setEnabled(false);
		}
		return name;
	}

	private TextField<String> getIDField() {
		if (policyID == null) {
			policyID = new TextField<String>();
			policyID.setFieldLabel("Policy ID");
			policyID.setEnabled(false);
		}
		return policyID;
	}

	public Widget getAccountDetailsPanel() {
		return this;
	}
}
