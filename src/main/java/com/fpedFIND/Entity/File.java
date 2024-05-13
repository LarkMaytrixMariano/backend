package com.fpedFIND.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name="Fped_files")
public class File {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	    @ManyToOne
		@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	    private User user;
		private LocalDateTime uploadDateTime;
		
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDate deadline; // New variable for deadline

	    
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private LocalDateTime statusChanged;
		
		
		 public LocalDateTime getStatusChanged() {
			return statusChanged;
		}

		public void setStatusChanged(LocalDateTime statusChanged) {
			this.statusChanged = statusChanged;
		}

		@Column
		 private String taggedUsers; // Assuming this field will contain user IDs separated by delimiters like comma (`,`), semicolon (`;`), etc.
		    public String getTaggedUsers() {
		        return taggedUsers;
		    }
		    
		    public void setTaggedUsers(String taggedUsers) {
		        this.taggedUsers = taggedUsers;
		    }
		    
		    // Method to add a single tagged user
		    public void addTaggedUser(String userId) {
		        if (this.taggedUsers == null || this.taggedUsers.isEmpty()) {
		            this.taggedUsers = userId;
		        } else {
		            this.taggedUsers += "," + userId; // You can use a different delimiter as per your choice
		        }
		    }

		    // Method to add multiple tagged users at once
		    public void addMultipleTaggedUsers(List<String> userIds) {
		        if (this.taggedUsers == null || this.taggedUsers.isEmpty()) {
		            this.taggedUsers = String.join(",", userIds);
		        } else {
		            for (String userId : userIds) {
		                this.taggedUsers += "," + userId; // You can use a different delimiter as per your choice
		            }
		        }
		    }
		
		
		public File() {
			super();
		}
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
		public String getFiletitle() {
			return filetitle;
		}

		public void setFiletitle(String filetitle) {
			this.filetitle = filetitle;
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
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public LocalDateTime getUploadDateTime() {
			return uploadDateTime;
		}
		public void setUploadDateTime(LocalDateTime uploadDateTime) {
			this.uploadDateTime = uploadDateTime;
		}
		public File(Integer fileId, String description, String remarks, String fileName, String filePath, String dts,
				String statusName, String categoryName, String sectionName, User user, LocalDateTime uploadDateTime, String filetitle) {
			this.fileId = fileId;
			this.description = description;
			this.remarks = remarks;
			this.fileName = fileName;
			this.filePath = filePath;
			this.dts = dts;
			this.statusName = statusName;
			this.categoryName = categoryName;
			this.sectionName = sectionName;
			this.user = user;
			this.uploadDateTime = uploadDateTime;
			this.filetitle = filetitle;
		}
		@Override
		public String toString() {
			return "File [fileId=" + fileId + ", description=" + description + ", remarks=" + remarks + ", fileName="
					+ fileName + ", filePath=" + filePath + ", dts=" + dts + ", statusName=" + statusName
					+ ", categoryName=" + categoryName + ", sectionName=" + sectionName + ", user=" + user
					+ ", uploadDateTime=" + uploadDateTime + ", filetitle=" + filetitle +"]";
		}

		public LocalDate getDeadline() {
			return deadline;
		}

		public void setDeadline(LocalDate newDeadline) {
			this.deadline = newDeadline;
		}

		

	
}
