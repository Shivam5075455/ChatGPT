package com.example.chatgpt;

public class MessageModel {

    String message, sentBy;

    public static String SEND_BY_USER = "user";
    public static String SEND_BY_BOT = "bot";

    public MessageModel(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
