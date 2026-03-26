package com.matrimony.listener;

import com.matrimony.event.MessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageEventListener {

    @EventListener
    @Async
    public void handleMessageEvent(MessageEvent event) {
        // Handle message events (e.g., push notifications, websocket messages)
        if ("SENT".equals(event.getEventType())) {
            // Send real-time notification to the recipient
            // This could be implemented with WebSocket or push notification service
        }
    }
}