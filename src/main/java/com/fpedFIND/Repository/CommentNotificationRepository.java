package com.fpedFIND.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.CommentNotification;

@Repository
public interface CommentNotificationRepository extends JpaRepository<CommentNotification, Long> {
    List<CommentNotification> findByUploaderId(String uploaderId);
    
    List<CommentNotification> findByUploaderIdAndSeen(String uploaderId, boolean seen);

    
    // Custom query to count unseen notifications for a specific receiver
    @Query("SELECT COUNT(n) FROM CommentNotification n WHERE n.uploaderId = ?1 AND n.seen = ?2")
    Long countByTaggedAndSeen(String uploaderId, boolean seen);

}
