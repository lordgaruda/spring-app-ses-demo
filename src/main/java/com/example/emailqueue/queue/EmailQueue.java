package com.example.emailqueue.queue;

import com.example.emailqueue.model.EmailMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EmailQueue {
    private final BlockingQueue<EmailMessage> queue = new LinkedBlockingQueue<>();

    public void enqueue(EmailMessage email) {
        queue.offer(email);
    }

    public EmailMessage dequeue() throws InterruptedException {
        return queue.take();
    }
}
