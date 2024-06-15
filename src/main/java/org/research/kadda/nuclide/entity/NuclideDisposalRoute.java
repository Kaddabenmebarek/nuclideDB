package org.research.kadda.nuclide.entity;
// Generated Apr 29, 2019 9:26:57 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NuclideDisposalRoute generated by hbm2java
 */
@Entity
@Table(name = "NUCLIDE_DISPOSAL_ROUTE", schema = "OSIRIS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NuclideDisposalRoute implements java.io.Serializable {

	private static final long serialVersionUID = -7840137440815985675L;
	private String disposalRoute;

	public NuclideDisposalRoute() {
	}

	public NuclideDisposalRoute(String disposalRoute) {
		this.disposalRoute = disposalRoute;
	}

	@Id
	@Column(name = "DISPOSAL_ROUTE", nullable = false, length = 32)
	public String getDisposalRoute() {
		return this.disposalRoute;
	}

	public void setDisposalRoute(String disposalRoute) {
		this.disposalRoute = disposalRoute;
	}

}
