package com.fpedFIND.Entity;

import java.time.LocalDateTime;

public class LastActivityRequest {
	  private Integer userId;
      private LocalDateTime lastActivityTime;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public LastActivityRequest(Integer userId, LocalDateTime lastActivityTime) {
		super();
		this.userId = userId;
		this.setLastActivityTime(lastActivityTime);
	}
	public LocalDateTime getLastActivityTime() {
		return lastActivityTime;
	}
	public void setLastActivityTime(LocalDateTime lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}
	public LastActivityRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

      
	
	
      
      
}
