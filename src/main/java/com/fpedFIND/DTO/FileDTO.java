package com.fpedFIND.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;

public class FileDTO {

    private Integer fileId;
    private String description;
    private String remarks;
    private String fileName;
    private String filePath;
    private String dts;
    private String statusName;
    private String categoryName;
    private String sectionName;  
    private String filetitle;
    private UserDto3 user;
    private LocalDateTime uploadDateTime;
    private String latestComment;

    
	 @Column
	 private String taggedUsers; // Assuming this field will contain user IDs separated by delimiters like comma (`,`), semicolon (`;`), etc.
	    public String getTaggedUsers() {
	        return taggedUsers;
	    }
	    
	    public void setTaggedUsers(String taggedUsers) {
	        this.taggedUsers = taggedUsers;
	    }
    
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDts() {
		return dts;
	}
	public void setDts(String dts) {
		this.dts = dts;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getFiletitle() {
		return filetitle;
	}
	public void setFiletitle(String filetitle) {
		this.filetitle = filetitle;
	}
	public UserDto3 getUser() {
		return user;
	}
	public void setUser(UserDto3 user) {
		this.user = user;
	}
	public LocalDateTime getUploadDateTime() {
		return uploadDateTime;
	}
	public void setUploadDateTime(LocalDateTime uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public String getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(String latestComment) {
		this.latestComment = latestComment;
	}
    
    // Getters and setters
}
