package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpedFIND.Entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
	
}