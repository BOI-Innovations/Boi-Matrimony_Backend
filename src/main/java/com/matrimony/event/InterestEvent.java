package com.matrimony.event;

import com.matrimony.model.entity.Interest;

public class InterestEvent {
    private final Interest interest;
    private final String eventType;

    public InterestEvent(Interest interest, String eventType) {
        this.interest = interest;
        this.eventType = eventType;
    }

    public Interest getInterest() {
        return interest;
    }

    public String getEventType() {
        return eventType;
    }
}