package com.icelero.ias.ad.client;

import java.util.List;

import com.icelero.ias.ad.shared.Client;
import com.icelero.ias.ad.shared.Config;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.Provision;
import com.icelero.ias.ad.shared.Software;
import com.icelero.ias.ad.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void validateUser(User user, AsyncCallback<User> callback);

	void getSearchResults(String msisdn, AsyncCallback<Provision> callback);

	void getClientIDS(String msisdn, AsyncCallback<List<String>> callback);

	void getClientDetails(String clientID, AsyncCallback<Client> callback);

	void getSoftwareIDS(AsyncCallback<List<Software>> callback);

	void getSoftwareDetails(String softwareID, AsyncCallback<Software> callback);

	void deleteClientID(String clientID, AsyncCallback<Boolean> callback);

	void addSoftwareDetails(Software softwareDetails, String user, AsyncCallback<Boolean> callback);

	void updateSoftwareDetails(Software softwareDetails, String user, AsyncCallback<Boolean> callback);

	void deleteSoftwareID(String softwareID, AsyncCallback<Boolean> callback);

	void searchSoftwareID(String softwareID, AsyncCallback<List<Software>> callback);

	void getPolicyIDS(AsyncCallback<List<Policy>> callback);

	void getPolicyDetails(String policyID, AsyncCallback<Policy> callback);

	void addPolicyDetails(Policy policyDetails, String user, AsyncCallback<Boolean> callback);

	void updatePolicyDetails(Policy policyDetails, String user, AsyncCallback<Boolean> callback);

	void deletePolicyID(String policyID, AsyncCallback<Boolean> callback);

	void searchPolicyID(String policyID, AsyncCallback<List<Policy>> callback);

	void updateAccountState(String msisdn, String newState, AsyncCallback<Boolean> callback);

	void archiveSoftware(String softwareId, AsyncCallback<Boolean> callback);

	void setConfigDetails(Config config, String user, AsyncCallback<Boolean> callback);

	void getConfigDetails(String configKey, AsyncCallback<Config> callback);

	void getConfigKeys(AsyncCallback<List<Config>> callback);

	void Logout(AsyncCallback<Void> callback);

}
