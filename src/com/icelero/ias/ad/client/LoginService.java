package com.icelero.ias.ad.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.icelero.ias.ad.shared.Client;
import com.icelero.ias.ad.shared.Config;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.Provision;
import com.icelero.ias.ad.shared.Software;
import com.icelero.ias.ad.shared.User;

@RemoteServiceRelativePath("data")
public interface LoginService extends RemoteService {
	User validateUser(User user);

	Provision getSearchResults(String msisdn);

	List<String> getClientIDS(String msisdn);

	Client getClientDetails(String clientID);

	List<Software> getSoftwareIDS();

	Software getSoftwareDetails(String softwareID);

	boolean deleteClientID(String clientID);

	boolean addSoftwareDetails(Software softwareDetails, String user);

	boolean updateSoftwareDetails(Software softwareDetails, String user);

	boolean deleteSoftwareID(String softwareID);

	List<Software> searchSoftwareID(String softwareID);

	List<Policy> getPolicyIDS();

	Policy getPolicyDetails(String policyID);

	boolean addPolicyDetails(Policy policyDetails, String user);

	boolean updatePolicyDetails(Policy policyDetails, String user);

	boolean deletePolicyID(String policyID);

	List<Policy> searchPolicyID(String policyID);

	boolean updateAccountState(String msisdn, String newState);

	boolean archiveSoftware(String softwareId);

	boolean setConfigDetails(Config config, String user);

	Config getConfigDetails(String configKey);

	List<Config> getConfigKeys();

	void Logout();

}
