package com.codingee.ranked.ranktracker.service.task;

import com.codingee.ranked.ranktracker.dto.task.NotificationQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackJobCompletedQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackRequestQueueTask;

public interface ITaskSchedularService {
    String sendTrackRequest(TrackRequestQueueTask task);

    void processTrackJobCompleted(TrackJobCompletedQueueTask task);

    void sendNotification(NotificationQueueTask task);
}
