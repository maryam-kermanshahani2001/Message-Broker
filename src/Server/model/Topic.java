package Server.model;

import Client.Client;
import Server.handler.ClientHandler;

import java.net.Socket;
import java.util.*;
import java.util.LinkedList;

public class Topic extends Observable {
    private String name;
    private Queue<Message> messageQueue;
    //    private Client publisherSocket;
    private ArrayList<ClientHandler> subscribers;
    //private final ThreadPoolExecutor threadPool;


    public Topic(String topicName) {
        this.name = topicName;
        this.messageQueue = new LinkedList<>();
        this.subscribers = new ArrayList<>();

    }

    public synchronized void addMessageToQueue(Message msg) {
        if (msg == null)
            return;
        if (messageQueue == null)
            messageQueue = new LinkedList<>();
        messageQueue.add(msg);
        notifyAllSubscribers(msg);
//        notifyObservers(msg); // I wanted to use observer and observable but it doesn't work
    }

    public void notifyAllSubscribers(Message msg) {
        for (ClientHandler c : subscribers) {
            c.sendMessage(msg);
        }
        removeMessageFromQueue();
    }

    public synchronized void removeMessageFromQueue() {
        if (messageQueue == null || messageQueue.size() == 0)
            return;
        messageQueue.remove();
    }

    public synchronized Message peekMessageFromQueue() {
        if (messageQueue == null || messageQueue.size() == 0)
            return null;
        return messageQueue.peek();
    }

    public void addSubscriber(ClientHandler subscriber) {
        subscribers.add(subscriber);
    }

//    public void setProducerSocket(Socket publisherSocket) {
//        this.publisherSocket = publisherSocket;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }
/*
    @Override
    public int hashCode() {
        return Objects.hash(name, messageQueue, publisherSocket, subscribers);
    }*/

    public String getName() {
        return name;
    }
}
