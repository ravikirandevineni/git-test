package com.icelero.ias.ad.shared;

import java.io.Serializable;

public class Policy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1803126821374092035L;
	
	private String policyID;
	private String description;
	private String name;
	private String policy;
	private int	 mediaServerStartTime;
	private int	 mediaServerStopTime;
	private int	 prefetchStartTime;
	private int  prefetchStopTime;
	private int bandwidth;
	private String prefetchContentType;
	
	
	public String getPolicyID() {
		return policyID;
	}

	public void setPolicyID(String policyID) {
		this.policyID = policyID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public int getMediaServerStartTime() {
		return mediaServerStartTime;
	}

	public void setMediaServerStartTime(int mediaServerStartTime) {
		this.mediaServerStartTime = mediaServerStartTime;
	}

	public int getMediaServerStopTime() {
		return mediaServerStopTime;
	}

	public void setMediaServerStopTime(int mediaServerStopTime) {
		this.mediaServerStopTime = mediaServerStopTime;
	}

	public int getPrefetchStartTime() {
		return prefetchStartTime;
	}

	public void setPrefetchStartTime(int prefetchStartTime) {
		this.prefetchStartTime = prefetchStartTime;
	}

	public int getPrefetchStopTime() {
		return prefetchStopTime;
	}

	public void setPrefetchStopTime(int prefetchStopTime) {
		this.prefetchStopTime = prefetchStopTime;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getPrefetchContentType() {
		return prefetchContentType;
	}

	public void setPrefetchContentType(String prefetchContentType) {
		this.prefetchContentType = prefetchContentType;
	}

}
