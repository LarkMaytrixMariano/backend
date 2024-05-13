package com.fpedFIND.DTO;

import java.time.LocalDateTime;



public class CommentDTO {
	private Long id;
    private Integer fileId;
    private String attachmentName;
    private String attachmentFilePath;
    private String content;
    private LocalDateTime timestamp;
    private UserDTO2 user;

    
    
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer integer) {
		this.fileId = integer;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentFilePath() {
		return attachmentFilePath;
	}
	public void setAttachmentFilePath(String attachmentFilePath) {
		this.attachmentFilePath = attachmentFilePath;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public CommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserDTO2 getUser() {
		return user;
	}
	public void setUser(UserDTO2 user) {
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	
    // Constructor, getters, and setters
}