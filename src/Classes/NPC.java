package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NPC {


    private int x, y, x2, y2;               // actual position of object
    private int centerX, centerY;           // center of Object
    private Image image_Car;                // actual image of each Object

    private Rectangle carRect;              //  Rectangle for collisions


    //  Constructor
    public NPC(String carType, int _x, int _y) {

        //  assign coordinates
        this.x = _x;
        this.y = _y;

        if (carType.equals("SMALL_CAR")) {   // generate one random small car

            Random randomNumber = new Random();
            int temp = randomNumber.nextInt(3); // generate number between 0 and 2 {0 , 1 , 2}

            if (temp == 0) {
                ImageIcon imgIcon = new ImageIcon("Resources//enemyCar1.png");
                image_Car = imgIcon.getImage();
            }
            else if (temp == 1) {
                ImageIcon imgIcon = new ImageIcon("Resources//enemyCar2.png");
                image_Car = imgIcon.getImage();
            }
            else {
                ImageIcon imgIcon = new ImageIcon("Resources//enemyCar3.png");
                image_Car = imgIcon.getImage();
            }

            this.x2 = this.x + 44;
            this.y2 = this.y + 58;
        }

        if (carType.equals("TRUCK")) {   // truck-vehicle

            ImageIcon imgIcon = new ImageIcon("Resources//truck.png");
            image_Car = imgIcon.getImage();

            this.x2 = this.x + 58;
            this.y2 = this.y + 128;
        }

        this.centerX = (x + x2) / 2;
        this.centerY = (y + y2) / 2;

        carRect = new Rectangle(x, y, x2 - x, y2 - y);
    }


    //  Getters
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getX2() { return this.x2; }
    public int getY2() { return this.y2; }
    public int getCenterX() { return this.centerX; }
    public int getCenterY() { return this.centerY; }

    public Image getImage() { return this.image_Car; }

    public Rectangle getRect() { return this.carRect; }


    //  Movement pattern of Small Cars
    public void smallCarMove(boolean turbo) {

        if (turbo) {
            this.y += 25;
            this.y2 += 25;
            this.centerY += 25;
            this.carRect.translate(0, 25);
        }
        else {
            this.y += 5;
            this.y2 += 5;
            this.centerY += 5;
            this.carRect.translate(0, 5);
        }

    }

    //  Movement pattern of Trucks
    public void truckMove(boolean turbo) {

        if (turbo) {
            this.y += 22;
            this.y2 += 22;
            this.centerY += 22;
            this.carRect.translate(0, 22);
        }
        else {
            this.y += 2;
            this.y2 += 2;
            this.centerY += 2;
            this.carRect.translate(0, 2);
        }
    }
}
