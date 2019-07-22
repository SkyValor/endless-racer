package personal.exercises.endlessRacer;

import javax.swing.*;
import java.awt.*;

class Background {

    private int posY;
    private Image image_Background;
    private boolean alone;

    Background(int posY) {

        this.posY = posY;
        ImageIcon imgIcon = new ImageIcon("resources/field_1.jpg");
        image_Background = imgIcon.getImage();

        alone = true;
    }

    int getY() {
        return posY;
    }

    Image getImage() {
        return image_Background;
    }

    boolean isAlone() {
        return alone;
    }

    void setAloneToFalse() {
        alone = false;
    }

    public void move(boolean isTurbo) {

        if (isTurbo) {
            this.posY += 30;
        }

        else {
            this.posY += 10;
        }
    }
}
