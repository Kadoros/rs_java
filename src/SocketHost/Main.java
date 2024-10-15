package SocketHost;

public class Main {
    public static void main(String[] args) {
        int port = 1000; // Single port for both services

        // Create a single SocketHost
        SocketHost socketHost = new SocketHost(port);

        // Pass the same SocketHost to both ScreenCastHost and ShellHost
        ScreenCastHost screenCastHost = new ScreenCastHost(socketHost);
        ShellHost shellHost = new ShellHost(socketHost);

        // Optionally, send commands through ShellHost
        shellHost.sendCmd("dir");


    }
}
