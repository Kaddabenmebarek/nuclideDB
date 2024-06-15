package org.research.kadda.nuclide.models;

import java.io.Serializable;

public class AttachedFile implements Serializable{

	private static final long serialVersionUID = 5250833275564348714L;
	private int nuclideAttachedId;
	private String nuclideBottle;
	private String nuclideUser;
	private String fileName;
	private String fileType;
	private String filePath;
	private String fileDate;
	private String deleteParam;
	
	public int getNuclideAttachedId() {
		return nuclideAttachedId;
	}
	public void setNuclideAttachedId(int nuclideAttachedId) {
		this.nuclideAttachedId = nuclideAttachedId;
	}
	public String getNuclideBottle() {
		return nuclideBottle;
	}
	public void setNuclideBottle(String nuclideBottle) {
		this.nuclideBottle = nuclideBottle;
	}
	public String getNuclideUser() {
		return nuclideUser;
	}
	public void setNuclideUser(String nuclideUser) {
		this.nuclideUser = nuclideUser;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getDeleteParam() {
		return deleteParam;
	}
	public void setDeleteParam(String deleteParam) {
		this.deleteParam = deleteParam;
	}
}
