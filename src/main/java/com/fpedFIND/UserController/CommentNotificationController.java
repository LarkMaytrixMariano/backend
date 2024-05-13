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

import com.fpedFIND.Entity.CommentNotification;
import com.fpedFIND.Entity.File;
import com.fpedFIND.Entity.Notification;
import com.fpedFIND.Repository.CommentNotificationRepository;
import com.fpedFIND.Repository.FileRepository;
import com.fpedFIND.Repository.UserRepository;


@RestController
@CrossOrigin(origins = "*")
public class CommentNotificationController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    
    @Autowired
    private CommentNotificationRepository notificationRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
 
	
    @MessageMapping("/sendCommentNotification")
    public void sendAdminAlert(CommentNotification payload) {
        String uploaderId = payload.getUploaderId();
        String message = payload.getMessage();
        String sender = payload.getCommentor();
        String filename = payload.getFilename();
        Integer fileId = payload.getFileId();
        LocalDateTime timestamp = payload.getTimestamp();    

        // Save the notification to the database with "seen" set to false
        payload.setSeen(false);

	        // Retrieve the tagged users associated with the file
	        File file = fileRepository.findByFileId(fileId);
	        if (file != null) {
	            String taggedUsers = file.getTaggedUsers();
	         
            // Notify the uploader if the commenter is not the uploader
            if (!uploaderId.equals(sender)) {
                CommentNotification uploaderNotification = new CommentNotification();
                uploaderNotification.setUploaderId(uploaderId);
                uploaderNotification.setMessage(message);
                uploaderNotification.setCommentor(sender);
                uploaderNotification.setFilename(filename);
                uploaderNotification.setFileId(fileId);
                uploaderNotification.setTimestamp(timestamp);
                notificationRepository.save(uploaderNotification);
                messagingTemplate.convertAndSendToUser(uploaderId, "/queue/CommentNotification", uploaderNotification);
            }

            // Notify the tagged users, excluding the commenter
            if (taggedUsers != null && !taggedUsers.isEmpty()) {
                List<String> taggedUserIds = Arrays.asList(taggedUsers.split(",")); // Assuming user IDs are separated by comma
                for (String taggedUserId : taggedUserIds) {
                    // Check if the tagged user is not the commenter
                    if (!taggedUserId.equals(sender)) {
                        CommentNotification taggedUserNotification = new CommentNotification();
                        taggedUserNotification.setUploaderId(taggedUserId);
                        taggedUserNotification.setMessage(message);
                        taggedUserNotification.setCommentor(sender);
                        taggedUserNotification.setFilename(filename);
                        taggedUserNotification.setFileId(fileId);
                        taggedUserNotification.setTimestamp(timestamp);
                        notificationRepository.save(taggedUserNotification);
                        messagingTemplate.convertAndSendToUser(taggedUserId, "/queue/CommentNotification", taggedUserNotification);
                    }
                }
            }
        }
    }

	
	  @GetMapping("/commentNotifications/{uploaderId}")
	    public ResponseEntity<List<CommentNotification>> getCommentNotificationsByTaggedAndUploaderId(@PathVariable String uploaderId) {
	        List<CommentNotification> notifications = notificationRepository.findByUploaderId(uploaderId);
	        return ResponseEntity.ok().body(notifications);
	    }
	  
	  
	  @GetMapping("/commentnotifications/unseen/count/{uploaderId}")
	    public ResponseEntity<Long> countUnseenNotifications(@PathVariable String uploaderId) {
	        Long unseenNotificationCount = notificationRepository.countByTaggedAndSeen(uploaderId, false);
	        return ResponseEntity.ok(unseenNotificationCount);
	    }

	  // Endpoint to mark notifications as seen for a receiver
	    @PostMapping("/commentnotifications/seen/{uploaderId}")
	    public ResponseEntity<String> markNotificationsAsSeen(@PathVariable String uploaderId) {
	        List<CommentNotification> notificationsToUpdate = notificationRepository.findByUploaderIdAndSeen(uploaderId, false);

	        if (notificationsToUpdate.isEmpty()) {
	            return new ResponseEntity<>("No unseen notifications found for receiverId: " + uploaderId, HttpStatus.OK);
	        } else {
	            for (CommentNotification notification : notificationsToUpdate) {	
	                notification.setSeen(true);
	            }
	            notificationRepository.saveAll(notificationsToUpdate);
	            return new ResponseEntity<>("Notifications marked as seen for receiverId: " + uploaderId, HttpStatus.OK);
	        }
	    }
	    
	    
	    @DeleteMapping("/commentnotifications/{uploaderId}")
	    public ResponseEntity<Map<String, String>> deleteNotificationsByReceiverId(@PathVariable String uploaderId) {
	        List<CommentNotification> notificationsToDelete = notificationRepository.findByUploaderId(uploaderId);

	        if (notificationsToDelete.isEmpty()) {
	            return new ResponseEntity<>(Collections.singletonMap("error", "No notifications found for receiverId: " + uploaderId), HttpStatus.NOT_FOUND);
	        } else {
	            notificationRepository.deleteAll(notificationsToDelete);
	            return new ResponseEntity<>(Collections.singletonMap("message", "Notifications deleted for receiverId: " + uploaderId), HttpStatus.OK);
	        }
	    }

	    
	    
	    @DeleteMapping("/commentnotifications/id/{notificationId}")
	    public ResponseEntity<Map<String, String>> deleteNotificationById(@PathVariable Long notificationId) {
	        Optional<CommentNotification> notificationToDelete = notificationRepository.findById(notificationId);

	        if (notificationToDelete.isEmpty()) {
	            return new ResponseEntity<>(Collections.singletonMap("error", "No notification found with id: " + notificationId), HttpStatus.NOT_FOUND);
	        } else {
	            notificationRepository.deleteById(notificationId);
	            return new ResponseEntity<>(Collections.singletonMap("message", "Notification deleted with id: " + notificationId), HttpStatus.OK);
	        }
	    }
	
	
	
	}
