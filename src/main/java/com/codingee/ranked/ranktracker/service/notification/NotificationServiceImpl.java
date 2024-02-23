package com.codingee.ranked.ranktracker.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService{

    record FCMNotificationRequest(Message message) {
        record Message(String token, Notification notification, Data data) {
            record Notification(String title, String body) {

            }

            record Data(String type, String url) {

            }
        }
    }


    @Autowired
    private GoogleCredential googleCredentials;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${fcm.api}")
    private String FCM_API;
    @Value("${fcm.sender.id}")
    private String SENDER_ID;

    @Override
    public boolean sendNotification(List<String> deviceIds, NotificationBody notificationBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + this.getAccessToken());
            headers.set("project_id", SENDER_ID);

            deviceIds.forEach(di -> {
                try {

                    FCMNotificationRequest request = new FCMNotificationRequest(
                            new FCMNotificationRequest.Message(
                                    di, new FCMNotificationRequest.Message.Notification(
                                            notificationBody.getTitle(), notificationBody.getMessage()
                            ), new FCMNotificationRequest.Message.Data(notificationBody.getData(), notificationBody.getData())));

                    String jsonBody = objectMapper.writeValueAsString(request);
                    HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> responseEntity = restTemplate.exchange(FCM_API, HttpMethod.POST, requestEntity, String.class);

                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        System.out.println("Message sent successfully!");
                    } else {
                        System.out.println("Error sending message: " + responseEntity.getBody());
                    }
                } catch (Exception e) {
                    System.out.println("Error sending message: " + e.getMessage());
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    private String getAccessToken() throws IOException {
        googleCredentials.refreshToken();
        return googleCredentials.getAccessToken();
    }


}
