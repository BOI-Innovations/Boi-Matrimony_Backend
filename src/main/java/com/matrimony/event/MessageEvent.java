package com.matrimony.event;

import com.matrimony.model.entity.Message;

public class MessageEvent {
    private final Message message;
    private final String eventType;

    public MessageEvent(Message message, String eventType) {
        this.message = message;
        this.eventType = eventType;
    }

    public Message getMessage() {
        return message;
    }

    public String getEventType() {
        return eventType;
    }
}