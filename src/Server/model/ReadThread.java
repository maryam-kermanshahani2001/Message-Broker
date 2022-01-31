package Server.model;

import Client.Client;
import Server.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.MessageDigest;

public class ReadThread extends Thread {
    private final ObjectInputStream ois;
    private Message responseMessage;
    private Message receivedMessage;
    private Client client;
    private boolean readthreadIsAlive;

    public ReadThread(ObjectInputStream ois, Client client) {
        this.ois = ois;
        this.client = client;
        this.readthreadIsAlive = true;

    }

    public void setAlive(boolean alive) {
        readthreadIsAlive = alive;
    }

    @Override
    public void run() {
        try {
            while (readthreadIsAlive) {
                this.receivedMessage = (Message) ois.readObject();
                if (receivedMessage.getText().equals("ping")) {
                    responseMessage = new Message("pong");
                } else if (receivedMessage.getText().equals("exit")) {
                    client.setAlive(false);
                    break;
                } else {
                    responseMessage = new Message("Ack");
                    client.printMessage(receivedMessage);
                    client.setMsgAcked(true);
                    //responseMessage.setMsgType(receivedMessage.getMsgType());}
                }
//                if ("PubAck")

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
