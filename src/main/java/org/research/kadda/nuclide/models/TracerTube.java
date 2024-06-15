package org.research.kadda.nuclide.models;

import java.util.Date;

public class TracerTube {

	private String nuclideName;
	private String userId;
	private String substanceName;
	private String batchName;
	private char state;
	private double initialActivity;
	private double initialAmount;
	private Date initialActivityDate;
	private String activityDate;
	private String location;
	
	
	public String getNuclideName() {
		return nuclideName;
	}
	public void setNuclideName(String nuclideName) {
		this.nuclideName = nuclideName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSubstanceName() {
		return substanceName;
	}
	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	public double getInitialActivity() {
		return initialActivity;
	}
	public void setInitialActivity(double initialActivity) {
		this.initialActivity = initialActivity;
	}
	public double getInitialAmount() {
		return initialAmount;
	}
	public void setInitialAmount(double initialAmount) {
		this.initialAmount = initialAmount;
	}
	public Date getInitialActivityDate() {
		return initialActivityDate;
	}
	public void setInitialActivityDate(Date initialActivityDate) {
		this.initialActivityDate = initialActivityDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}
	
	
	
}
