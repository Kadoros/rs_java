package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient extends SocketClient {
    BufferedReader keyboardReader;

    public ChatClient(String target_IP, int port) {
        super(target_IP, port);
        initChat();
    }


    public void initChat() {
        try {
            keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            new Thread(new ReadThread()).start();
            while (true) {
                System.out.println("키보드 입력 대기");
                String input = keyboardReader.readLine(); // 키보드에서 데이터 입력 받음
                socketWriter.write(input);
                socketWriter.newLine(); // 문장의 끝을 알려주어야 한다.
                socketWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ReadThread implements Runnable {

        @Override
        public void run() {
            // 소켓 통신으로 들어온 데이터를 읽어야 함
            while (true) {
                try {
                    // readLine() : 글을 읽는 기능
                    String serverMsg = socketReader.readLine();
                    System.out.println("서버 >>> " + serverMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
