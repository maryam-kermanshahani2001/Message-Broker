package Server.model;

import Server.model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class WriteThread extends Thread {
    private final ObjectOutputStream oos;
    private Message msg;
//    private long startTime;

    public WriteThread(ObjectOutputStream oos, Message msg) {
        this.oos = oos;
        this.msg = msg;

    }
/*

    public WriteThread(ObjectOutputStream oos, Message msg, long startTime) {
        this.oos = oos;
        this.msg = msg;
        this.startTime = startTime;

    }
*/

    @Override
    public void run() {

        try {
            oos.writeObject(msg);

//            oos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
