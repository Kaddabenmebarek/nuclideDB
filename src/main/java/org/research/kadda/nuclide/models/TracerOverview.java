package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

public class TracerOverview implements Serializable {

	private static final long serialVersionUID = -4315452480112853825L;
	private int tracerId;
	private int parentId;
	private String nuclideName;
	private String location;
	private String substance;
	private String solidLiquidState;
	private String originalDaughterState;
	private String currentAmount;
	private String currentActivity;
	private BigDecimal initialAmount;
	private BigDecimal initialActivity;
	private String responsible;
	private Date activityDate;
	private String creationDate;
	private String initialActivityDate;
	private String disposalDate;
	private String activityYear;
	private String discardedStatus;
	private String parentTracerTube;
	private String elbTracerCreation;
	private String nuclideUserByCreationUserId;
	private String creationUserId;
	private String nuclideUserByDisposalUserId;
	private String batchName;
	private String wasteContainer;
	private Map<String, String> wasteContainerList;
	private String userId;
	private List<UsageOverview> usageList;
	private String attachedFiles;
	private String attachedFilesLabel;
	private Double solideWastePercentage;
	private String generik;
	private int tracerTypeId;
	private Boolean externalTracersOnly;
	private String destination;
	private String isChildren;
	private TracerOverview parent;
	private List<TracerOverview> children;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date discardDate;

	public TracerOverview() {
		super();
	}

	public TracerOverview(int tracerId, String nuclideName, String location, String substance, String solidLiquidState,
			String originalDaughterState, String currentAmount, String currentActivity, BigDecimal initialAmount,
			BigDecimal initialActivity, String responsible, Date activityDate, Date discardDate, String activityYear,
			String discardedStatus, String parentTracerTube, String elbTracerCreation,
			String nuclideUserByCreationUserId, String nuclideUserByDisposalUserId, String batchName,
			String wasteContainer, Map<String, String> wasteContainerList) {
		super();
		this.tracerId = tracerId;
		this.nuclideName = nuclideName;
		this.location = location;
		this.substance = substance;
		this.solidLiquidState = solidLiquidState;
		this.originalDaughterState = originalDaughterState;
		this.currentAmount = currentAmount;
		this.currentActivity = currentActivity;
		this.initialAmount = initialAmount;
		this.initialActivity = initialActivity;
		this.responsible = responsible;
		this.activityDate = activityDate;
		this.discardDate = discardDate;
		this.activityYear = activityYear;
		this.discardedStatus = discardedStatus;
		this.parentTracerTube = parentTracerTube;
		this.elbTracerCreation = elbTracerCreation;
		this.nuclideUserByCreationUserId = nuclideUserByCreationUserId;
		this.nuclideUserByDisposalUserId = nuclideUserByDisposalUserId;
		this.batchName = batchName;
		this.wasteContainer = wasteContainer;
		this.wasteContainerList = wasteContainerList;
	}

	public int getTracerId() {
		return tracerId;
	}

	public void setTracerId(int tracerId) {
		this.tracerId = tracerId;
	}

	public String getNuclideName() {
		return nuclideName;
	}

