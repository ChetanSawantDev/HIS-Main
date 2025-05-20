package com.his.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class JavaMailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String userEmail, String uniqueReportId) {
        try {

            String downloadUrl = "http://localhost:9090/reportService/printReport?reportId=" + uniqueReportId;

            String subject = "Your Report is Ready";
            String body = "Dear user,\n\nYour requested report is ready. You can download it using the link below:\n"
                    + downloadUrl + "\n\nThanks,\nHospital Management System";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
