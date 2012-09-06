package com.icelero.ias.ad.client;


import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.utilities.PrefetchContentTypeUtilities;
import com.icelero.ias.ad.shared.utilities.TimeUtilities;

public class AddPolicyDialog extends Dialog {
	
	private FormPanel addPolicyPanel;
	private TextField<String> policyDescription;
	private TextField<String> name;
	private Button saveButton;
	private Button resetButton;
	private Button cancelButton;
	private ButtonBar saveResetCancelButtonbar;
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
	
	
	public AddPolicyDialog() {
		setLayout(new FitLayout());
		setProperties();
		add(getAddpolicyPanel());
	}

	private void setProperties() {
		setHeading("Add Policy");
		setButtons("");
		setModal(true);
		setResizable(false);
		setBodyBorder(true);
		setWidth(630);
		setHeight(400);
	}

	private FormPanel getAddpolicyPanel() {
		if (addPolicyPanel == null) {
			addPolicyPanel = new FormPanel();
			addPolicyPanel.setHeaderVisible(false);
			addPolicyPanel.setBorders(false);
			addPolicyPanel.setBodyBorder(false);
			addPolicyPanel.setLabelWidth(230);
			addPolicyPanel.setButtonAlign(HorizontalAlignment.CENTER);
			addPolicyPanel.add(getNameField(), new FormData("100%"));
			addPolicyPanel.add(getDescriptionField(), new FormData("100%"));
			addPolicyPanel.add(getSaveResetCacelButtonbar());
			ContentPanel innerComboxPanel = new ContentPanel();
			innerComboxPanel.setLayout(new FitLayout());
			innerComboxPanel.setHeaderVisible(false);
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
			innerComboxPanel.add(flexTable);
			addPolicyPanel.add(innerComboxPanel);
			addPolicyPanel.add(getSaveResetCacelButtonbar());
		}
		return addPolicyPanel;
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

	private Button getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new Button("Cancel");
			cancelButton.setWidth(70);
			cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					MessageBox.confirm("Confirm", "Do you want to save changes?", new Listener<MessageBoxEvent>() {
						@Override
						public void handleEvent(MessageBoxEvent be) {
							Button btn = be.getButtonClicked();
							if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {
								if (savePolicyDetails()) {
									AddPolicyDialog.this.hide();
								}
							} else if (Dialog.NO.equalsIgnoreCase(btn.getItemId())) {
								AddPolicyDialog.this.hide();
							}
						}
					});

				}
			});

		}
		return cancelButton;
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("Reset");
			resetButton.setWidth(70);
			resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					resetFieldValues();
				}
			});
		}
		return resetButton;
	}

	private void resetFieldValues() {

		policyDescription.setValue("");
		name.setValue("");

	}

	private Button getSaveButton() {
		if (saveButton == null) {
			saveButton = new Button("Save");
			saveButton.setWidth(70);
			saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					savePolicyDetails();
				}
			});
		}
		return saveButton;
	}

	private TextField<String> getDescriptionField() {
		if (policyDescription == null) {
			policyDescription = new TextField<String>();
			policyDescription.setFieldLabel("Description");
		}
		return policyDescription;
	}

	private TextField<String> getNameField() {
		if (name == null) {
			name = new TextField<String>();
			name.setFieldLabel("Name");
		}
		return name;
	}

	private boolean savePolicyDetails() {

		if (isValidFormData()) {
			LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
			Policy policyDetails = new Policy();
			policyDetails.setName(name.getValue());
			policyDetails.setDescription(policyDescription.getValue());
			policyDetails.setPrefetchContentType(prefetch.getSimpleValue());
			policyDetails.setMediaServerStartTime(TimeUtilities.setHoursToSeconds(mediaServerStartHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(mediaServerStartMM.getSimpleValue()));
			policyDetails.setMediaServerStopTime(TimeUtilities.setHoursToSeconds(mediaServerStopHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(mediaServerStopMM.getSimpleValue()));
			policyDetails.setPrefetchStartTime(TimeUtilities.setHoursToSeconds(prefetchStartHH.getSimpleValue())
					+ TimeUtilities.setMinutesToSeconds(prefetchStartMM.getSimpleValue()));
			policyDetails.setPrefetchStopTime(TimeUtilities.setHoursToSeconds(prefetchStopHH.getSimpleValue()));
			policyDetails.setBandwidth((Integer) bandwidth.getValue());
			service.addPolicyDetails(policyDetails, Registry.get("loggedUser").toString(), new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						AddPolicyDialog.this.hide();
						MessageBox.alert("Success", "New policy added successfully", null);
					} else {
						MessageBox.alert("Faild", "Faild to add the software", null);
						AddPolicyDialog.this.hide();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					MessageBox.alert("Error", "Failed to add the new policy", null);

				}
			});
			return true;
		}

		return false;
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

	private boolean isValidFormData() {
		boolean status = false;
		if (name.getValue() != null && name.getValue().trim().length() > 0) {
			status = true;
		} else {
			status = false;
			MessageBox.alert("Failed", "Please enter Name", null);
		}

		if (status) {
			if (policyDescription.getValue() != null && policyDescription.getValue().trim().length() > 0) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter Description", null);
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
				MessageBox.alert("Failed", "Please enter valid Media Server Start Time", null);
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
				MessageBox.alert("Failed", "Please enter valid Media Server Stop Time", null);
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
				MessageBox.alert("Failed", "Please enter valid Prefetch Start Time", null);
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
				MessageBox.alert("Failed", "Please enter valid Prefetch Stop Time", null);
			}
		}

		if (status) {
			if (bandwidth.getValue() != null) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please enter valid Bandwidth", null);
			}
		}

		if (status) {
			if (prefetch.getSimpleValue() != null && prefetch.getSimpleValue().trim().length() > 0
					&& PrefetchContentTypeUtilities.isValidContentType(prefetch.getSimpleValue())) {
				status = true;
			} else {
				status = false;
				MessageBox.alert("Failed", "Please select valid Content Type", null);
			}
		}

		if (status) {

			if ((TimeUtilities.setHoursToSeconds(prefetchStopHH.getSimpleValue()) + TimeUtilities.setMinutesToSeconds(prefetchStopMM
					.getSimpleValue())) > (TimeUtilities.setHoursToSeconds(prefetchStartHH.getSimpleValue()) + TimeUtilities
					.setMinutesToSeconds(prefetchStartMM.getSimpleValue()))) {
				status = true;

			} else {
				status = false;
				MessageBox.alert("Failed", "Prefetch Stop Time should be greater than Prefetch Start Time", null);
			}

		}

		if (status) {

			if ((TimeUtilities.setHoursToSeconds(mediaServerStopHH.getSimpleValue()) + TimeUtilities.setMinutesToSeconds(mediaServerStopMM
					.getSimpleValue())) > (TimeUtilities.setHoursToSeconds(mediaServerStartHH.getSimpleValue()) + TimeUtilities
					.setMinutesToSeconds(mediaServerStartMM.getSimpleValue()))) {
				status = true;

			} else {
				status = false;
				MessageBox.alert("Failed", "Mediaserver Stop Time should be greater than Start Time", null);
			}
		}

		return status;

	}

}
