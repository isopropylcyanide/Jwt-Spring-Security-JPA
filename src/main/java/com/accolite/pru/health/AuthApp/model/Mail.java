package com.accolite.pru.health.AuthApp.model;

import java.util.HashMap;
import java.util.Map;

public class Mail {
    private String from;
    private String to;
    private String subject;
    private String content;
    private Map<String, String> model;

    public Mail() {
        model = new HashMap<>();
    }

    public Mail(String from, String to, String subject, String content, Map<String, String> model) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.model = model;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }
}
