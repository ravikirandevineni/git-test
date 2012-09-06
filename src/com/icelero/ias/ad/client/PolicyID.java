package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class PolicyID extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PolicyID() {
	}

	public PolicyID(String pid,String pname) {
		set("pid", pid);
		set("pname", pname);
	}

	public String getPid() {
		return (String) get("pid");
	}
	
	public String getPName() {
		return (String) get("pname");
	}
}
