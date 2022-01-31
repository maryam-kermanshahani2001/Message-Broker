package Server;

import Server.handler.ClientHandler;
import Server.model.Topic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerStarter implements Runnable {
    private final int port;
    private ArrayList<Topic> topics;
    private ArrayList<ClientHandler> clientHandlers;
    private static ServerStarter serverStarter;

    public ServerStarter(int port) {
        this.topics = new ArrayList<>();
        this.clientHandlers = new ArrayList<>();
        this.port = port;
        run();
    }

    // Singleton design pattern
    public static ServerStarter getInstance(int port) {
        if (serverStarter == null) {
            // if serverStarter is null, initialize
            serverStarter = new ServerStarter(port);
        }
        return serverStarter;
    }


    @Override
    public void run() {

        try {
            // listen to the socket
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);
                clientHandler.start();


            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }
}
