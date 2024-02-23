package com.codingee.ranked.ranktracker.service.email;

public interface IEmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String htmlContent);

    void sendHtmlMessageWithAttachment(
            String to, String subject, String htmlContent, String attachmentPath, String attachmentName
    );
}
