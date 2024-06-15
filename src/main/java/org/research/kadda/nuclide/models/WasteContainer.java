package org.research.kadda.nuclide.models;


import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class WasteContainer implements Serializable {

	private static final long serialVersionUID = -4675576870457080216L;
	@NotEmpty
	private String nuclideName;
	private char state;
	private String location;
	private String userId;
	
	public String getNuclideName() {
		return nuclideName;
	}
	public void setNuclideName(String nuclideName) {
		this.nuclideName = nuclideName;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
