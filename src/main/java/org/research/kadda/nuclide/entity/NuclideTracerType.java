package org.research.kadda.nuclide.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "NUCLIDE_TRACER_TYPE", schema = "OSIRIS")
@SequenceGenerator(name = "nuclide_tracer_type_seq", sequenceName = "osiris.nuclide_tracer_type_seq_id", allocationSize = 1)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NuclideTracerType implements java.io.Serializable {

	private static final long serialVersionUID = -3157248401082103624L;
	private int tracerTypeId;
	private String tracerTypeName;
	private Set<NuclideBottle> nuclideBottles = new HashSet<NuclideBottle>(0);
	
	public NuclideTracerType() {
		super();
	}

	public NuclideTracerType(int tracerTypeId) {
		super();
		this.tracerTypeId = tracerTypeId;
	}

	public NuclideTracerType(int tracerTypeId, String tracerTypeName) {
		super();
		this.tracerTypeId = tracerTypeId;
		this.tracerTypeName = tracerTypeName;
	}

	@Id
	@Column(name = "TRACER_TYPE_ID", unique = true, nullable = false, precision = 8, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nuclide_tracer_type_seq")
	public int getTracerTypeId() {
		return tracerTypeId;
	}

	public void setTracerTypeId(int tracerTypeId) {
		this.tracerTypeId = tracerTypeId;
	}

	@Column(name = "TRACER_TYPE_NAME", nullable = false, length = 16)
	public String getTracerTypeName() {
		return tracerTypeName;
	}

	public void setTracerTypeName(String tracerTypeName) {
		this.tracerTypeName = tracerTypeName;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tracerType")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<NuclideBottle> getNuclideBottlesForNuclideUserId() {
		return this.nuclideBottles;
	}

	public void setNuclideBottlesForNuclideUserId(Set<NuclideBottle> nuclideBottles) {
		this.nuclideBottles = nuclideBottles;
	}

}