package com.example.emailqueue.controller;

import com.example.emailqueue.model.EmailMessage;
import com.example.emailqueue.queue.EmailQueue;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailQueue emailQueue;

    public EmailController(EmailQueue emailQueue) {
        this.emailQueue = emailQueue;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailMessage emailMessage) {
        emailQueue.enqueue(emailMessage);
        return "📨 Queued email to: " + emailMessage.getTo();
    }

    @PostMapping("/send-batch")
    public String sendBatch(@RequestBody List<EmailMessage> emailMessages) {
        emailMessages.forEach(emailQueue::enqueue);
        return "📨 Queued " + emailMessages.size() + " emails";
}
}
