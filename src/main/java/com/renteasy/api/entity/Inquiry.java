package com.renteasy.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "inquiries")
public class Inquiry {

    @Id
    private String id;

    private String listingId;
    private String senderId;
    private String message;
    private boolean responded;
    private LocalDateTime timestamp;

    // 👉 Add the missing setter here
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // 🔁 Getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResponded() {
        return responded;
    }

    public void setResponded(boolean responded) {
        this.responded = responded;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
