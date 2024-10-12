import Socket.ScreenCastClient;

import java.awt.*;

public class Main {
    public static void main(String[] args)  {
        try {
            ScreenCastClient screenCastClient = new ScreenCastClient("127.0.0.1", 1000);
            screenCastClient.sendScreenCast();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}