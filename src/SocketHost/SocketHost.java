package SocketHost;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHost {
    ServerSocket serverSocket;
    Socket socket;

    BufferedReader socketReader;
    BufferedWriter socketWriter;

    public SocketHost(int port) {
        initConnection(port);
    }

    protected void initConnection(int port) {
        try {
            // Log the server's local IP address
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on IP: " + serverSocket.getInetAddress().getHostAddress() + " and port: " + port);

            // Wait for a client connection and log the client's IP address
            socket = serverSocket.accept();
            System.out.println("Client connected from IP: " + socket.getInetAddress().getHostAddress());

            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    public BufferedWriter getSocketWriter() {
        return socketWriter;
    }

    public Socket getSocket() {
        return socket;
    }

}
