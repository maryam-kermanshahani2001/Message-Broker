/*
package Server.handler;

import Client.Client;
import Server.ServerStarter;
import Server.model.Message;
import Server.model.Topic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SubscribeHandler implements Observer {
    private ServerStarter server;
    private Topic topic;
    private Socket socket;
    private ObjectOutputStream oos;
    private ClientHandler clientHandler;

    public SubscribeHandler(ClientHandler clientHandler) throws IOException {
        this.server = clientHandler.getServer();
        this.topic = clientHandler.getTopic();
        this.socket = clientHandler.getSocket();
        this.oos = clientHandler.getObjectOutputStream();
        this.clientHandler = clientHandler;

    }

    public void handleSubscribe(String[] tokens) {
        ArrayList<Topic> topics = server.getTopics();

        for (String str : tokens) {
            if (str.equals(tokens[0]))
                continue;
            for (Topic t : topics) {
                if (t.getName().equals(str)) {
                    this.topic = t;
                    t.addSubscriber(this.socket);
                    t.addObserver(this);

                }
            }
        }

        clientHandler.setRunning(true);
    }

    @Override
    public void update(Observable o, Object message) {
        try {
            if (message != null)
                oos.writeObject((Message) message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // timer
        // ack for resp message
//        this.topic.removeMessageFromQueue();

    }


}
*/
