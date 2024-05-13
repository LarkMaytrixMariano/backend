package com.fpedFIND.UserController;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fpedFIND.Entity.Message;


@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    @MessageMapping("/chat") // Endpoint to handle incoming messages from clients
    @SendTo("/topic/public") // Send the message to all subscribed clients on "/topic/public"
    public Message send(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        // Get sender's information from headerAccessor (e.g., username, user ID)
        // Example: String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        // Modify the message object or database storage to include sender's info
        // message.setSender(username);

        // Handle the incoming message, save it to the database if needed with sender's info
        return message;
    }
}
