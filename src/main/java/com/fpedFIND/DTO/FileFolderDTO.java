package com.fpedFIND.DTO;

import java.time.LocalDateTime;

import com.fpedFIND.Entity.Folder;

import jakarta.persistence.Column;

public class FileFolderDTO {
    private Long filefolder_id;
    private Folder folder;
    private UserDto3 user;
    private String filefolder_name;
    private String folderpath;
    private LocalDateTime filefolderDateTime;
    private String details;
    @Column(nullable = true, length = 255)
        private String category_name;

    

  
	@Override
    public String toString() {
        return "FileFolderDTO [filefolder_id=" + filefolder_id + ", folder=" + folder + ", user=" + user
                + ", filefolder_name=" + filefolder_name + ", folderpath=" + folderpath + ", filefolderDateTime="
                + filefolderDateTime + "]";
    }

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public FileFolderDTO(Long filefolder_id, Folder folder, UserDto3 user, String filefolder_name, String folderpath,
			LocalDateTime filefolderDateTime, String category_name, String details) {
		super();
		this.filefolder_id = filefolder_id;
		this.folder = folder;
		this.user = user;
		this.filefolder_name = filefolder_name;
		this.folderpath = folderpath;
		this.filefolderDateTime = filefolderDateTime;
		this.category_name = category_name;
		this.details = details;
		
	}

	public FileFolderDTO() {
		super();
		// TODO Auto-generated constructor stub
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

	public UserDto3 getUser() {
		return user;
	}

	public void setUser(UserDto3 user) {
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
}