package com.fpedFIND.Entity;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Fped_users")
public class User {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="user_id")
	    private Integer user_id;

	    private String username;
	    private String password;
	    private String email;
	    private String sectionName;
	    private String firstname;
	    private String lastname;
	    private String department;
	    private String role_name; 
	    private LocalDateTime lastActive;
	    private boolean hasAccess;
	    private boolean inactive;
	    
	    @Lob
	    @Column(columnDefinition = "LONGBLOB")
	    private String profilePicture;
	    
	    @JsonFormat(pattern = "MM-dd-yyyy")
	    private LocalDate birthday;
	    private String status;
	    

		public User() {
		}

	

		public User(Integer user_id, String username, String password, String email, String sectionName,
				String firstname, String lastname, String department, String role_name, String profilePicture,
				LocalDate birthday, String status, boolean hasAccess) {
			super();
			this.user_id = user_id;
			this.username = username;
			this.password = password;
			this.email = email;
			this.sectionName = sectionName;
			this.firstname = firstname;
			this.lastname = lastname;
			this.department = department;
			this.role_name = role_name;
			this.profilePicture = profilePicture;
			this.birthday = birthday;
			this.status = status;
			this.hasAccess = hasAccess;

		}
		
		
		public User(Integer user_id, String username, String firstname, String lastname,String profilePicture) {
			super();
			this.user_id = user_id;
			this.username = username;
			this.firstname = firstname;
			this.lastname = lastname;
			this.profilePicture = profilePicture;
		}
	



		public Integer getUser_id() {
			return user_id;
		}

		public void setUser_id(Integer user_id) {
			this.user_id = user_id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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

		public String getProfilePicture() {
			return profilePicture;
		}

		public void setProfilePicture(String profilePicture) {
			this.profilePicture = profilePicture;
		}

		public LocalDate getBirthday() {
			return birthday;
		}


		public void setBirthday(LocalDate birthday) {
			this.birthday = birthday;
		}
		
		

	    public String getFullName() {
	        return this.firstname + " " + this.lastname;
	    }



		@Override
		public String toString() {
			return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", email=" + email
					+ ", sectionName=" + sectionName + ", firstname=" + firstname + ", lastname=" + lastname
					+ ", department=" + department + ", role_name=" + role_name + ", profilePicture=" + profilePicture
					+ ", birthday=" + birthday + ", status=" + status + "]";
		}



		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}



		public LocalDateTime getLastActive() {
			return lastActive;
		}



		public void setLastActive(LocalDateTime lastActive) {
			this.lastActive = lastActive;
		}



		public Boolean getHasAccess() {
			return hasAccess;
		}



		public void setHasAccess(Boolean hasAccess) {
			this.hasAccess = hasAccess;
		}



		public boolean isInactive() {
			return inactive;
		}



		public void setInactive(boolean inactive) {
			this.inactive = inactive;
		}



	
	



		
		
	
}