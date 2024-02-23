package com.codingee.ranked.ranktracker.service.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public interface INotificationService {

    @Getter
    @Setter
    @AllArgsConstructor
    static class NotificationBody {
        String message;
        String title;
        String imgUrl;
        String data;
    }
    boolean sendNotification(List<String> deviceId, NotificationBody notificationBody);

}
