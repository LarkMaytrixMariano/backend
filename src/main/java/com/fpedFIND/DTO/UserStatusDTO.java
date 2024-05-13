package com.fpedFIND.DTO;

public class UserStatusDTO {
	   private Integer user_id;
	    private String status;
	    private String firstname;
	    private String lastname;
	    
		public UserStatusDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		public UserStatusDTO(Integer user_id, String status) {
			super();
			this.setUser_id(user_id);
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getFirstname() {
			return firstname;
		}
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public Integer getUser_id() {
			return user_id;
		}
		public void setUser_id(Integer user_id) {
			this.user_id = user_id;
		}
		public String getLastname() {
			return lastname;
		}
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
	    
	    
	    
}
