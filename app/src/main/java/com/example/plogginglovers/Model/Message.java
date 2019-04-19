package com.example.plogginglovers.Model;

import java.util.Date;

public class Message {
    private String nickname;
    private String message;
    private long messageTime;

    public Message() {
    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;

        this.messageTime = new Date().getTime();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
