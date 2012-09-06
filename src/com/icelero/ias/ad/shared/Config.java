package com.icelero.ias.ad.shared;

import java.io.Serializable;

public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6039450929074864247L;
	private int id;
	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
