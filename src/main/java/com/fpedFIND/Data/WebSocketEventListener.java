package com.fpedFIND.Data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.fpedFIND.Entity.Message;
import com.fpedFIND.Entity.MessageType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	private final SimpMessageSendingOperations messageTemplate = null;
	
    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

	@EventListener
	public void handleWebSocketDisconnectListener(
			SessionDisconnectEvent event
			) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username =(String) headerAccessor.getSessionAttributes().get("username");
		if(username != null) {
			log.info("User disconneted: {}", username);
			var chatMessage = Message.builder()
					.type(MessageType.LEAVE)
					.sender(username)
					.build();
			messageTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}
}
