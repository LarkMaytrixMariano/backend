package com.fpedFIND.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fpedFIND.Entity.AdminAlert;

@Controller
@CrossOrigin(origins = "*")
public class AdminAlertController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendAdminAlert")
    public void sendAdminAlert(AdminAlert payload) {
        String receiverId = payload.getReceiver();
        payload.getMessage();
        payload.getSender();

        System.out.println("Received admin alert request: " + payload.toString());

        if ("all".equals(receiverId)) {
            messagingTemplate.convertAndSend("/topic/adminAlerts", payload);
            //System.out.println("Alert broadcasted to all users: " + payload.toString());
        } else {
            messagingTemplate.convertAndSendToUser(receiverId, "/queue/adminAlerts", payload);
          //  System.out.println("Alert sent to user " + receiverId + ": " + payload.toString());
        }
    }
}
