package com.fpedFIND.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="fped_filefolder")
public class FileFolder {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filefolder_id;

    @ManyToOne
    @JoinColumn(name="folder_id", nullable=false)
    private Folder folder;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable = false, length = 255)
    private String filefolder_name;

    @Column(nullable = false, length = 255)
    private String folderpath;

    @Column(nullable = false)
    private LocalDateTime filefolderDateTime;
    
    @Column(nullable = true, length = 255)
    private String category_name;
    
    @Column(nullable = true, length = 255)
    private String details;
    

    
    
	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getFilefolder_id() {
		return filefolder_id;
	}

	public void setFilefolder_id(Long filefolder_id) {
		this.filefolder_id = filefolder_id;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFilefolder_name() {
		return filefolder_name;
	}

	public void setFilefolder_name(String filefolder_name) {
		this.filefolder_name = filefolder_name;
	}

	public String getFolderpath() {
		return folderpath;
	}

	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}

	public LocalDateTime getFilefolderDateTime() {
		return filefolderDateTime;
	}

	public void setFilefolderDateTime(LocalDateTime filefolderDateTime) {
		this.filefolderDateTime = filefolderDateTime;
	}

	@Override
	public String toString() {
		return "FileFolder [filefolder_id=" + filefolder_id + ", folder=" + folder + ", user=" + user
				+ ", filefolder_name=" + filefolder_name + ", folderpath=" + folderpath + ", filefolderDateTime="
				+ filefolderDateTime + "]";
	}


    
    
	
}
