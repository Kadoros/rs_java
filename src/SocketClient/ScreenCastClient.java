package SocketClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ScreenCastClient  {
    final int w = Toolkit.getDefaultToolkit().getScreenSize().width;
    final int h = Toolkit.getDefaultToolkit().getScreenSize().height;

    BufferedImage image;
    BufferedOutputStream socketByteOutStream;
    DataInputStream mouseInputStream;
    Socket socket;
    Robot r;

    public ScreenCastClient(SocketClient socketClient) {
        socket = socketClient.getSocket();
        initStreams(); // Initialize streams here
    }

    private void initStreams() {
        try {
            socketByteOutStream = new BufferedOutputStream(socket.getOutputStream());
            mouseInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing streams", e);
        }
    }

    public void sendScreenCast() {
        try {
            r = new Robot();
            long frameInterval = 1000 / 30;  // 30 FPS

            while (true) {
                long startTime = System.currentTimeMillis();  // Record the start time of the frame

                // Capture the screen
                image = r.createScreenCapture(new Rectangle(0, 0, w, h));

                // Send the image to the server
                ImageIO.write(image, "bmp", socketByteOutStream);
                socketByteOutStream.flush();

                // Calculate the time taken for the capture and transmission
                long elapsedTime = System.currentTimeMillis() - startTime;

                // Calculate the remaining time to wait to maintain 30 FPS
                long sleepTime = frameInterval - elapsedTime;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }

                // Receive and handle mouse coordinates
                receiveMouseCoordinates();
            }
        } catch (IOException | AWTException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveMouseCoordinates() {
        try {
            int mouseX = mouseInputStream.readInt(); // Read X coordinate
            int mouseY = mouseInputStream.readInt(); // Read Y coordinate
            r.mouseMove(mouseX,mouseY);
            System.out.println("Received mouse coordinates: (" + mouseX + ", " + mouseY + ")");
            // Optional: Update any UI or other components based on mouse coordinates
        } catch (IOException e) {
            e.printStackTrace();
            // Handle potential errors
        }
    }




}
