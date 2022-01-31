/*
package Server.handler;

import Server.ServerStarter;
import Server.model.Message;
import Server.model.Topic;

import java.util.ArrayList;

public class PublisherHandler {
    private ServerStarter server;
    private Topic topic;
    private ClientHandler clientHandler;

    public PublisherHandler(ClientHandler clientHandler) {
        this.server = clientHandler.getServer();
        this.clientHandler = clientHandler;

    }

    public void handlePublish(String[] tokens) {
        ArrayList<Topic> topics = server.getTopics();
        if (topics.size() != 0) {
            for (Topic t : topics) {
                if (t.getName().equals(tokens[1])) {
                    this.topic = t;
                    break;
                }
            }
        } else {
            this.topic = new Topic(tokens[1]);
            server.addTopic(topic);
        }

        String msgText = tokens[2];
        topic.addMessageToQueue(new Message(msgText));
        clientHandler.setRunning(true);
    }

}
*/
