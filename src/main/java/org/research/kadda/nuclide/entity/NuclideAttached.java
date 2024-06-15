package org.research.kadda.nuclide.entity;

import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * NuclideAttached generated by hbm2java
 */
@Entity
@Table(name = "NUCLIDE_ATTACHED", schema = "OSIRIS")
@Audited
@SequenceGenerator(name="nuclide_attached_seq", sequenceName="osiris.nuclide_attached_seq_id", allocationSize=1)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NuclideAttached implements java.io.Serializable {

	private static final long serialVersionUID = 8758278264044131184L;
	private int nuclideAttachedId;
	private NuclideBottle nuclideBottle;
	private NuclideUser nuclideUser;
	private String fileName;
	private String fileType;
	private String filePath;
	private String fileFullPath;
	private Date fileDate;

	public NuclideAttached() {
	}

	public NuclideAttached(NuclideBottle nuclideBottle, NuclideUser nuclideUser, String fileName,
			String fileType, String filePath, String fileFullPath, Date fileDate) {
		this.nuclideBottle = nuclideBottle;
		this.nuclideUser = nuclideUser;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.fileFullPath = fileFullPath;
		this.fileDate = fileDate;
	}

	@Id
	@Column(name = "NUCLIDE_ATTACHED_ID", unique = true, nullable = false, precision = 8, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="nuclide_attached_seq")
	public int getNuclideAttachedId() {
		return this.nuclideAttachedId;
	}

	public void setNuclideAttachedId(int nuclideAttachedId) {
		this.nuclideAttachedId = nuclideAttachedId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.MERGE)
	@JoinColumn(name = "NUCLIDE_BOTTLE_ID", nullable = false)
	public NuclideBottle getNuclideBottle() {
		return this.nuclideBottle;
	}

	public void setNuclideBottle(NuclideBottle nuclideBottle) {
		this.nuclideBottle = nuclideBottle;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "USER_ID", nullable = false)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public NuclideUser getNuclideUser() {
		return this.nuclideUser;
	}

	public void setNuclideUser(NuclideUser nuclideUser) {
		this.nuclideUser = nuclideUser;
	}	

	@Column(name = "FILE_NAME", nullable = false, length = 100)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_TYPE", nullable = false, length = 10)
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_PATH", nullable = false, length = 300)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "FILE_FULLPATH", nullable = false, length = 400)
	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FILE_DATE", nullable = false, length = 7)
	public Date getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}


}
