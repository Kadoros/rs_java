package Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class SocketClient {
    Socket socket;

    final String IP = "localhost";

    BufferedReader socketReader;
    BufferedWriter socketWriter;

    public SocketClient(String target_IP, int port) {
        initConnection(target_IP, port);
    }

    protected void initConnection(String target_IP, int port) {
        try {
            socket = new Socket(target_IP, port);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
