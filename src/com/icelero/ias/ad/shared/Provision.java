package com.icelero.ias.ad.shared;

import java.io.Serializable;
import java.util.List;

public class Provision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msisdn;
	private String state;
	private String policyid;
	private Policy policy;
	private List<String> clientIDS;

	public Provision() {

	}

	public Provision(String msisdn, String state, String policyid) {
		this.msisdn = msisdn;
		this.state = msisdn;
		this.policyid = policyid;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPolicyid() {
		return policyid;
	}

	public void setPolicyid(String policyid) {
		this.policyid = policyid;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public List<String> getClientIDS() {
		return clientIDS;
	}

	public void setClientIDS(List<String> clientIDS) {
		this.clientIDS = clientIDS;
	}

}
