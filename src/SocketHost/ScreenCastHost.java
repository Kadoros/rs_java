package SocketHost;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ScreenCastHost implements MouseMotionListener {
    final int w = 1280, h = 720;
    final int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - w / 2;
    final int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - h / 2;

    Socket socket;

    JFrame frame;
    BufferedInputStream socketByteReader;
    DataOutputStream mouseOutputStream;

    public ScreenCastHost(SocketHost socketHost) {
        initStreams(socketHost);
        createJFrame();
        getFrames();
    }

    private void initStreams(SocketHost socketHost) {
        try {
            // Use the same socket from SocketHost
            socket = socketHost.getSocket();
            socketByteReader = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createJFrame() {
        frame = new JFrame("Server");
        frame.setBounds(x, y, w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addMouseMotionListener(this);  // Add mouse motion listener
    }

    private void getFrames() {
        while (true) {
            try {
                // Receive image from client and display it on the JFrame
                Image receivedImage = ImageIO.read(socketByteReader);
                if (receivedImage != null) {
                    frame.getGraphics().drawImage(receivedImage, 0, 0, w, h, frame);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        System.out.println("Mouse moved to: (" + mouseX + ", " + mouseY + ")");

        // Send mouse coordinates to the client
        sendMouseCoordinates(mouseX, mouseY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Handle mouse dragging if needed
    }

    private void sendMouseCoordinates(int x, int y) {
        try {
            mouseOutputStream.writeInt(x); // Send X coordinate
            mouseOutputStream.writeInt(y); // Send Y coordinate
            mouseOutputStream.flush(); // Ensure the data is sent
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
