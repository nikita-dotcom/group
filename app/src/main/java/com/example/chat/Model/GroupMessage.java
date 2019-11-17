package com.example.chat.Model;

public class GroupMessage {

    private String groupName;
    private String sender;
    private String message;

    public GroupMessage(){ }
    public GroupMessage(String groupName,String sender,String message ) {
        this.groupName=groupName;
        this.sender = sender;
        this.message = message;

    }
    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {

        this.groupName= groupName;
    }

    public String getSender() {

        return sender;
    }
    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
