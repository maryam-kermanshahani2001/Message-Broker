package Server.model;

import java.io.Serializable;

public class Message implements Serializable {
    //private MessageType msgType;
    private String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


/*
    public MessageType getMsgType() {
        return msgType;
    }
*/

//    public void setMsgType(MessageType msgType) {
//        this.msgType = msgType;
//    }

    public void setText(String text) {
        this.text = text;
    }

}
