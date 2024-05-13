package com.fpedFIND.UserController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fpedFIND.Entity.Notification;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Repository.NotificationRepository;
import com.fpedFIND.Repository.UserRepository;




@RestController
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    
    @Autowired
    private NotificationRepository notificationRepository;
    
	@Autowired
	private UserRepository repo;
	
	
	@MessageMapping("/sendNotification")
	public void sendAdminAlert(Notification payload) {
	    String receiverId = payload.getTagged();
	    String message = payload.getMessage();
	    String sender = payload.getSender();
	    String filename = payload.getFilename();
	    Integer fileId = payload.getFileId();
	    LocalDateTime timestamp = payload.getTimestamp();
	    
	    // Save the notification to the database with "seen" set to false
	    payload.setSeen(false);
	    
	    if (!"all".equals(receiverId)) { // Exclude the "all" case
	        // If receiverId is not "all", send the notification to the specified user only
	        Notification userNotification = new Notification();
	        userNotification.setTagged(receiverId);
	        userNotification.setMessage(message);
	        userNotification.setSender(sender);
	        userNotification.setFilename(filename);
	        userNotification.setFileId(fileId);
	        userNotification.setTimestamp(timestamp);
	        // Save the notification to the database for the specified user
	        notificationRepository.save(userNotification);
	        // Send the notification to the specified user
	        messagingTemplate.convertAndSendToUser(receiverId, "/queue/Notifications", userNotification);
	    } else {
	        // If receiverId is "all", send the notification to all users
	        List<User> allUsers = repo.findAll(); // Assuming you have a repository for User entities
	        for (User user : allUsers) {
	            // Create a new instance of the payload for each user
	            Notification userNotification = new Notification();
	            userNotification.setTagged(user.getUser_id().toString());
	            userNotification.setMessage(message);
	            userNotification.setSender(sender);
	            userNotification.setFilename(filename);
	            userNotification.setFileId(fileId);
	            userNotification.setTimestamp(timestamp);
	            // Save the notification to the database for each user
	            userNotification.setSeen(false);
	            notificationRepository.save(userNotification);
	            // Send the notification to each user
	            messagingTemplate.convertAndSendToUser(user.getUser_id().toString(), "/queue/Notifications", userNotification);
	        }
	    }
	}

    
    @GetMapping("/notifications/{receiverId}")
    public List<Notification> getNotificationsByReceiverId(@PathVariable String receiverId) {
        if ("all".equals(receiverId)) {
            // If receiverId is "all", return all notifications
            return notificationRepository.findAll();
        } else {
            // If receiverId is specific, return notifications for that receiverId
            return notificationRepository.findByTagged(receiverId);
        }
    }
    
    @DeleteMapping("/notifications/{receiverId}")
    public ResponseEntity<Map<String, String>> deleteNotificationsByReceiverId(@PathVariable String receiverId) {
        List<Notification> notificationsToDelete = notificationRepository.findByTagged(receiverId);

        if (notificationsToDelete.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("error", "No notifications found for receiverId: " + receiverId), HttpStatus.NOT_FOUND);
        } else {
            notificationRepository.deleteAll(notificationsToDelete);
            return new ResponseEntity<>(Collections.singletonMap("message", "Notifications deleted for receiverId: " + receiverId), HttpStatus.OK);
        }
    }

    
    
    @DeleteMapping("/notifications/id/{notificationId}")
    public ResponseEntity<Map<String, String>> deleteNotificationById(@PathVariable Long notificationId) {
        Optional<Notification> notificationToDelete = notificationRepository.findById(notificationId);

        if (notificationToDelete.isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("error", "No notification found with id: " + notificationId), HttpStatus.NOT_FOUND);
        } else {
            notificationRepository.deleteById(notificationId);
            return new ResponseEntity<>(Collections.singletonMap("message", "Notification deleted with id: " + notificationId), HttpStatus.OK);
        }
    }
    
    
 // Endpoint to mark notifications as seen for a receiver
    @PostMapping("/notifications/seen/{receiverId}")
    public ResponseEntity<String> markNotificationsAsSeen(@PathVariable String receiverId) {
        List<Notification> notificationsToUpdate = notificationRepository.findByTaggedAndSeen(receiverId, false);

        if (notificationsToUpdate.isEmpty()) {
            return new ResponseEntity<>("No unseen notifications found for receiverId: " + receiverId, HttpStatus.OK);
        } else {
            for (Notification notification : notificationsToUpdate) {
                notification.setSeen(true);
            }
            notificationRepository.saveAll(notificationsToUpdate);
            return new ResponseEntity<>("Notifications marked as seen for receiverId: " + receiverId, HttpStatus.OK);
        }
    }
    
    
    @GetMapping("/notifications/seen/status/{receiverId}")
    public ResponseEntity<Boolean> getNotificationsSeenStatus(@PathVariable String receiverId) {
        try {
            // Assuming you have a method to fetch the notifications seen status from your repository
            boolean notificationsSeen = notificationRepository.areNotificationsSeen(receiverId);
            return ResponseEntity.ok(notificationsSeen);
        } catch (Exception e) {
            // Handle exception if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Example endpoint to fetch unread notifications
    @GetMapping("/notifications/unread/{receiverId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String receiverId) {
        try {
            // Assuming you have a method to fetch unread notifications from your repository
            List<Notification> unreadNotifications = notificationRepository.findByTaggedAndSeen(receiverId, false);
            return ResponseEntity.ok(unreadNotifications);
        } catch (Exception e) {
            // Handle exception if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    // Endpoint to count unseen notifications for a receiver
    @GetMapping("/notifications/unseen/count/{receiverId}")
    public ResponseEntity<Long> countUnseenNotifications(@PathVariable String receiverId) {
        Long unseenNotificationCount = notificationRepository.countByTaggedAndSeen(receiverId, false);
        return ResponseEntity.ok(unseenNotificationCount);
    }

    

    
}

