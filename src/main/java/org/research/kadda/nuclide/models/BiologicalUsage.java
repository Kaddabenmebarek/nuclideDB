package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BiologicalUsage implements Serializable{

	private static final long serialVersionUID = 4461056364674479275L;
	private String newUsageUserId;
	private String tracerTubeConcat;
	private double amountTaken;
	private String biologicalLabJournal;
	private String assayType;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)	
	private Date usageDate;
	private String recordUsageDate;
	private String liquidWasteConcat;
	private String solidWasteConcat;	
	private int solidWastePercentage;
	private int liquidWastePercentage;
	private double newTotalVolume;
	private String newBatchName;
	private String materialToSend;
	private double totalSampleVolume;
	private String sampleLocation;
	private String location;
	private String generik;
	private String treeNodeId;
	private Double treeYPosition;
	private String messageToDisplay;
	
	public String getNewUsageUserId() {
		return newUsageUserId;
	}
	public void setNewUsageUserId(String newUsageuserId) {
		this.newUsageUserId = newUsageuserId;
	}
	public String getTracerTubeConcat() {
		return tracerTubeConcat;
	}
	public void setTracerTubeConcat(String tracerTubeConcat) {
		this.tracerTubeConcat = tracerTubeConcat;
	}
	public double getAmountTaken() {
		return amountTaken;
	}
	public void setAmountTaken(double amountTaken) {
		this.amountTaken = amountTaken;
	}
	public String getBiologicalLabJournal() {
		return biologicalLabJournal;
	}
	public void setBiologicalLabJournal(String biologicalLabJournal) {
		this.biologicalLabJournal = biologicalLabJournal;
	}
	public String getAssayType() {
		return assayType;
	}
	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}
	public Date getUsageDate() {
		return usageDate;
	}
	public void setUsageDate(Date usageDate) {
		this.usageDate = usageDate;
	}
	public String getRecordUsageDate() {
		return recordUsageDate;
	}
	public void setRecordUsageDate(String recordUsageDate) {
		this.recordUsageDate = recordUsageDate;
	}
	public String getLiquidWasteConcat() {
		return liquidWasteConcat;
	}
	public void setLiquidWasteConcat(String liquidWasteConcat) {
		this.liquidWasteConcat = liquidWasteConcat;
	}
	public String getSolidWasteConcat() {
		return solidWasteConcat;
	}
	public void setSolidWasteConcat(String solidWasteConcat) {
		this.solidWasteConcat = solidWasteConcat;
	}
	public int getSolidWastePercentage() {
		return solidWastePercentage;
	}
	public void setSolidWastePercentage(int solidWastePercentage) {
		this.solidWastePercentage = solidWastePercentage;
	}
	public double getNewTotalVolume() {
		return newTotalVolume;
	}
	public void setNewTotalVolume(double newTotalVolume) {
		this.newTotalVolume = newTotalVolume;
	}
	public String getNewBatchName() {
		return newBatchName;
	}
	public void setNewBatchName(String newBatchName) {
		this.newBatchName = newBatchName;
	}
	public String getMaterialToSend() {
		return materialToSend;
	}
	public void setMaterialToSend(String materialToSend) {
		this.materialToSend = materialToSend;
	}
	public double getTotalSampleVolume() {
		return totalSampleVolume;
	}
	public void setTotalSampleVolume(double totalSampleVolume) {
		this.totalSampleVolume = totalSampleVolume;
	}
	public String getSampleLocation() {
		return sampleLocation;
	}
	public void setSampleLocation(String sampleLocation) {
		this.sampleLocation = sampleLocation;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getLiquidWastePercentage() {
		return liquidWastePercentage;
	}
	public void setLiquidWastePercentage(int liquidWastePercentage) {
		this.liquidWastePercentage = liquidWastePercentage;
	}
	public String getTreeNodeId() {
		return treeNodeId;
	}
	public void setTreeNodeId(String treeNodeId) {
		this.treeNodeId = treeNodeId;
	}
	public Double getTreeYPosition() {
		return treeYPosition;
	}
	public void setTreeYPosition(Double treeYPosition) {
		this.treeYPosition = treeYPosition;
	}
	public String getMessageToDisplay() {
		return messageToDisplay;
	}
	public void setMessageToDisplay(String messageToDisplay) {
		this.messageToDisplay = messageToDisplay;
	}
	public String getGenerik() {
		return generik;
	}
	public void setGenerik(String generik) {
		this.generik = generik;
	}
}
