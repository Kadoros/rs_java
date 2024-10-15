package SocketClient;

public class Main {
    public static void main(String[] args) {
        String target_IP = "2a02:830a:b006:6800:432f:cd8f:53fe:839c";
        int port = 1000;
        System.out.println("start for " + target_IP);
        SocketClient socketClient = new SocketClient(target_IP, port);
        ShellClient shellClient = new ShellClient(socketClient);
        ScreenCastClient screenCastClient = new ScreenCastClient(socketClient);


    }
}
