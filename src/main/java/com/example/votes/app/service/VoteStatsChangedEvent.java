package com.example.votes.app.service;

import org.springframework.context.ApplicationEvent;

public class VoteStatsChangedEvent extends ApplicationEvent {
    public VoteStatsChangedEvent(Object source) {
        super(source);
    }
}
