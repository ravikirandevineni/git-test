package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class SoftwareID extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SoftwareID() {
	}

	public SoftwareID(String sid,String sversion) {
		set("sid", sid);
		set("sversion",sversion);
	}

	public String getSid() {
		return (String) get("sid");
	}

	public String getVersion() {
		return (String) get("sversion");
	}

}
