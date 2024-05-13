package com.fpedFIND.Entity;
import java.time.LocalDateTime;
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
@Table(name="Fped_comments")
public class CommentTagging {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	
	    private Integer fileId;
	    

	    @Column(columnDefinition = "TEXT")
	    private String content;

	    private LocalDateTime timestamp;
	    
	    
	    private String attachmentName;
	    private String attachmentfilePath;
	    
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}

		public String getContent() {
			return content;
		}
		
		
		public String getContentWithUsername() {
		    if (user != null) {
		        return user.getFirstname()+" "+ user.getLastname() + ": " + content;
		    } else {
		        return "Unknown user: " + content;
		    }
		}
		
		
		public void setContent(String content) {
			this.content = content;
		}
	
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public String getAttachmentName() {
			return attachmentName;
		}
		public void setAttachmentName(String attachmentName) {
			this.attachmentName = attachmentName;
		}
		public String getAttachmentfilePath() {
			return attachmentfilePath;
		}
		public void setAttachmentfilePath(String attachmentfilePath) {
			this.attachmentfilePath = attachmentfilePath;
		}
		public CommentTagging() {
			super();
			// TODO Auto-generated constructor stub
		}


		public void transferTo(java.io.File newFile) {
			// TODO Auto-generated method stub
			
		}
		
		  public Object[] getTimestamp() {
		        // Convert LocalDateTime to an array of integers
		        return new Object[]{
		            timestamp.getYear(),
		            timestamp.getMonthValue(),
		            timestamp.getDayOfMonth(),
		            timestamp.getHour(),
		            timestamp.getMinute(),
		            timestamp.getSecond(),
		            timestamp.getNano()
		        };
		    }

		public Integer getFileId() {
			return fileId;
		}
		public void setFileId(Integer fileId) {
			this.fileId = fileId;
		}
	    
}
