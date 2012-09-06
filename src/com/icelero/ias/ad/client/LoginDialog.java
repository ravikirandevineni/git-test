package com.icelero.ias.ad.client;


import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.icelero.ias.ad.shared.User;

public class LoginDialog extends Dialog {

	private FormPanel formPanel;
	private TextField<String> userName;
	private TextField<String> password;
	private KeyListener enterKeyListener;
	private Button loginButton;
	private Button resetButton;

	public LoginDialog() {
		setLayout(new FitLayout());
		setProperties();
		add(getFormPanel());
		setFocusWidget(getUserNameField());
	}

	private void setProperties() {
		setAnimCollapse(true);
		setButtons("");
		setHeading("Login");
		setModal(true);
		setResizable(false);
		setBodyBorder(true);
		setWidth(350);
		setHeight(200);
	}

	private FormPanel getFormPanel() {
		if (formPanel == null) {
			formPanel = new FormPanel();
			formPanel.setHeaderVisible(false);
			formPanel.setBorders(false);
			formPanel.setBodyBorder(false);
			formPanel.setLabelWidth(100);
			formPanel.setButtonAlign(HorizontalAlignment.CENTER);
			Image image = new Image("icelero_logo.png");
			formPanel.add(image);
			formPanel.add(getUserNameField(), new FormData("100%"));
			formPanel.add(getPasswordField(), new FormData("100%"));
			formPanel.addButton(getLoginButton());
			formPanel.addButton(getResetButton());
		}
		return formPanel;
	}

	private TextField<String> getPasswordField() {
		if (password == null) {
			password = new TextField<String>();
			password.setPassword(true);
			password.setFieldLabel("Password");
			password.addKeyListener(getEnterKeyListener());
		}
		return password;
	}

	private TextField<String> getUserNameField() {
		if (userName == null) {
			userName = new TextField<String>();
			userName.setFieldLabel("Username");
			userName.addKeyListener(getEnterKeyListener());
		}
		return userName;
	}

	private Button getResetButton() {
		if (resetButton == null) {
			resetButton = new Button("reset");
			resetButton.setWidth(70);
			resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				public void componentSelected(ButtonEvent ce) {
					userName.reset();
					password.reset();
					userName.focus();
				}
			});
		}
		return resetButton;
	}

	private Button getLoginButton() {
		if (loginButton == null) {
			loginButton = new Button("login");
			loginButton.setWidth(70);
			loginButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				public void componentSelected(ButtonEvent ce) {
					if (isValidFormData())
						onFormSubmit();
				}
			});
		}
		return loginButton;
	}

	private KeyListener getEnterKeyListener() {
		if (enterKeyListener == null) {
			enterKeyListener = new KeyListener() {
				public void componentKeyDown(ComponentEvent event) {
					if (event.getKeyCode() == 13) {
						if (isValidFormData())
							onFormSubmit();
					}
				}
			};
		}
		return enterKeyListener;
	}

	private boolean isValidFormData() {
		if (userName.getValue() != null && userName.getValue().trim().length() > 0 && password.getValue() != null
				&& password.getValue().trim().length() > 0) {
			return true;
		}
		MessageBox.alert("Failed to login", "User Name or Password is empty", null);
		return false;
	}

	private void onFormSubmit() {
		LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
		User user = new User();
		user.setName(userName.getValue());
		user.setPassword(password.getValue());
		service.validateUser(user, new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MessageBox.alert("Error", "Enter valid user name and password!!", null);
				userName.reset();
				password.reset();
				userName.focus();
			}

			@Override
			public void onSuccess(User currentUser) {
				if (currentUser != null) {
					Registry.register("loggedUser", currentUser.getName());
					LoginDialog.this.hide();
				} else {
					MessageBox.alert("Failed to login", "Enter valid user name and password!!", null);
				}
			}
		});
	}

}
