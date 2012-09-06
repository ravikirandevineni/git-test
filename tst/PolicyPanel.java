package in.sample.app.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PolicyPanel extends ContentPanel {

	private FormPanel policyDetailsPanel;
	private TabPanel policyRecordsTab;
	private ContentPanel recentDataPanel;
	private ContentPanel serachPanelTab;
	private ContentPanel searchDataPanel;
	private ContentPanel searchResult;
	ButtonBar newCloneDeleteButtonbar;
	ButtonBar saveResetButtonbar;
	Button saveButton;
	Button resetButton;

	public PolicyPanel() {
		initialize();
	}

	private void initialize() {

		setLayout(new BorderLayout());
		setHeight(520);
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 720);
		eastData.setMargins(new Margins(5));
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 600);
		westData.setMargins(new Margins(5));
		add(getPolicyDetailsPanel(), eastData);
		add(getPolicyRecordDetailsPanel(), westData);
	}

	private Widget getPolicyRecordDetailsPanel() {
		if (policyRecordsTab == null) {
			policyRecordsTab = new TabPanel();
			TabItem recent = new TabItem("Recent");
			TabItem search = new TabItem("Search");
			policyRecordsTab.add(recent);
			policyRecordsTab.add(search);
			recent.add(getRecentRecordsPanel());
			search.add(getSearchPanel());
		}
		return policyRecordsTab;
	}

	private Widget getSearchPanel() {

		if (serachPanelTab == null) {
			serachPanelTab = new ContentPanel();
			serachPanelTab.setHeading("PolicyID's");
			serachPanelTab.setHeight(460);
			serachPanelTab.setHeaderVisible(false);
			serachPanelTab.setLayout(new BorderLayout());
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
			searchResult.setLayout(new RowLayout());
			searchResult.setHeading("SearchResults");
			policyDetailsPanel.setHeight(460);
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(new Label("1231231"));
			searchResult.add(getNewCloneDeleteButtonbar());
		}
		return searchResult;
	}

	private Widget getSearchTabPanel() {
		if (searchDataPanel == null) {
			searchDataPanel = new ContentPanel();
			searchDataPanel.setHeaderVisible(false);
			searchDataPanel.setLayout(new ColumnLayout());
			searchDataPanel.add(new TextField<String>(), new ColumnData(.6));
			searchDataPanel.add(new Button("Search"), new ColumnData(.1));
		}
		return searchDataPanel;
	}

	private Widget getRecentRecordsPanel() {
		if (recentDataPanel == null) {
			recentDataPanel = new ContentPanel();
			recentDataPanel.setHeight(460);
			recentDataPanel.setHeading("PolicyID's");
			recentDataPanel.setLayout(new RowLayout());
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(new Label("13331"));
			recentDataPanel.add(getNewCloneDeleteButtonbar());
		}
		return recentDataPanel;
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
		if (resetButton == null) {
			resetButton = new Button("reset");
		}
		return resetButton;
	}

	private Button getResetButton() {
		if (saveButton == null) {
			saveButton = new Button("save");
		}
		return saveButton;
	}

	private ButtonBar getNewCloneDeleteButtonbar() {
		if (newCloneDeleteButtonbar == null) {
			newCloneDeleteButtonbar = new ButtonBar();
			newCloneDeleteButtonbar.add(new Button("New"));
			newCloneDeleteButtonbar.add(new Button("Clone"));
			newCloneDeleteButtonbar.add(new Button("Delete"));
		}
		return newCloneDeleteButtonbar;
	}

	private Widget getPolicyDetailsPanel() {
		if (policyDetailsPanel == null) {
			policyDetailsPanel = new FormPanel();
			policyDetailsPanel.setHeight(460);
			policyDetailsPanel.setHeading("PolicyDetails");
			TextField<String> policyID = new TextField<String>();
			policyID.setFieldLabel("ID");
			TextField<String> name = new TextField<String>();
			name.setFieldLabel("Policy Name");
			TextField<String> description = new TextField<String>();
			description.setFieldLabel("Description");
			TextField<String> p1 = new TextField<String>();
			p1.setFieldLabel("P1");
			TextField<String> p2 = new TextField<String>();
			p2.setFieldLabel("P2");
			policyDetailsPanel.add(policyID, new FormData("100%"));
			policyDetailsPanel.add(name, new FormData("100%"));
			policyDetailsPanel.add(description, new FormData("100%"));
			policyDetailsPanel.add(p1, new FormData("100%"));
			policyDetailsPanel.add(p2, new FormData("100%"));
			policyDetailsPanel.add(saveResetButtonbar());
		}
		return policyDetailsPanel;
	}
}
