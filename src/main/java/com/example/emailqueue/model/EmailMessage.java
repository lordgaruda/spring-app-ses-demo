package com.example.emailqueue.model;

import java.util.List;

public class EmailMessage {
    private String to;
    private String subject;
    private String body;
    private List<EmailAttachment> attachments;

    public EmailMessage() {}

    public EmailMessage(String to, String subject, String body, List<EmailAttachment> attachments) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public List<EmailAttachment> getAttachments() { return attachments; }
    public void setAttachments(List<EmailAttachment> attachments) { this.attachments = attachments; }
}
