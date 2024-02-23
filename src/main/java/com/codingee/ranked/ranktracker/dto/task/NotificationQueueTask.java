package com.codingee.ranked.ranktracker.dto.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationQueueTask {

    public NotificationQueueTask(ENotificationType type, Long trackRequestId) {
        this.type = type;
        this.trackRequestId = trackRequestId;
    }

    public enum ENotificationType {
        TRACK_JOB_COMPLETED
    }

    private String id;
    private ENotificationType type;
    private Long trackRequestId;

}
