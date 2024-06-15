package org.research.kadda.nuclide.models;

public class LocationAllowance {

	private String nuclide;
	private double activityThreshold;
	private String location;

	public String getNuclide() {
		return nuclide;
	}

	public void setNuclide(String nuclide) {
		this.nuclide = nuclide;
	}

	public double getActivityThreshold() {
		return activityThreshold;
	}

	public void setActivityThreshold(double activityThreshold) {
		this.activityThreshold = activityThreshold;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(activityThreshold);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((nuclide == null) ? 0 : nuclide.hashCode());
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
		LocationAllowance other = (LocationAllowance) obj;
		if (Double.doubleToLongBits(activityThreshold) != Double.doubleToLongBits(other.activityThreshold))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (nuclide == null) {
			if (other.nuclide != null)
				return false;
		} else if (!nuclide.equals(other.nuclide))
			return false;
		return true;
	}

}
