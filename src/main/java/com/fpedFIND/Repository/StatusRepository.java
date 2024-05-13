package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpedFIND.Entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer>{

	Status findByStatusName(String statusName);
	
	static Status getStatusByID(Long statusId) {
		// TODO Auto-generated method stub
		return null;
	}
}
