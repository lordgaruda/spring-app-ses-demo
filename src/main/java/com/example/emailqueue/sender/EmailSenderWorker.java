package com.example.emailqueue.sender;

import com.example.emailqueue.model.EmailMessage;
import com.example.emailqueue.queue.EmailQueue;
import com.example.emailqueue.service.SesEmailClient;
import com.google.common.util.concurrent.RateLimiter;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5); // 5 parallel workers
        RateLimiter rateLimiter = RateLimiter.create(14.0); // 14 emails/sec

        executor.submit(() -> {
            while (true) {
                try {
                    EmailMessage email = emailQueue.dequeue();

                    // Acquire a permit (waits if limit reached)
                    rateLimiter.acquire();

                    executor.submit(() -> {
                        try {
                            sesEmailClient.sendEmail(email);
                            System.out.println("✅ Sent: " + email.getSubject());
                        } catch (Exception e) {
                            System.err.println("❌ Error sending to " + email.getTo() + ": " + e.getMessage());
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

}
