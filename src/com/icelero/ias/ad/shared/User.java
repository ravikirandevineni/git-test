package com.icelero.ias.ad.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class User implements IsSerializable {
	
	private int ID;
	private String name;
	private String password;

	public User() {}
		
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

	
}
