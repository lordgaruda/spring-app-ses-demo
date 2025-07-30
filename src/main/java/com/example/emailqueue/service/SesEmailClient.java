package com.example.emailqueue.service;

import com.example.emailqueue.model.EmailMessage;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SesEmailClient {

    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private int smtpPort;
    @Value("${mail.smtp.username}")
    private String smtpUser;
    @Value("${mail.smtp.password}")
    private String smtpPass;
    @Value("${ses.sender.email}")
    private String from;

    private Session session;
    private Transport transport;

    private synchronized void ensureConnected() throws Exception {
        if (session == null) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            session = Session.getInstance(props);
        }
        if (transport == null || !transport.isConnected()) {
            transport = session.getTransport("smtp");
            transport.connect(smtpHost, smtpPort, smtpUser, smtpPass);
            System.out.println("✅ SMTP Connected");
        }
    }

    public synchronized void sendEmail(EmailMessage email) throws Exception {
        ensureConnected();

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
        message.setSubject(email.getSubject());
        message.setText(email.getBody());

        transport.sendMessage(message, message.getAllRecipients());
    }
}
