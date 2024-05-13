package com.fpedFIND.Entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public class UserProfileUpdateRequest {
	
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String profilePicture;	    
    private String firstname;
	    private String lastname;
	    private String username;
	    private String oldPassword;
	    private String newPassword;
	    private String email;
	    private String sectionName;
	    private String department;
	    private String role_name;
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	    private LocalDate birthday;

	    
		public String getProfilePicture() {
			return profilePicture;
		}

		public void setProfilePicture(String profilePicture) {
			this.profilePicture = profilePicture;
		}
	    
	    
		public String getFirstname() {
			return firstname;
		}
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public String getLastname() {
			return lastname;
		}
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		 public String getOldPassword() {
		        return oldPassword;
		    }

		    public void setOldPassword(String oldPassword) {
		        this.oldPassword = oldPassword;
		    }

		    public String getNewPassword() {
		        return newPassword;
		    }

		    public void setNewPassword(String newPassword) {
		        this.newPassword = newPassword;
		    }
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getSectionName() {
			return sectionName;
		}
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getRole_name() {
			return role_name;
		}
		public void setRole_name(String role_name) {
			this.role_name = role_name;
		}
		public LocalDate getBirthday() {
			return birthday;
		}
		public void setBirthday(LocalDate birthday) {
			this.birthday = birthday;
		}
	    

}
