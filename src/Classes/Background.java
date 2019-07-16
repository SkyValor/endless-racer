package Classes;

import javax.swing.*;
import java.awt.*;

public class Background {

    private int y;                      // actual position of Background
    private Image image_Background;     // actual image of Background


    //  Constructor
    public Background(int _y) {

        this.y = _y;
        ImageIcon imgIcon = new ImageIcon("Resources//field_1.jpg");
        image_Background = imgIcon.getImage();
    }

    //  Getters

    public int getY() { return this.y; }

    public Image getImage() { return this.image_Background; }

    //  Methods

    public void descend(boolean turbo) {

        if (turbo)
            this.y += 30;
        else
            this.y += 10;
    }
}
