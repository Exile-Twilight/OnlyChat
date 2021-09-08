package com.example.onlychat;

public class Msg {
    String sender ;
    String sender_id;
    String contents ;
    String contentstype ;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContentstype() {
        return contentstype;
    }

    public void setContentstype(String contentstype) {
        this.contentstype = contentstype;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    String sendtime ;

    public Msg(String sender, String sender_id, String contents, String contentstype) {
        this.sender = sender;
        this.sender_id = sender_id;
        this.contents = contents;
        this.contentstype = contentstype;
    }
}
