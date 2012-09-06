package com.icelero.ias.ad.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Config;

public class ConfigPanel extends ContentPanel {

	private ContentPanel configDetailsPanel;
	private FormPanel configDetailsFormPanel;
	private TextField<String> configKey;
	private TextField<String> configValue;
	private ButtonBar saveResetButtonbar;
	private Button saveButton;
	private Button resetButton;
	private ContentPanel configKeysListPanel;
	private Grid<ConfigKeys> configKeysGrid;
	private ButtonBar newCloneDeleteButtonbar;
	private Button newButton;
	private Button cloneButton;
	private Button deleteButton;
	private ColumnModel columnModel;
	private List<ColumnConfig> configs;

	public ConfigPanel() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		setHeight(520);
		getConfigKeys();
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 720);
		eastData.setMargins(new Margins(5));
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 600);
		westData.setMargins(new Margins(5));
		add(getConfigDetailsPanel(), eastData);
		add(getConfigRecordDetailsPanel(), westData);
	}

	private Widget getConfigRecordDetailsPanel() {
		if (configKeysListPanel == null) {
			configKeysListPanel = new ContentPanel();
			configKeysListPanel.setHeading("Config Detais");
			configKeysListPanel.setHeight(460);
			configKeysListPanel.add(getConfigKeysGrid());
			configKeysListPanel.add(getNewCloneDeleteButtonbar());
		}
		return configKeysListPanel;
	}

	private Widget getConfigDetailsPanel() {
		if (configDetailsPanel == null) {
			configDetailsPanel = new ContentPanel();
			configDetailsPanel.setHeaderVisible(false);
			configDetailsPanel.add(getConfigDetailsFormPanel());
		}
		return configDetailsPanel;
	}

	private Widget getConfigDetailsFormPanel() {
		if (configDetailsFormPanel == null) {
			configDetailsFormPanel = new FormPanel();
			configDetailsFormPanel.setBorders(false);
			configDetailsFormPanel.setBodyBorder(disabled);
			configDetailsFormPanel.setHeading("Config Details");
			configDetailsFormPanel.setButtonAlign(HorizontalAlignment.CENTER);
			configDetailsFormPanel.setLabelWidth(120);
			configDetailsFormPanel.add(getKeyField(), new FormData("100%"));
			configDetailsFormPanel.add(getConfigValueField(), new FormData("100%"));
			configDetailsFormPanel.add(saveResetButtonbar());
		}

		return configDetailsFormPanel;
	}

	private Widget getKeyField() {
		if (configKey == null) {
			configKey = new TextField<String>();
			configKey.setFieldLabel("Key");
		}
		return configKey;
	}

	private Widget getConfigValueField() {
		if (configValue == null) {
			configValue = new TextField<String>();
			configValue.setFieldLabel("Value");
		}
		return configValue;
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

	private Button getResetButton() {
		if (saveButton == null) {
			saveButton = new Button("Save");
			saveButton.setWidth(70);
		}
		return saveButton;
	}

	private Button getSaveButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
		}
		return resetButton;
	}

	private Widget getNewCloneDeleteButtonbar() {
		if (newCloneDeleteButtonbar == null) {
			newCloneDeleteButtonbar = new ButtonBar();
			newCloneDeleteButtonbar.add(getNewButton());
			newCloneDeleteButtonbar.add(getCloneButton());
			newCloneDeleteButtonbar.add(getDeleteButton());

		}
		return newCloneDeleteButtonbar;
	}

	private Button getNewButton() {

		if (newButton == null) {
			newButton = new Button("New");
			newButton.setWidth(70);
			newButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					AddConfigDialog addConfigDialog = new AddConfigDialog();
					addConfigDialog.show();
					addConfigDialog.center();
				}
			});
		}
		return newButton;
	}

	private Button getCloneButton() {
		if (cloneButton == null) {
			cloneButton = new Button("Clone");
			cloneButton.setWidth(70);
			
			cloneButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				
				@Override
				public void componentSelected(ButtonEvent ce) {
					//CloneConfigDialog cloneconfig=new CloneConfigDialog(config);
					
					
				}
			});
		}
		return cloneButton;
	}

	private Button getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new Button("Delete");
			deleteButton.setWidth(70);
		}
		return deleteButton;
	}

	private Grid<ConfigKeys> getConfigKeysGrid() {

		if (configKeysGrid == null) {
			configKeysGrid = new Grid<ConfigKeys>(getConfigKeysList(), getColumnModel());
			setGridProperties();
			configKeysGrid.addListener(Events.OnClick, new Listener<ComponentEvent>() {
				@Override
				public void handleEvent(ComponentEvent ce) {
					getConfigDetails(configKeysGrid.getSelectionModel().getSelectedItem().getKey());
				}
			});
		}

		return configKeysGrid;
	}

	private void setGridProperties() {
		configKeysGrid.setBorders(true);
		configKeysGrid.setLoadMask(true);
		configKeysGrid.setStripeRows(true);
		configKeysGrid.setAutoWidth(false);
		configKeysGrid.setHeight(390);
		configKeysGrid.setWidth(600);
	}

	private ColumnModel getColumnModel() {
		configs = new ArrayList<ColumnConfig>();
		ColumnConfig column = new ColumnConfig();
		column.setId("key");
		column.setWidth(290);
		column.setHeader("Key");
		configs.add(column);
		column = new ColumnConfig();
		column.setId("value");
		column.setWidth(300);
		column.setHeader("Value");
		configs.add(column);
		columnModel = new ColumnModel(configs);
		return columnModel;
	}

	private ListStore<ConfigKeys> getConfigKeysList() {
		List<ConfigKeys> configKey = new ArrayList<ConfigKeys>();
		ListStore<ConfigKeys> softwareIDSList = new ListStore<ConfigKeys>();
		softwareIDSList.add(configKey);
		return softwareIDSList;
	}

	private void getConfigKeys() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getConfigKeys(new AsyncCallback<List<Config>>() {
			@Override
			public void onSuccess(List<Config> result) {
				if (result != null) {
					List<ConfigKeys> softwareIDs = new ArrayList<ConfigKeys>();
					for (Config config : result) {
						ConfigKeys si = new ConfigKeys(config.getKey(), config.getValue());
						softwareIDs.add(si);
						configKeysGrid.getStore().add(si);
					}
				} else {
					MessageBox.alert("Faild", "No data avaliable", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MessageBox.alert("Load Data Failed", "Failed to load data from datbase", null);
			}
		});
	}

	private void getConfigDetails(String key) {

		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		service.getConfigDetails(key, new AsyncCallback<Config>() {

			@Override
			public void onSuccess(Config result) {
				if (result != null) {
					Registry.register("ConfigDetails", result);
					setConfigValues();
				} else {
					MessageBox.alert("Faild", "No details found", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Error", "Faild to fetch the config details", null);
			}
		});

	}

	private void setConfigValues() {
		if (Registry.get("ConfigDetails") != null) {
			Config config = (Config) Registry.get("ConfigDetails");
			configKey.setValue(config.getKey());
			configValue.setValue(config.getValue());
		}
	}

}