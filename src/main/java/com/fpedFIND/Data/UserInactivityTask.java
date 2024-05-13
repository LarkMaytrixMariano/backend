package com.fpedFIND.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fpedFIND.Entity.User;
import com.fpedFIND.Repository.UserRepository;

@Component
public class UserInactivityTask {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    // Run this method every hour to check for user inactivity
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
   // @Scheduled(fixedRate = 60000) // 1 minute in milliseconds
    public void checkUserInactivity() {
        List<User> users = userRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();

        for (User user : users) {
            LocalDateTime lastActiveTime = user.getLastActive();
            
            // Check if the lastActive timestamp is null
            if (lastActiveTime != null) {
                long hoursSinceLastActive = ChronoUnit.HOURS.between(lastActiveTime, currentTime);
            //    long minutesSinceLastActive = ChronoUnit.MINUTES.between(lastActiveTime, currentTime);

                // Check if the user has been inactive for a certain duration (e.g., 12 hours)
            //    if (minutesSinceLastActive >= 1) {
                if (hoursSinceLastActive >= 5) {
                    // Logout the user
                    user.setStatus("offline");
                    user.setLastActive(null); // Reset last active time
                    userRepository.save(user);
                    messagingTemplate.convertAndSend("/topic/userStatus", user);

                }
            } else {
                user.setStatus("offline");
                user.setLastActive(null); // Reset last active time
                userRepository.save(user);
                messagingTemplate.convertAndSend("/topic/userStatus", user);
            }
        }
    }
}
