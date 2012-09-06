package com.icelero.ias.ad.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ConfigKeys extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6760390522289243056L;

	public ConfigKeys() {
	}

	public ConfigKeys(String key, String value) {
		set("key", key);
		set("value", value);
	}

	public String getKey() {
		return (String) get("key");
	}

	public String getValue() {
		return (String) get("value");
	}

}
