package com.fpedFIND.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.CalendarEvents;



@Repository
public interface CalendarRepository  extends JpaRepository <CalendarEvents , Long>{
	   @Query("SELECT ce FROM CalendarEvents ce WHERE ce.user.user_id = :userId")
	    List<CalendarEvents> findAllByUserId(@Param("userId") Integer userId);
	   
	   
	   @Query("SELECT ce FROM CalendarEvents ce WHERE ce.chief_id = :chiefId")
	    List<CalendarEvents> findAllByChiefId(@Param("chiefId") Integer chiefId);
}
