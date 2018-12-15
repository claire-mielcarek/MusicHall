package com.projet.musichall.discussion;

import java.util.Date;

public class MessageChat {

    private String messageText;
    private String nomSender;
    private long messageTime;

    public MessageChat(String messageText, String messageUser){
        this.messageText = messageText;
        this.nomSender = messageUser;

        messageTime =  new Date().getTime();

    }

    /*@Override
    public String afficher() {
        return "Message{" +
                " writer ='" + messageUser + '\'' +
                ", date ='" + messageTime + '\'' +
                ", content ='" + messageText + '\'' +
                '}';
    }
    */

    public String getNomSender() {
        return nomSender;
    }

    public void setNomSender(String messageUser) {
        this.nomSender = messageUser;
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
