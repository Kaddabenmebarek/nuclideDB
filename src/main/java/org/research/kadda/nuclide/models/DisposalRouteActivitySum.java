package org.research.kadda.nuclide.models;

import java.io.Serializable;

public class DisposalRouteActivitySum implements Serializable{

	private static final long serialVersionUID = -26334660551389865L;
	
	private String disposalRoute;
	private Integer activitySum;
	
	public DisposalRouteActivitySum() {
		super();
	}

	public DisposalRouteActivitySum(String disposalRoute, Integer activitySum) {
		super();
		this.disposalRoute = disposalRoute;
		this.activitySum = activitySum;
	}

	public String getDisposalRoute() {
		return disposalRoute;
	}

	public void setDisposalRoute(String disposalRoute) {
		this.disposalRoute = disposalRoute;
	}

	public Integer getActivitySum() {
		return activitySum;
	}

	public void setActivitySum(Integer activitySum) {
		this.activitySum = activitySum;
	}
	
	

}
