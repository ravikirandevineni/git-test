package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Software;

public class CloneSoftwareDialog extends Dialog {

	private FormPanel addSoftwarePanel;
	private TextArea releaseNotes;
	private TextField<String> version;
	private DateField releaseDate;
	private TextField<String> build;
	private CheckBox mandatory;
	private Button saveButton;
	private Button resetButton;
	private Button cancleButton;
	private ButtonBar saveResetCancelButtonbar;
	Software clonedSoftwareDetails;
	private TextField<String> url;

	public CloneSoftwareDialog(Software clonedSoftwareDetails) {
		this.clonedSoftwareDetails = clonedSoftwareDetails;
		setLayout(new FitLayout());
		setProperties();
		add(getAddSoftwarePanel());
	}

	private void setProperties() {
		setHeading("Clone Software");
		setButtons("");
		setModal(true);
		setResizable(false);
		setBodyBorder(true);
		setWidth(400);
		setHeight(475);
	}

	private FormPanel getAddSoftwarePanel() {
		if (addSoftwarePanel == null) {
			addSoftwarePanel = new FormPanel();
			addSoftwarePanel.setHeaderVisible(false);
			addSoftwarePanel.setBorders(false);
			addSoftwarePanel.setBodyBorder(false);
			addSoftwarePanel.setLabelWidth(100);
			addSoftwarePanel.setButtonAlign(HorizontalAlignment.CENTER);
			addSoftwarePanel.add(getSoftwareVersionField(), new FormData("100%"));
			addSoftwarePanel.add(getBuildField(), new FormData("100%"));
			addSoftwarePanel.add(getMandatory(), new FormData("33%"));
			addSoftwarePanel.add(getURLField(), new FormData("100%"));
			addSoftwarePanel.add(getreleaseDateField(), new FormData("100%"));
			addSoftwarePanel.add(getSoftwareReleaseNotes(), new FormData("100%"));
			cloneSoftwareDetails();
			addSoftwarePanel.add(getSaveResetCacelButtonbar());
		}
		return addSoftwarePanel;
	}

	private void cloneSoftwareDetails() {
		releaseNotes.setValue(clonedSoftwareDetails.getNotes());
		version.setValue(clonedSoftwareDetails.getVersion());
		build.setValue(clonedSoftwareDetails.getBuild());
		releaseDate.setValue(clonedSoftwareDetails.getReleaseDate());
		url.setValue(clonedSoftwareDetails.getUrl());
		if (clonedSoftwareDetails.getMandatory().equalsIgnoreCase("y")) {
			mandatory.setValue(true);
		} else {
			mandatory.setValue(false);
		}
	}

	private Widget getSaveResetCacelButtonbar() {
		if (saveResetCancelButtonbar == null) {
			saveResetCancelButtonbar = new ButtonBar();
			saveResetCancelButtonbar.add(getSaveButton());
			saveResetCancelButtonbar.add(getResetButton());
			saveResetCancelButtonbar.add(getCancleButton());
			saveResetCancelButtonbar.setAlignment(HorizontalAlignment.CENTER);
		}
		return saveResetCancelButtonbar;
	}

	private Button getCancleButton() {
		if (cancleButton == null) {
			cancleButton = new Button("Cancel");
			cancleButton.setWidth(70);
			cancleButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					MessageBox.confirm("Confirm", "Do you want to save changes?", new Listener<MessageBoxEvent>() {
						@Override
						public void handleEvent(MessageBoxEvent be) {
							Button btn = be.getButtonClicked();
							if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
								if (saveSoftwareDetails()) {
									CloneSoftwareDialog.this.hide();
								}
							} else if (Dialog.NO.equalsIgnoreCase(btn.getItemId())) {
								CloneSoftwareDialog.this.hide();
							}
						}
					});
				}
			});
		}
		return cancleButton;
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
			DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy");
			releaseDate.getPropertyEditor().setFormat(dtf);
			releaseDate.setEditable(false);
			resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					cloneSoftwareDetails();
				}
			});
		}
		return resetButton;
	}

	private Button getSaveButton() {
		if (saveButton == null) {
			saveButton = new Button("Save");
			saveButton.setWidth(70);
			saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {

					saveSoftwareDetails();

				}
			});
		}
		return saveButton;
	}

	private TextField<String> getSoftwareReleaseNotes() {
		if (releaseNotes == null) {
			releaseNotes = new TextArea();
			releaseNotes.setFieldLabel("Release Notes");
			releaseNotes.setSize(75, 250);
		}
		return releaseNotes;
	}

	private TextField<String> getSoftwareVersionField() {
		if (version == null) {
			version = new TextField<String>();
			version.setFieldLabel("Version");
		}
		return version;
	}

	private DateField getreleaseDateField() {
		if (releaseDate == null) {
			releaseDate = new DateField();
			releaseDate.setFieldLabel("Release Date");
			DateTimeFormat dtf = DateTimeFormat.getFormat("MM/dd/yyyy");
			releaseDate.getPropertyEditor().setFormat(dtf);
		}
		return releaseDate;
	}

	private TextField<String> getBuildField() {
		if (build == null) {
			build = new TextField<String>();
			build.setFieldLabel("Build");
		}
		return build;
	}

	private CheckBox getMandatory() {
		if (mandatory == null) {
			mandatory = new CheckBox();
			mandatory.setFieldLabel("Mandatory");
		}
		return mandatory;
	}

	private boolean saveSoftwareDetails() {

		if (isValidFormData()) {
			LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
			Software softwareDetails = new Software();
			softwareDetails.setVersion(version.getValue());
			softwareDetails.setNotes(releaseNotes.getValue());
			softwareDetails.setReleaseDate(releaseDate.getValue());
			softwareDetails.setBuild(build.getValue());
			softwareDetails.setUrl(url.getValue());
			if (mandatory.getValue() == true) {
				softwareDetails.setMandatory("y");
			} else {
				softwareDetails.setMandatory("n");
			}
			service.addSoftwareDetails(softwareDetails, Registry.get("loggedUser").toString(), new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {

					if (result) {
						CloneSoftwareDialog.this.hide();
						MessageBox.alert("Success", "Software Cloned Successfully", null);
					} else {
						CloneSoftwareDialog.this.hide();
						MessageBox.alert("Faild", "Software clone faild", null);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					MessageBox.alert("Error", "Error occured while clone the software ", null);
				}
			});
			return true;
		}
		return false;
	}

	private TextField<String> getURLField() {
		if (url == null) {
			url = new TextField<String>();
			url.setFieldLabel("URL");
		}
		return url;
	}

	private boolean isValidFormData() {
		if (releaseNotes.getValue() != null && releaseNotes.getValue().trim().length() > 0 && version.getValue() != null
				&& version.getValue().trim().length() > 0 && build.getValue() != null && build.getValue().trim().length() > 0
				&& mandatory.getValue() != null && url.getValue() != null && url.getValue().trim().length() > 0) {
			return true;
		}
		MessageBox.alert("Failed to Clone", "All fields are mandatory", null);
		return false;
	}

}
