package org.research.kadda.nuclide.entity;
// Generated May 12, 2022, 1:22:21 PM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NuclideLocationAllowance generated by hbm2java
 */
@Entity
@Table(name = "NUCLIDE_LOCATION_ALLOWANCE", schema = "OSIRIS")
@SequenceGenerator(name="NUCLIDE_LOCATION_ALLOWANCE_SEQ", sequenceName="OSIRIS.NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ", allocationSize=1)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NuclideLocationAllowance implements java.io.Serializable {

	private static final long serialVersionUID = 2077867337658041889L;
	private int id;
	private NuclideLocation nuclideLocation;
	private NuclideAllowance nuclideAllowance;

	public NuclideLocationAllowance() {
	}

	public NuclideLocationAllowance(int id, NuclideLocation nuclideLocation, NuclideAllowance nuclideAllowance) {
		this.id = id;
		this.nuclideLocation = nuclideLocation;
		this.nuclideAllowance = nuclideAllowance;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 8, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NUCLIDE_LOCATION_ALLOWANCE_SEQ")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCATION_ID", nullable = false)
	public NuclideLocation getNuclideLocation() {
		return this.nuclideLocation;
	}

	public void setNuclideLocation(NuclideLocation nuclideLocation) {
		this.nuclideLocation = nuclideLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALLOWANCE_ID", nullable = false)
	public NuclideAllowance getNuclideAllowance() {
		return this.nuclideAllowance;
	}

	public void setNuclideAllowance(NuclideAllowance nuclideAllowance) {
		this.nuclideAllowance = nuclideAllowance;
	}

}
