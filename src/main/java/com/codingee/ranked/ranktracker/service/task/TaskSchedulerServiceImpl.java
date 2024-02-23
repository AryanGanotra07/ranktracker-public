package com.codingee.ranked.ranktracker.service.task;

import com.codingee.ranked.ranktracker.config.RabbitMqConfig;
import com.codingee.ranked.ranktracker.dto.task.NotificationQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackJobCompletedQueueTask;
import com.codingee.ranked.ranktracker.dto.task.TrackRequestQueueTask;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskSchedulerServiceImpl implements ITaskSchedularService {

    private final RabbitTemplate rabbitTemplate;


    @Override
    public String sendTrackRequest(TrackRequestQueueTask task) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TRACK_REQUEST_QUEUE, task);
        return task.getId();
    }

    @Override
    public void processTrackJobCompleted(TrackJobCompletedQueueTask task) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TRACK_JOB_COMPLETED_QUEUE, task);
    }

    @Override
    public void sendNotification(NotificationQueueTask task) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.NOTIFICATIONS_QUEUE, task);
    }
}