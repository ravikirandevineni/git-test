package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Config;

public class AddConfigDialog extends Dialog {

	private FormPanel addConfigPanel;
	private TextField<String> key;
	private TextField<String> value;
	private Button saveButton;
	private Button resetButton;
	private Button cancelButton;
	private ButtonBar saveResetCancelButtonbar;

	public AddConfigDialog() {
		setLayout(new FitLayout());
		setProperties();
		add(getAddSoftwarePanel());

	}

	private void setProperties() {
		setHeading("New Config");
		setButtons("");
		setModal(true);
		setResizable(false);
		setBodyBorder(true);
		setWidth(450);
		setHeight(200);
	}

	private Component getAddSoftwarePanel() {
		if (addConfigPanel == null) {
			addConfigPanel = new FormPanel();
			addConfigPanel.setHeaderVisible(false);
			addConfigPanel.setBorders(false);
			addConfigPanel.setBodyBorder(false);
			addConfigPanel.setLabelWidth(80);
			addConfigPanel.add(getKeyField(), new FormData("100%"));
			addConfigPanel.add(getValueField(), new FormData("100%"));
			addConfigPanel.add(getSaveResetCacelButtonbar());
		}
		return addConfigPanel;
	}

	private Widget getSaveResetCacelButtonbar() {
		if (saveResetCancelButtonbar == null) {
			saveResetCancelButtonbar = new ButtonBar();
			saveResetCancelButtonbar.add(getSaveButton());
			saveResetCancelButtonbar.add(getResetButton());
			saveResetCancelButtonbar.add(getCancelButton());
			saveResetCancelButtonbar.setAlignment(HorizontalAlignment.CENTER);

		}
		return saveResetCancelButtonbar;
	}

	private Button getSaveButton() {
		if (saveButton == null) {
			saveButton = new Button("Save");
			saveButton.setWidth(70);
			saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {

					saveConfigDetails();
				}
			});
		}
		return saveButton;
	}

	private void saveConfigDetails() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		Config config = new Config();
		config.setKey(key.getValue());
		config.setValue(value.getValue());
		service.setConfigDetails(config, Registry.get("loggedUser").toString(), new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					AddConfigDialog.this.hide();
					MessageBox.alert("Success", "New Software Added Successfully", null);
				} else {
					AddConfigDialog.this.hide();
					MessageBox.alert("Success", "Faild to add the new software", null);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
		}
		return resetButton;
	}

	private Button getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new Button("Cancel");
			cancelButton.setWidth(70);
		}
		return cancelButton;
	}

	private TextField<String> getKeyField() {

		if (key == null) {
			key = new TextField<String>();
			key.setFieldLabel("Key");
		}
		return key;
	}

	private TextField<String> getValueField() {
		if (value == null) {
			value = new TextField<String>();
			value.setFieldLabel("value");
		}
		return value;
	}

}
