package org.research.kadda.nuclide.models;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


public class WasteOverview implements Serializable {
	
	private static final long serialVersionUID = -3283538372528934099L;
	private int nuclideWasteId;
	private String nuclideName;
	private String solidLiquidState;
	private String location;
	private String nuclideUserByCreationUserId;	
	private String nuclideUserByClosureUserId;
	private String closedOn;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date closureDate;	
	private String nuclideUserByDisposalUserId;
	private String disposalRoute;
	private String activityKbq;
	private String disposedOfOn;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date disposalDate;	
	private String disposalStrDate;
	private int disposalLimit;
	private String currentActivityKBq;
	private String currentActivityLE;
	private String hightlight;
	private String status;
	
	public WasteOverview() {
		super();
	}

	public WasteOverview(String nuclideName, String solidLiquidState, String location,
			String nuclideUserByCreationUserId, String nuclideUserByClosureUserId, String closedOn,
			String nuclideUserByDisposalUserId, String disposalRoute, String activityKbq, String disposedOfOn,
			int disposalLimit) {
		super();
		this.nuclideName = nuclideName;
		this.solidLiquidState = solidLiquidState;
		this.location = location;
		this.nuclideUserByCreationUserId = nuclideUserByCreationUserId;
		this.nuclideUserByClosureUserId = nuclideUserByClosureUserId;
		this.closedOn = closedOn;
		this.nuclideUserByDisposalUserId = nuclideUserByDisposalUserId;
		this.disposalRoute = disposalRoute;
		this.activityKbq = activityKbq;
		this.disposedOfOn = disposedOfOn;
		this.disposalLimit = disposalLimit;
	}

	public int getNuclideWasteId() {
		return nuclideWasteId;
	}

	public void setNuclideWasteId(int nuclideWasteId) {
		this.nuclideWasteId = nuclideWasteId;
	}

	public String getNuclideName() {
		return nuclideName;
	}

	public void setNuclideName(String nuclideName) {
		this.nuclideName = nuclideName;
	}

	public String getSolidLiquidState() {
		return solidLiquidState;
	}

	public void setSolidLiquidState(String solidLiquidState) {
		this.solidLiquidState = solidLiquidState;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNuclideUserByCreationUserId() {
		return nuclideUserByCreationUserId;
	}

	public void setNuclideUserByCreationUserId(String nuclideUserByCreationUserId) {
		this.nuclideUserByCreationUserId = nuclideUserByCreationUserId;
	}

	public String getNuclideUserByClosureUserId() {
		return nuclideUserByClosureUserId;
	}

	public void setNuclideUserByClosureUserId(String nuclideUserByClosureUserId) {
		this.nuclideUserByClosureUserId = nuclideUserByClosureUserId;
	}

	public String getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(String closedOn) {
		this.closedOn = closedOn;
	}

	public String getNuclideUserByDisposalUserId() {
		return nuclideUserByDisposalUserId;
	}

	public void setNuclideUserByDisposalUserId(String nuclideUserByDisposalUserId) {
		this.nuclideUserByDisposalUserId = nuclideUserByDisposalUserId;
	}

	public String getDisposalRoute() {
		return disposalRoute;
	}

	public void setDisposalRoute(String disposalRoute) {
		this.disposalRoute = disposalRoute;
	}

	public String getActivityKbq() {
		return activityKbq;
	}

	public void setActivityKbq(String activityKbq) {
		this.activityKbq = activityKbq;
	}

	public String getDisposedOfOn() {
		return disposedOfOn;
	}

	public void setDisposedOfOn(String disposedOfOn) {
		this.disposedOfOn = disposedOfOn;
	}

	public int getDisposalLimit() {
		return disposalLimit;
	}

	public void setDisposalLimit(int disposalLimit) {
		this.disposalLimit = disposalLimit;
	}

	public Date getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public Date getDisposalDate() {
		return disposalDate;
	}

	public void setDisposalDate(Date disposalDate) {
		this.disposalDate = disposalDate;
	}

	public String getCurrentActivityKBq() {
		return currentActivityKBq;
	}

	public void setCurrentActivityKBq(String currentActivityKBq) {
		this.currentActivityKBq = currentActivityKBq;
	}

	public String getCurrentActivityLE() {
		return currentActivityLE;
	}

	public void setCurrentActivityLE(String currentActivityLE) {
		this.currentActivityLE = currentActivityLE;
	}

	public String getHightlight() {
		return hightlight;
	}

	public void setHightlight(String hightlight) {
		this.hightlight = hightlight;
	}

	public String getDisposalStrDate() {
		return disposalStrDate;
	}

	public void setDisposalStrDate(String disposalStrDate) {
		this.disposalStrDate = disposalStrDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
