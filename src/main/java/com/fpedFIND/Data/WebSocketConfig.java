package com.fpedFIND.Data;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/user", "/comments", "/deadline", "/events"); // Enables destinations for topics, queues, and user-specific queues
        config.setApplicationDestinationPrefixes("/app"); // Messages whose destination starts with "/app" are routed to message-handling methods
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .setAllowedOriginPatterns("http://localhost:4200/", "http://172.16.52.78:8888/", "http://172.16.52.78/", "http://find.bfar.da.gov.ph/", "http://172.16.52.77:8888/", "http://localhost:54299/")
                .withSockJS()
                .setHeartbeatTime(60000); // Set the interval to send heartbeats from the server to the client (in milliseconds)
    }
    

    
    

    
}