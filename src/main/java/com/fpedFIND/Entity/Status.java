package com.fpedFIND.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="Fped_status")
public class Status {
	  @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Column(name="status_id")
	   private Integer status_id;

	   private String statusName;

	public Status() {
	}

	public Integer getStatus_id() {
		return status_id;
	}

	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Status(Integer status_id, String statusName) {
		this.status_id = status_id;
		this.statusName = statusName;
	}

	@Override
	public String toString() {
		return "Status [status_id=" + status_id + ", statusName=" + statusName + "]";
	}
	   
	   
	
	
	   
}
