package com.example.emailqueue.queue;

import com.example.emailqueue.model.EmailMessage;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Log4j2
public class EmailQueue {
    private final BlockingQueue<EmailMessage> queue = new LinkedBlockingQueue<>();

    public void enqueue(EmailMessage email) {
        log.info("📥 Queued email to: {}", email.getTo());
        queue.offer(email);
    }

    public EmailMessage dequeue() throws InterruptedException {
        return queue.take();
    }
}
