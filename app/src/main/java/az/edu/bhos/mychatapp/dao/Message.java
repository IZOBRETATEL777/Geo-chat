package az.edu.bhos.mychatapp.dao;

import java.util.Date;

public class Message {
    public String userName;
    public String textMessage;
    private long messageTime;
    private String location;

    public Message() {}

    public Message(String userName, String textMessage, String location) {
        this.userName = userName;
        this.textMessage = textMessage;
        this.location = location;
        this.messageTime = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public String getTextMessage() {
        return textMessage;
    }
    public String getLocation() {
        return location;
    }

    public long getMessageTime() {
        return messageTime;
    }

}
