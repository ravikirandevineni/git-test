package com.icelero.ias.ad.server;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.icelero.ias.ad.client.LoginService;
import com.icelero.ias.ad.shared.Client;
import com.icelero.ias.ad.shared.Config;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.Provision;
import com.icelero.ias.ad.shared.Software;
import com.icelero.ias.ad.shared.User;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	private static final long serialVersionUID = 1L;
	private ProvisionDAO dao = null;

	public LoginServiceImpl() {
		dao = new ProvisionDAO();
	}

	@Override
	public User validateUser(User user) {
		User loggedUser = dao.isValidUser(user);
		if (loggedUser != null) {
			this.getThreadLocalRequest().getSession().getId();
		}
		return loggedUser;
	}

	@Override
	public Provision getSearchResults(String imsi) {
		return dao.getSearchResult(imsi);
	}

	@Override
	public List<String> getClientIDS(String imsi) {
		return dao.getClientIDS(imsi);
	}

	@Override
	public Client getClientDetails(String clientID) {
		return dao.getClientDetails(clientID);
	}

	@Override
	public void Logout() {
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public List<Software> getSoftwareIDS() {
		return dao.getSoftwareIDS();
	}

	@Override
	public Software getSoftwareDetails(String softwareID) {
		return dao.getSoftwareDetails(softwareID);
	}

	@Override
	public boolean deleteClientID(String clientID) {
		return dao.deleteClientID(clientID);
	}

	@Override
	public boolean addSoftwareDetails(Software softwareDetails, String user) {
		softwareDetails.setSoftwareID(UUID.randomUUID().toString());
		return dao.addSoftwareDetails(softwareDetails, user);
	}

	@Override
	public boolean updateSoftwareDetails(Software softwareDetails, String user) {
		return dao.updateSoftwareDetails(softwareDetails, user);
	}

	@Override
	public boolean deleteSoftwareID(String softwareID) {
		return dao.deleteSoftwareID(softwareID);
	}

	@Override
	public List<Software> searchSoftwareID(String softwareID) {
		return dao.searchSoftwareID(softwareID);
	}

	@Override
	public List<Policy> getPolicyIDS() {
		return dao.getPolicyIDS();
	}

	@Override
	public Policy getPolicyDetails(String policyID) {
		return dao.getPolicyDetails(policyID);
	}

	@Override
	public boolean addPolicyDetails(Policy policyDetails, String user) {
		policyDetails.setPolicyID(UUID.randomUUID().toString());
		return dao.addPolicyDetails(policyDetails, user);
	}

	@Override
	public boolean updatePolicyDetails(Policy policyDetails, String user) {
		return dao.updatePolicyDetails(policyDetails, user);
	}

	@Override
	public boolean deletePolicyID(String policyID) {
		return dao.deletePolicyID(policyID);
	}

	@Override
	public List<Policy> searchPolicyID(String policyID) {
		return dao.searchPolicyID(policyID);
	}

	@Override
	public boolean updateAccountState(String msisdn, String newState) {
		return dao.updateAccountState(msisdn, newState);
	}

	@Override
	public boolean archiveSoftware(String softwareID) {
		return dao.archiveSoftware(softwareID);
	}

	@Override
	public boolean setConfigDetails(Config config, String user) {
		return dao.setConfigDetails(config, user);
	}

	@Override
	public List<Config> getConfigKeys() {
		return dao.getConfigKeys();
	}

	@Override
	public Config getConfigDetails(String configKey) {
		return dao.getConfigDetails(configKey);
	}
}
