package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.WidgetComponent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppView extends View {
	private Viewport viewport;
	private ContentPanel northPanel;
	private TabPanel centerPanel;
	private Image logo;
	private Button logoutButton;
	private ToolBar northToolBar;

	public AppView(Controller controller) {
		super(controller);
	}

	protected void initialize() {
		LoginDialog dialog = new LoginDialog();
		dialog.setClosable(false);
		dialog.addListener(Events.Hide, new Listener<WindowEvent>() {
			public void handleEvent(WindowEvent be) {
				// Window.alert(Cookies.getCookie("JSESSIONID"));
				initUI();
			}
		});
		dialog.show();
	}

	private void initUI() {
		if (viewport == null) {
			viewport = new Viewport();
			viewport.setMonitorWindowResize(true);
			viewport.setLayout(new BorderLayout());
			BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 55);
			BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, 800);
			viewport.add(getNorthWidget(), northData);
			viewport.add(getCenterWidget(), centerData);
		}
		RootPanel.get().add(viewport);
	}

	private Widget getNorthWidget() {
		northPanel = new ContentPanel();
		northPanel.setHeaderVisible(false);
		northPanel.setBodyBorder(false);
		northPanel.setBorders(false);
		northPanel.add(getNorthToolBar());
		return northPanel;
	}

	private TabPanel getCenterWidget() {

		if (centerPanel == null) {
			centerPanel = new TabPanel();
			TabItem userDetails = new TabItem("Account Manager");
			TabItem softwareDetails = new TabItem("Software Manager");
			TabItem policyDetails = new TabItem("Policy Manager");
			TabItem configDetails = new TabItem("Config");
			centerPanel.add(userDetails);
			centerPanel.add(softwareDetails);
			centerPanel.add(policyDetails);
			centerPanel.add(configDetails);
			userDetails.add(new AccountPanel());
			softwareDetails.add(new SoftwarePanel());
			policyDetails.add(new PolicyPanel());
			configDetails.add(new ConfigPanel());
		}

		return centerPanel;
	}

	private Label getWelcomeText() {
		Label label = new Label("Welcome " + Registry.get("loggedUser"));
		return label;
	}

	private Button getLogoutButton() {
		if (logoutButton == null) {
			logoutButton = new Button("Logout");
			logoutButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					MessageBox.confirm("Confirm", "Are you sure you want to logout?", new Listener<MessageBoxEvent>() {
						@Override
						public void handleEvent(MessageBoxEvent be) {
							Button btn = be.getButtonClicked();
							if (Dialog.YES.equalsIgnoreCase(btn.getItemId())) {

								LoginServiceAsync service = (LoginServiceAsync) Registry.get(Main.SERVICE);
								service.Logout(new AsyncCallback<Void>() {
									@Override
									public void onSuccess(Void result) {
										Window.Location.reload();
									}

									@Override
									public void onFailure(Throwable caught) {

									}
								});

							}
						}
					});
				}
			});
		}
		return logoutButton;
	}

	private Image getLogo() {
		if (logo == null) {
			logo = new Image("icelero_logo.png");
		}
		return logo;
	}

	private ToolBar getNorthToolBar() {
		if (northToolBar == null) {
			northToolBar = new ToolBar();
			northToolBar.setHeight(100);
			northToolBar.setBorders(false);
			northToolBar.add(new WidgetComponent(getLogo()));
			northToolBar.add(new FillToolItem());
			northToolBar.add(new WidgetComponent(getWelcomeText()));
			northToolBar.add(new SeparatorToolItem());
			northToolBar.add(getLogoutButton());
		}
		return northToolBar;
	}

	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type.getEventCode() == AppEvents.search.getEventCode()) {

		}
	}

}
