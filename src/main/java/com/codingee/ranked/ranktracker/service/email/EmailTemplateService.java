package com.codingee.ranked.ranktracker.service.email;

import com.codingee.ranked.ranktracker.dto.email.PasswordResetEmailDTO;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

@Service
public class EmailTemplateService {


    @Autowired
    private IEmailService emailService;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${client.url}")
    private String appUrl;

//    public void sendOrderPlacedEmail(String customerName, String customerEmail, String orderId) {
//        String subject = "Your Order Confirmation";
//        String templatePath = "templates/order_placed_email_template.vm";
//
//        // Create a VelocityContext and add dynamic data
//        VelocityContext velocityContext = new VelocityContext();
//        velocityContext.put("customerName", customerName);
//        velocityContext.put("orderId", orderId);
//
//        // Merge the template with the context
//        Template template = velocityEngine.getTemplate(templatePath);
//        StringWriter writer = new StringWriter();
//        template.merge(velocityContext, writer);
//
//        String htmlContent = writer.toString();
//
//        emailService.sendHtmlMessage(customerEmail, subject, htmlContent);
//    }

    public void sendEmailVerificationEmail() {

    }

    public void sendForgetPasswordEmail(String to, String name, PasswordResetEmailDTO passwordResetDTO) {
        String subject = "Reset your ZRankIt password";
        String templatePath = "templates/password_reset_template.vm";
        try {
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("name", name);
            velocityContext.put("token", String.format("%s/auth/reset-password?token=%s&expiry=%s", appUrl, passwordResetDTO.token(), passwordResetDTO.expiryTime()));
            Template template = velocityEngine.getTemplate(templatePath);
            StringWriter writer = new StringWriter();
            template.merge(velocityContext, writer);
            String htmlContent = writer.toString();
            this.emailService.sendHtmlMessage(to, subject, htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
