package Client;

import Server.model.ReadThread;
import Server.model.Message;
import Server.model.WriteThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String hostname;
    private int port;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private boolean msgAcked;
    private long startTime;
    private final long timeout = 10000L;
    private boolean isAlive;

    public Client(String hostname, int port) {
        msgAcked = false;
        this.hostname = hostname;
        this.port = port;
        try {
            Socket socket = new Socket(hostname, port);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void printMessage(Message msg) {
        System.out.println(msg.getText());
    }

    public void execute() {
        System.out.println("Connected to the chat server");
        ReadThread readThread = new ReadThread(objectInputStream, this);
        readThread.start();


        String text = "";
        isAlive = true;
        while (isAlive) {
            Scanner sc = new Scanner(System.in);
            text = sc.nextLine();
            String[] tokens = text.split(" ");
            Message msg = new Message(text);

            startTime = System.currentTimeMillis();
            WriteThread writeThread = new WriteThread(objectOutputStream, msg);
            writeThread.start();

            String cmd = tokens[0];

            // 10 second timer that client must receive ACK messaage
            // and if it doesn't get the ack after 10 second it ends the socket
            if (cmd.equals("publish") || cmd.equals("sub")) {
                if (!msgAcked && System.currentTimeMillis() - startTime > timeout) {
                    System.out.println("Server doesn't respond");
                    try {
                        objectOutputStream.writeObject(new Message("exit"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    readThread.setAlive(false);
                    break;
                }
            }
            if (cmd.equals("exit")) {
                break;
            }

        }
        System.out.println("Goodbye");
//        Message msg = (Message) objectInputStream.readObject();
//        System.out.println(msg.getText() + " ");

    }

    public void setMsgAcked(boolean msgAcked) {
        this.msgAcked = msgAcked;
    }

    public static void main(String[] args) {


        System.out.println("Enter the server that you want to connect");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        String[] tokens = text.split(" ");
        String hostname = tokens[0];
        int port = Integer.parseInt(tokens[1]);


        Client client = new Client(hostname, port);
        client.execute();
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