	public void setNuclideName(String nuclideName) {
		this.nuclideName = nuclideName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSubstance() {
		return substance;
	}

	public void setSubstance(String substance) {
		this.substance = substance;
	}

	public String getSolidLiquidState() {
		return solidLiquidState;
	}

	public void setSolidLiquidState(String solidLiquidState) {
		this.solidLiquidState = solidLiquidState;
	}

	public String getOriginalDaughterState() {
		return originalDaughterState;
	}

	public void setOriginalDaughterState(String originalDaughterState) {
		this.originalDaughterState = originalDaughterState;
	}

	public String getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(String currentActivity) {
		this.currentActivity = currentActivity;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}

	public BigDecimal getInitialActivity() {
		return initialActivity;
	}

	public void setInitialActivity(BigDecimal initialActivity) {
		this.initialActivity = initialActivity;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getDisposalDate() {
		return disposalDate;
	}

	public void setDisposalDate(String disposalDate) {
		this.disposalDate = disposalDate;
	}

	public Date getDiscardDate() {
		return discardDate;
	}

	public void setDiscardDate(Date discardDate) {
		this.discardDate = discardDate;
	}

	public String getActivityYear() {
		return activityYear;
	}

	public void setActivityYear(String activityYear) {
		this.activityYear = activityYear;
	}

	public String getDiscardedStatus() {
		return discardedStatus;
	}

	public void setDiscardedStatus(String discardedStatus) {
		this.discardedStatus = discardedStatus;
	}

	public String getParentTracerTube() {
		return parentTracerTube;
	}

	public void setParentTracerTube(String parentTracerTube) {
		this.parentTracerTube = parentTracerTube;
	}

	public String getElbTracerCreation() {
		return elbTracerCreation;
	}

	public void setElbTracerCreation(String elbTracerCreation) {
		this.elbTracerCreation = elbTracerCreation;
	}

	public String getNuclideUserByCreationUserId() {
		return nuclideUserByCreationUserId;
	}

	public void setNuclideUserByCreationUserId(String nuclideUserByCreationUserId) {
		this.nuclideUserByCreationUserId = nuclideUserByCreationUserId;
	}

	public String getNuclideUserByDisposalUserId() {
		return nuclideUserByDisposalUserId;
	}

	public void setNuclideUserByDisposalUserId(String nuclideUserByDisposalUserId) {
		this.nuclideUserByDisposalUserId = nuclideUserByDisposalUserId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getWasteContainer() {
		return wasteContainer;
	}

	public void setWasteContainer(String wasteContainer) {
		this.wasteContainer = wasteContainer;
	}

	public Map<String, String> getWasteContainerList() {
		return wasteContainerList;
	}

	public void setWasteContainerList(Map<String, String> wasteContainerList) {
		this.wasteContainerList = wasteContainerList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreationUserId() {
		return creationUserId;
	}

	public void setCreationUserId(String creationUserId) {
		this.creationUserId = creationUserId;
	}

	public String getInitialActivityDate() {
		return initialActivityDate;
	}

	public void setInitialActivityDate(String initialActivityDate) {
		this.initialActivityDate = initialActivityDate;
	}

	public List<UsageOverview> getUsageList() {
		return usageList;
	}

	public void setUsageList(List<UsageOverview> usageList) {
		this.usageList = usageList;
	}

	public String getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(String attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	public String getAttachedFilesLabel() {
		return attachedFilesLabel;
	}

	public void setAttachedFilesLabel(String attachedFilesLabel) {
		this.attachedFilesLabel = attachedFilesLabel;
	}

	public Double getSolideWastePercentage() {
		return solideWastePercentage;
	}

	public void setSolideWastePercentage(Double solideWastePercentage) {
		this.solideWastePercentage = solideWastePercentage;
	}

	public String getGenerik() {
		return generik;
	}

	public void setGenerik(String generik) {
		this.generik = generik;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public int getTracerTypeId() {
		return tracerTypeId;
	}

	public void setTracerTypeId(int tracerTypeId) {
		this.tracerTypeId = tracerTypeId;
	}

	public Boolean getExternalTracersOnly() {
		return externalTracersOnly;
	}

	public void setExternalTracersOnly(Boolean externalTracersOnly) {
		this.externalTracersOnly = externalTracersOnly;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getIsChildren() {
		return isChildren;
	}

	public void setIsChildren(String isChildren) {
		this.isChildren = isChildren;
	}

	public TracerOverview getParent() {
		return parent;
	}

	public void setParent(TracerOverview parent) {
		this.parent = parent;
	}

	public List<TracerOverview> getChildren() {
		return children;
	}

	public void setChildren(List<TracerOverview> children) {
		this.children = children;
	}

}
