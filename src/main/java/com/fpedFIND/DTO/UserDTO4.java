package com.fpedFIND.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDTO4 {
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate birthday;
    private Integer user_id;
    private String firstname;
    private String lastname;
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
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
	public UserDTO4(LocalDate birthday, Integer user_id, String firstname, String lastname) {
		super();
		this.birthday = birthday;
		this.user_id = user_id;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	public UserDTO4() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
