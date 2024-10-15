package SocketHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatHost extends SocketHost {

    BufferedReader keyboardReader;

    public ChatHost(int port) {
        super(port);
        initChat();
    }

    private void initChat() {
        try {
            System.out.println("Chat started. Waiting for messages...");
            keyboardReader = new BufferedReader(new InputStreamReader(System.in));

            // Start a new thread to handle sending messages from the server to the client
            WriterThread writerThread = new WriterThread();
            Thread thread = new Thread(writerThread);
            thread.start();

            // Continuously listen for messages from the client
            while (true) {
                String msgFromClient = socketReader.readLine(); // Blocking call, waits for input from client
                if (msgFromClient != null) {
                    System.out.println("Client >>> " + msgFromClient);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class WriterThread implements Runnable {
        @Override
        public void run() {
            // Continuously read from the keyboard and send messages to the client
            while (true) {
                try {
                    String serverMsg = "Server >>> " + keyboardReader.readLine();
                    System.out.println("Sending: " + serverMsg);
                    socketWriter.write(serverMsg + "\n");
                    socketWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
