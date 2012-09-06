package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.icelero.ias.ad.shared.Config;

public class CloneConfigDialog extends Dialog {

	private FormPanel cloneConfigPanel;
	private TextField<String> key;
	private TextField<String> value;
	private Button saveButton;
	private Button resetButton;
	private Button cancelButton;
	private ButtonBar saveResetCancelButtonbar;
	Config config;

	public CloneConfigDialog(Config config) {
		this.config = config;
		setLayout(new FitLayout());
		setProperties();
		add(getCloneConfigPanel());
	}

	private void setProperties() {
		setHeading("Clone Config");
		setButtons("");
		setModal(true);
		setResizable(false);
		setBodyBorder(true);
		setWidth(450);
		setHeight(200);
	}

	private Component getCloneConfigPanel() {
		if (cloneConfigPanel == null) {
			cloneConfigPanel = new FormPanel();
			cloneConfigPanel.setHeaderVisible(false);
			cloneConfigPanel.setBorders(false);
			cloneConfigPanel.setBodyBorder(false);
			cloneConfigPanel.setLabelWidth(80);
			cloneConfigPanel.add(getKeyField(), new FormData("100%"));
			cloneConfigPanel.add(getValueField(), new FormData("100%"));
			cloneConfigPanel.add(getSaveResetCacelButtonbar());
			cloneConfigDetails();
		}
		return cloneConfigPanel;
	}

	private void cloneConfigDetails() {
		key.setValue(config.getKey());
		value.setValue(config.getValue());
	}

	private ButtonBar getSaveResetCacelButtonbar() {
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
		}
		return saveButton;
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
