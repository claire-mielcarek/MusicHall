package com.projet.musichall.discussion;

import java.util.Date;

public class MessageChat {

    private String messageText;
    private String messageUser;
    private long messageTime;

    public MessageChat(String messageText, String messageUser){
        this.messageText = messageText;
        this.messageUser = messageUser;

        messageTime =  new Date().getTime();

    }

    @Override
    public String toString() {
        return "Message{" +
                " writer ='" + messageUser + '\'' +
                ", date ='" + messageTime + '\'' +
                ", content ='" + messageText + '\'' +
                '}';
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
