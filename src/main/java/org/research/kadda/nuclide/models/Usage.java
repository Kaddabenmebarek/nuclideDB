package org.research.kadda.nuclide.models;

import java.io.Serializable;

public class Usage implements Serializable{
	
	private static final long serialVersionUID = -7892997239234098652L;
	private int wasteId;
	private int tracerId;
	private int newTracerId;
	private String substanceName;
	private String amount;
	private String bioLabJournal;
	private String assayType;
	private String destination;
	private String usageDate;
	private String scientist;
	private Double solideWastePercentage;
	
	public Usage() {
		super();
	}

	public int getWasteId() {
		return wasteId;
	}
	public void setWasteId(int wasteId) {
		this.wasteId = wasteId;
	}
	public int getTracerId() {
		return tracerId;
	}
	public void setTracerId(int tracerId) {
		this.tracerId = tracerId;
	}
	public String getSubstanceName() {
		return substanceName;
	}
	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getUsageDate() {
		return usageDate;
	}
	public void setUsageDate(String usageDate) {
		this.usageDate = usageDate;
	}
	public String getScientist() {
		return scientist;
	}
	public void setScientist(String scientist) {
		this.scientist = scientist;
	}
	public int getNewTracerId() {
		return newTracerId;
	}
	public void setNewTracerId(int newTracerId) {
		this.newTracerId = newTracerId;
	}

	public Double getSolideWastePercentage() {
		return solideWastePercentage;
	}

	public void setSolideWastePercentage(Double solideWastePercentage) {
		this.solideWastePercentage = solideWastePercentage;
	}
	
	
}
