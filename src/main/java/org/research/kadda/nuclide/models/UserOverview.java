package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.util.List;

public class UserOverview implements Serializable{

	private static final long serialVersionUID = 5707079138636644250L;
	private String userId;
	private String user;
	private String firstName;
	private String lastName;
	private String nuclide;
	private String lastUsageDate;
	private String usageDate;
	private String bioLabJournal;
	private String assayType;
	private String isActive;
	private String inLocation;
	private String isInactiveRadio;
	private String role;
	private List<String> userIdS;
	private String generik;
	
	public UserOverview() {
		super();
	}
	
	public UserOverview(String user, String userId, String nuclide, String lastUsageDate, String usageDate, String role) {
		super();
		this.user = user;
		this.userId = userId;
		this.nuclide = nuclide;
		this.lastUsageDate = lastUsageDate;
		this.usageDate = usageDate;
		this.role = role;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNuclide() {
		return nuclide;
	}

	public void setNuclide(String nuclide) {
		this.nuclide = nuclide;
	}

	public String getLastUsageDate() {
		return lastUsageDate;
	}

	public void setLastUsageDate(String lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}

	public String getBioLabJournal() {
		return bioLabJournal;
	}

	public void setBioLabJournal(String bioLabJournal) {
		this.bioLabJournal = bioLabJournal;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public String getUsageDate() {
		return usageDate;
	}
	public void setUsageDate(String usageDate) {
		this.usageDate = usageDate;
	}
	
	public String getInLocation() {
		return inLocation;
	}

	public void setInLocation(String inLocation) {
		this.inLocation = inLocation;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsInactiveRadio() {
		return isInactiveRadio;
	}

	public void setIsInactiveRadio(String isInactiveRadio) {
		this.isInactiveRadio = isInactiveRadio;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public List<String> getUserIdS() {
		return userIdS;
	}

	public void setUserIdS(List<String> userIdS) {
		this.userIdS = userIdS;
	}
	
	public String getGenerik() {
		return generik;
	}

	public void setGenerik(String generik) {
		this.generik = generik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assayType == null) ? 0 : assayType.hashCode());
		result = prime * result + ((bioLabJournal == null) ? 0 : bioLabJournal.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((isInactiveRadio == null) ? 0 : isInactiveRadio.hashCode());
		result = prime * result + ((lastUsageDate == null) ? 0 : lastUsageDate.hashCode());
		result = prime * result + ((nuclide == null) ? 0 : nuclide.hashCode());
		result = prime * result + ((usageDate == null) ? 0 : usageDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserOverview other = (UserOverview) obj;
		if (assayType == null) {
			if (other.assayType != null)
				return false;
		} else if (!assayType.equals(other.assayType))
			return false;
		if (bioLabJournal == null) {
			if (other.bioLabJournal != null)
				return false;
		} else if (!bioLabJournal.equals(other.bioLabJournal))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (isInactiveRadio == null) {
			if (other.isInactiveRadio != null)
				return false;
		} else if (!isInactiveRadio.equals(other.isInactiveRadio))
			return false;
		if (lastUsageDate == null) {
			if (other.lastUsageDate != null)
				return false;
		} else if (!lastUsageDate.equals(other.lastUsageDate))
			return false;
		if (nuclide == null) {
			if (other.nuclide != null)
				return false;
		} else if (!nuclide.equals(other.nuclide))
			return false;
		if (usageDate == null) {
			if (other.usageDate != null)
				return false;
		} else if (!usageDate.equals(other.usageDate))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
