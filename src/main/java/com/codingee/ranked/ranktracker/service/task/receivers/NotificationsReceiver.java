package com.codingee.ranked.ranktracker.service.task.receivers;


import com.codingee.ranked.ranktracker.config.RabbitMqConfig;
import com.codingee.ranked.ranktracker.dto.task.NotificationQueueTask;
import com.codingee.ranked.ranktracker.model.client.Client;
import com.codingee.ranked.ranktracker.model.domain.Domain;
import com.codingee.ranked.ranktracker.model.track_request.TrackRequest;
import com.codingee.ranked.ranktracker.service.client.IClientService;
import com.codingee.ranked.ranktracker.service.domain.IDomainService;
import com.codingee.ranked.ranktracker.service.notification.INotificationService;
import com.codingee.ranked.ranktracker.service.track_request.TrackRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationsReceiver {

    private final INotificationService notificationService;
    private final TrackRequestService trackRequestService;
    private final IDomainService domainService;
    private final IClientService clientService;

    private static final String DOMAIN_TRACKED = "Your domain %s has ranked %s. Tap now to check complete rankings";

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATIONS_QUEUE)
    @Transactional
    public void receiveMessage(NotificationQueueTask notificationQueueTask) {
        TrackRequest trackRequest = this.trackRequestService.getTrackRequestById(notificationQueueTask.getTrackRequestId());
        Domain domain = this.domainService.getDomainById(trackRequest.getDomainId());
        Client client = this.clientService.getClient(domain.getClientId());
        Set<String> deviceIds = client.getDeviceIds();
        notificationService.sendNotification(deviceIds.stream().toList(), new INotificationService.NotificationBody(
                String.format(DOMAIN_TRACKED, domain.getName(), trackRequest.getAverageRank()),
                domain.getName(),
                null,
                null
        ));
    }

}
