package com.codingee.ranked.ranktracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TrackRequestQueueTask {


    private Long trackRequestId;
    private String id;
    private Long domainId;
    private Long clientId;

    public TrackRequestQueueTask() {}

    public TrackRequestQueueTask(Long trackRequestId, Long domainId, Long clientId) {
        this.trackRequestId = trackRequestId;
        this.domainId = domainId;
        this.clientId = clientId;
        this.id = UUID.randomUUID().toString();
    }



}