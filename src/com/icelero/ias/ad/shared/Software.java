package com.icelero.ias.ad.shared;

import java.io.Serializable;
import java.util.Date;

public class Software implements Serializable {

	private static final long serialVersionUID = 3783186606782000335L;
	private String softwareID;
	private String version;
	private String notes;
	private String build;
	private String mandatory;
	private Date releaseDate;
	private Date currentDate;
	private String url;

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSoftwareID() {
		return softwareID;
	}

	public void setSoftwareID(String softwareID) {
		this.softwareID = softwareID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public Date getCurrentDate() {
		if (currentDate == null) {
			currentDate = new Date();
		}
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
