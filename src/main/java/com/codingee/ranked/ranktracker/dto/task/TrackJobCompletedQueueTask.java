package com.codingee.ranked.ranktracker.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrackJobCompletedQueueTask {
    private String id;
    private String trackJobId;
    private String tag;

    public TrackJobCompletedQueueTask(String trackJobId, String tag) {
        this.id = UUID.randomUUID().toString();
        this.trackJobId = trackJobId;
        this.tag = tag;
    }
}
