package com.example.emailqueue.sender;

import com.example.emailqueue.model.EmailMessage;
import com.example.emailqueue.queue.EmailQueue;
import com.example.emailqueue.service.SesEmailClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailSenderWorker {

    private final EmailQueue emailQueue;
    private final SesEmailClient sesEmailClient;

    public EmailSenderWorker(EmailQueue emailQueue, SesEmailClient sesEmailClient) {
        this.emailQueue = emailQueue;
        this.sesEmailClient = sesEmailClient;
    }

    @PostConstruct
    public void startWorker() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (int i = 0; i < 14; i++) {
                    EmailMessage email = emailQueue.dequeue();
                    sesEmailClient.sendEmail(email);
                    System.out.println("✅ Sent: " + email.getSubject());
                }
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

}
