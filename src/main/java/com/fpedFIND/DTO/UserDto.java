package com.fpedFIND.DTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDto {

    private Integer user_id;
    private String firstname;
    private String lastname;
    private String username;
    private String role_name;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate birthday;
    private String sectionName;
    private String status;
    private String email;
    private LocalDateTime lastActive;
 

    private boolean hasAccess;
    
    
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
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
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	

	public UserDto(Integer user_id, String firstname, String lastname,
			LocalDate birthday, String sectionName, Boolean hasAccess) {
		super();
		this.user_id = user_id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.sectionName = sectionName;
		this.hasAccess = hasAccess;

	}
	public UserDto() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

	public Boolean getHasAccess() {
		return hasAccess;
	}
	public void setHasAccess(Boolean hasAccess) {
		this.hasAccess = hasAccess;
	}
	
	
	   public UserDto(String firstname, String lastname) {
	        this.firstname = firstname;
	        this.lastname = lastname;

	    }
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getLastActive() {
		return lastActive;
	}
	public void setLastActive(LocalDateTime lastActive) {
		this.lastActive = lastActive;
	}
	   
	
}
