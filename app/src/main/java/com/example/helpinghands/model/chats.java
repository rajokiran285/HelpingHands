package com.example.helpinghands.model;

public class chats {

    String Sender,Receiver,Message;

    public chats(String Sender, String Receiver, String Message) {
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.Message = Message;
    }

    public chats() {
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
