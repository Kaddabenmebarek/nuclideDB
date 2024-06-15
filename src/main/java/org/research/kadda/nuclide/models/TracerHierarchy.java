package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.util.Set;

public class TracerHierarchy implements Serializable {

	private static final long serialVersionUID = -2626177419872825169L;

	private Integer nuclideId;
	private Set<TracerHierarchy> nuclideChildrenSet;
	private String nuclideName;
	private String substanceName;
	private char state;
	private double childrenAmount;
	private double initialAmount;
	private String location;
	private int amountPercentage;
	private int level;
	private boolean discarded;
	private boolean externalUsage;

	public TracerHierarchy() {
		super();
	}

	public TracerHierarchy(Integer nuclideId, Set<TracerHierarchy> nuclideChildrenSet, String nuclideName,
			String substanceName, char state, double childrenAmount, double initialAmount,
			String location, int level) {
		super();
		this.nuclideId = nuclideId;
		this.nuclideChildrenSet = nuclideChildrenSet;
		this.nuclideName = nuclideName;
		this.substanceName = substanceName;
		this.state = state;
		this.childrenAmount = childrenAmount;
		this.initialAmount = initialAmount;
		this.location = location;
		this.level = level;
	}

	public Integer getNuclideId() {
		return nuclideId;
	}

	public void setNuclideId(Integer nuclideId) {
		this.nuclideId = nuclideId;
	}

	public Set<TracerHierarchy> getNuclideChildrenSet() {
		return nuclideChildrenSet;
	}

	public void setNuclideChildrenSet(Set<TracerHierarchy> nuclideChildrenSet) {
		this.nuclideChildrenSet = nuclideChildrenSet;
	}

	public String getNuclideName() {
		return nuclideName;
	}

	public void setNuclideName(String nuclideName) {
		this.nuclideName = nuclideName;
	}

	public String getSubstanceName() {
		return substanceName;
	}

	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	public double getChildrenAmount() {
		return childrenAmount;
	}

	public void setChildrenAmount(double childrenAmount) {
		this.childrenAmount = childrenAmount;
	}

	public double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAmountPercentage() {
		return amountPercentage;
	}

	public void setAmountPercentage(int amountPercentage) {
		this.amountPercentage = amountPercentage;
	}
	
	public boolean isDiscarded() {
		return discarded;
	}

	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
	}
	
	public boolean isExternalUsage() {
		return externalUsage;
	}

	public void setExternalUsage(boolean externalUsage) {
		this.externalUsage = externalUsage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(childrenAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(initialAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + level;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((nuclideChildrenSet == null) ? 0 : nuclideChildrenSet.hashCode());
		result = prime * result + ((nuclideId == null) ? 0 : nuclideId.hashCode());
		result = prime * result + ((nuclideName == null) ? 0 : nuclideName.hashCode());
		result = prime * result + state;
		result = prime * result + ((substanceName == null) ? 0 : substanceName.hashCode());
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
		TracerHierarchy other = (TracerHierarchy) obj;
		if (Double.doubleToLongBits(childrenAmount) != Double.doubleToLongBits(other.childrenAmount))
			return false;
		if (Double.doubleToLongBits(initialAmount) != Double.doubleToLongBits(other.initialAmount))
			return false;
		if (level != other.level)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (nuclideChildrenSet == null) {
			if (other.nuclideChildrenSet != null)
				return false;
		} else if (!nuclideChildrenSet.equals(other.nuclideChildrenSet))
			return false;
		if (nuclideId == null) {
			if (other.nuclideId != null)
				return false;
		} else if (!nuclideId.equals(other.nuclideId))
			return false;
		if (nuclideName == null) {
			if (other.nuclideName != null)
				return false;
		} else if (!nuclideName.equals(other.nuclideName))
			return false;
		if (state != other.state)
			return false;
		if (substanceName == null) {
			if (other.substanceName != null)
				return false;
		} else if (!substanceName.equals(other.substanceName))
			return false;
		return true;
	}

}
