package org.research.kadda.nuclide.models;

import java.math.BigDecimal;

public class UsageOverview {
	
	private BigDecimal usageId;
	private String wastId;
	private Integer percentage;
	private Integer overallPercentage;
	private BigDecimal amount;
	private String bioLabJournal;
	private String assayType;
	private String destination;
	private String usageDate;
	private String user;
	private BigDecimal newTracerId;
	private BigDecimal tracerType;	
	
	public BigDecimal getUsageId() {
		return usageId;
	}
	public void setUsageId(BigDecimal usageId) {
		this.usageId = usageId;
	}
	public String getWastId() {
		return wastId;
	}
	public void setWastId(String wastId) {
		this.wastId = wastId;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
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
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Integer getOverallPercentage() {
		return overallPercentage;
	}
	public void setOverallPercentage(Integer overallPercentage) {
		this.overallPercentage = overallPercentage;
	}
	public BigDecimal getNewTracerId() {
		return newTracerId;
	}
	public void setNewTracerId(BigDecimal newTracerId) {
		this.newTracerId = newTracerId;
	}
	public BigDecimal getTracerType() {
		return tracerType;
	}
	public void setTracerType(BigDecimal tracerType) {
		this.tracerType = tracerType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((assayType == null) ? 0 : assayType.hashCode());
		result = prime * result + ((bioLabJournal == null) ? 0 : bioLabJournal.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((overallPercentage == null) ? 0 : overallPercentage.hashCode());
		result = prime * result + ((percentage == null) ? 0 : percentage.hashCode());
		result = prime * result + ((usageDate == null) ? 0 : usageDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((wastId == null) ? 0 : wastId.hashCode());
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
		UsageOverview other = (UsageOverview) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
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
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (overallPercentage == null) {
			if (other.overallPercentage != null)
				return false;
		} else if (!overallPercentage.equals(other.overallPercentage))
			return false;
		if (percentage == null) {
			if (other.percentage != null)
				return false;
		} else if (!percentage.equals(other.percentage))
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
		if (wastId == null) {
			if (other.wastId != null)
				return false;
		} else if (!wastId.equals(other.wastId))
			return false;
		return true;
	}
	
	
	
}
