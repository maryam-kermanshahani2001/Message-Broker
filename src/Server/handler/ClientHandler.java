package Server.handler;

import Server.ServerStarter;
import Server.model.Message;
import Server.model.Topic;
import Server.model.WriteThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClientHandler extends Thread implements Observer {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    //    private ArrayList<Topic> topics;
    private ServerStarter server;
    private Topic topic;
    private boolean isRunning;

    public Socket getSocket() {
        return socket;
    }

    public ClientHandler(Socket socket, ServerStarter server) throws IOException {
        this.socket = socket;
        this.server = server;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        isRunning = true;

    }
//
//    public synchronized void addTopic(Topic topic) {
//        topics.add(topic);
//    }


    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        Message msg = null;
        try {
            while (isRunning) {
                msg = (Message) ois.readObject();
                String text = msg.getText();
                String[] tokens = text.split(" ");
                String cmd = tokens[0];
                switch (cmd) {
                    case "publish":
                        isRunning = false;
                        sendMessage((new Message("PubAck")));
                        handlePublish(tokens);
                        break;
                    case "subscribe":
                        sendMessage((new Message("SubAck")));
                        isRunning = false;
                        handleSubscribe(tokens);
                        break;
                    case "Ack":
                        System.out.println("Ack received" + this);
                    case "exit":
                        terminate();
                    case "ping":
                        sendMessage(new Message("pong"));

                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void handleSubscribe(String[] tokens) {
        ArrayList<Topic> topics = server.getTopics();

        for (String str : tokens) {
            if (str.equals(tokens[0]))
                continue;
            for (Topic t : topics) {
                if (t.getName().equals(str)) {
                    this.topic = t;
                    t.addSubscriber(this);
                    t.addObserver(this);

                }
            }
        }
        isRunning = true;

    }

    public void sendMessage(Message msg) {
        try {
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePublish(String[] tokens) {
        ArrayList<Topic> topics = server.getTopics();

//       @TODO solve the bug in here!
        boolean found = false;
        if (topics.size() != 0) {
            for (Topic t : topics) {
                if (t.getName().equals(tokens[1])) {
                    this.topic = t;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            this.topic = new Topic(tokens[1]);
            server.addTopic(topic);
        }

        String msgText = topic.getName() + ": " + tokens[2];

        topic.addMessageToQueue(new Message(msgText));
        isRunning = true;
    }

    public void terminate() {
        sendMessage(new Message("exit"));
        server.removeClient(this);
        isRunning = false;
    }


    public Topic getTopic() {
        return topic;
    }

    public ServerStarter getServer() {
        return server;
    }

    public ObjectInputStream getObjectInputStream() {
        return ois;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return oos;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void update(Observable o, Object message) {
        if (message != null)
            sendMessage((Message) message);
        // timer
        // ack for resp message
//        this.topic.removeMessageFromQueue();

    }

}


/*

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

*/
