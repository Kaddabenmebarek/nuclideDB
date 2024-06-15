package org.research.kadda.nuclide.models;

import java.io.Serializable;

public class NuclideUserLogin implements Serializable {
	
	private static final long serialVersionUID = -2530213325796066806L;
	private String userId;
	private String password;
	
	public NuclideUserLogin() {
		super();
	}
	
	public NuclideUserLogin(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
