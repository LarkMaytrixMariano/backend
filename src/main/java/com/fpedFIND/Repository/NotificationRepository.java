package com.fpedFIND.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Define custom query methods if needed
	
    List<Notification> findByTagged(String receiverId);

    List<Notification> findByTaggedAndSeen(String receiverId, boolean seen);
    
    
    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM Notification n WHERE n.tagged = :receiverId AND n.seen = true")
    boolean areNotificationsSeen(@Param("receiverId") String receiverId);
    
    // Custom query to count unseen notifications for a specific receiver
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.tagged = ?1 AND n.seen = ?2")
    Long countByTaggedAndSeen(String receiverId, boolean seen);
    
}
