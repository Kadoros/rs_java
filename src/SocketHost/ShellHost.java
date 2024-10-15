package SocketHost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ShellHost {
    private final BufferedReader socketReader;
    private final BufferedWriter socketWriter;

    public ShellHost(SocketHost socketHost) {
        // Use the socketReader from the shared SocketHost
        socketReader = socketHost.getSocketReader();
        socketWriter = socketHost.getSocketWriter();
        initShell();
    }

    private void initShell() {
        new Thread(() -> {
            try {
                while (true) {
                    String msgFromClient = socketReader.readLine(); // Blocking call
                    if (msgFromClient != null) {
                        System.out.println("Client >>> " + msgFromClient);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start(); // Start a new thread to listen for messages from the client
    }

    public void sendCmd(String command) {
        try {
            System.out.println("Sending command: " + command);
            socketWriter.write(command + "\n");
            socketWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
