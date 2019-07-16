package Classes;

import javax.swing.*;
import java.awt.*;

public class Turbo {

    private int x, y, x2, y2;
    private int centerX, centerY;
    private Image image_turbo;
    private static int width;

    private Rectangle turboRect;


    //  Constructor
    public Turbo(int _x, int _y) {

        ImageIcon imgIcon = new ImageIcon("Resources//turbo_placeholder.png");      // IMPORTANTE -- falta dar uma imagem ( ou GIF )
        image_turbo = imgIcon.getImage();

        this.x = _x;
        this.y = _y;

        this.x2 = _x + imgIcon.getIconWidth();
        this.y2 = _y + imgIcon.getIconHeight();

        System.out.println("y: " + this.y2 + " x: " + this.x2);

        width = imgIcon.getIconWidth();

        this.centerX = (_x + x2) / 2;
        this.centerY = (_y + y2) / 2;

        this.turboRect = new Rectangle(_x, _y, imgIcon.getIconWidth(), imgIcon.getIconHeight());

    }


    //  Getters
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public Image getImage() { return this.image_turbo; }

    public Rectangle getRect() { return this.turboRect; }

    public static int getWidth() { return width; }


    //  Methods
    public void descend() {

        this.y += 8;
        this.y2 += 8;
        this.centerY += 8;
        this.turboRect.translate(0, 8);
    }
}
