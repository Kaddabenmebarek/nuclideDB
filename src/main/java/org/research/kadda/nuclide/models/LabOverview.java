package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.util.List;

public class LabOverview implements Serializable {

	private static final long serialVersionUID = 5234165991893121274L;

	private int id;
	private String locationName;
	private String locationType;
	private String nuclide;
	private String activityThreshold;
	private String wastesActivity;
	private String openWastesActivity;
	private String closedWastesActivity;
	private String tracersActivity;
	private String totalActivity;
	private List<WasteOverview> wastesList;
	private List<TracerOverview> tracersList;
	private String hightlight;
	private String isNewLab;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getActivityThreshold() {
		return activityThreshold;
	}

	public void setActivityThreshold(String activityThreshold) {
		this.activityThreshold = activityThreshold;
	}

	public String getOpenWastesActivity() {
		return openWastesActivity;
	}

	public void setOpenWastesActivity(String openWastesActivity) {
		this.openWastesActivity = openWastesActivity;
	}

	public String getClosedWastesActivity() {
		return closedWastesActivity;
	}

	public void setClosedWastesActivity(String closedWastesActivity) {
		this.closedWastesActivity = closedWastesActivity;
	}

	public String getWastesActivity() {
		return wastesActivity;
	}

	public void setWastesActivity(String wastesActivity) {
		this.wastesActivity = wastesActivity;
	}

	public String getTracersActivity() {
		return tracersActivity;
	}

	public void setTracersActivity(String tracersActivity) {
		this.tracersActivity = tracersActivity;
	}

	public String getTotalActivity() {
		return totalActivity;
	}

	public void setTotalActivity(String totalActivity) {
		this.totalActivity = totalActivity;
	}

	public List<WasteOverview> getWastesList() {
		return wastesList;
	}

	public void setWastesList(List<WasteOverview> wastesList) {
		this.wastesList = wastesList;
	}

	public List<TracerOverview> getTracersList() {
		return tracersList;
	}

	public void setTracersList(List<TracerOverview> tracersList) {
		this.tracersList = tracersList;
	}

	public String getHightlight() {
		return hightlight;
	}

	public void setHightlight(String hightlight) {
		this.hightlight = hightlight;
	}

	public String getIsNewLab() {
		return isNewLab;
	}

	public void setIsNewLab(String isNewLab) {
		this.isNewLab = isNewLab;
	}

	public void setNuclide(String nuclide) {
		this.nuclide = nuclide;
	}

	public String getNuclide() {
		return nuclide;
	}
	
}
