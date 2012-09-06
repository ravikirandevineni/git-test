package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ClientID extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientID() {
	}
	public ClientID(String cid) {
		set("cid", cid);
	}
	
	public String getCid() {
		return (String) get("cid");
	}
}
