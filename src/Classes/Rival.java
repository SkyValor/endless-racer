package Classes;

import javax.swing.*;
import java.awt.*;

public class Rival {

    private int x, y, x2, y2;                   // actual position of Object
    private int centerX, centerY;               // center position of Object
    private int distanceX, distanceY;           // makes travel happen
    private int velocity;                       // normal velocity of vehicle
    private int speed = 1;                      // divided by
    private Image image_Car;                    // actual image of Object
    private Image image_invincible;             // an image that occurs when Classes.Rival is invincible

    private Rectangle carRect;                  // Rectangle for collisions

    // control variables
    private boolean isShield;
    private boolean isAvoiding;
    private boolean isClosingIn;
    private boolean isCrashing;

    private boolean isUp, isDown, isLeft, isRight;      // for collision purposes

    private int initialX, initialY;                     // for Push method
    private int force;                                  // for Push method
    private boolean pushHappeningX, pushHappeningY;     // checks if method is in course


    //  Constructor
    public Rival(int _x, int _y) {

        ImageIcon imgIcon = new ImageIcon("Resources//rivalCar.png");
        image_Car = imgIcon.getImage();

        ImageIcon imgIcon2 = new ImageIcon("Resources//shield.png");
        image_invincible = imgIcon2.getImage();

        x = _x;
        y = _y;

        x2 = x + 40;
        y2 = y + 80;

        centerX = (x + x2) / 2;
        centerY = (y + y2) / 2;

        velocity = 4;   // have an even number, for halving

        carRect = new Rectangle(x, y, x2 - x, y2 - y);

        isShield = false;
        isAvoiding = false;
        isClosingIn = false;
        isCrashing = false;

        isUp = false;
        isDown = false;
        isLeft = false;
        isRight = false;

        initialX = -10;
        initialY = -10;
        pushHappeningX = false;
        pushHappeningY = false;
        force = 0;
    }


    //  Getters
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getX2() {
        return this.x2;
    }
    public int getY2() {
        return this.y2;
    }
    public int getCenterX() {
        return this.centerX;
    }
    public int getCenterY() {
        return this.centerY;
    }

    public Image getImage() {
        return this.image_Car;
    }
    public Image getImage_invincible() { return this.image_invincible; }

    public Rectangle getRect() { return this.carRect; }

    public boolean getCrash() { return this.isCrashing; }
    public void setCrash(boolean value) { this.isCrashing = value; }

    public boolean getCloseIn() { return this.isClosingIn; }
    public void setCloseIn(boolean value) { this.isClosingIn = value; }

    public boolean getAvoid() { return this.isAvoiding; }
    public void setAvoid(boolean value) { this.isAvoiding = value; }

    public boolean getShield() { return this.isShield; }

    public boolean getUp() {
        return this.isUp;
    }
    public boolean getDown() {
        return this.isDown;
    }
    public boolean getLeft() {
        return this.isLeft;
    }
    public boolean getRight() {
        return this.isRight;
    }



    public void setDistanceX(String direction) {

        switch (direction) {
            case "RIGHT": this.distanceX = velocity;
            break;

            case "LEFT": this.distanceX = -1 * velocity;
            break;

            default: this.distanceX = 0;
        }
    }




    public void setDistanceY(String direction) {

        if (direction.equals("UP"))
            this.distanceY = -1 * velocity;

        else if (direction.equals("DOWN"))
            this.distanceY = velocity;

        else
            this.distanceY = 0;
    }



    public void setDistance(String direction) {

        switch (direction) {
            case "UP": this.distanceY = -1 * velocity;
            break;

            case "DOWN": this.distanceY = velocity;
            break;

            case "RIGHT": this.distanceX = velocity;
            break;

            case "LEFT": this.distanceX = -1 * velocity;
            break;

            default: this.distanceX = 0; this.distanceY = 0;

        }
    }


    //  receives the value for X when Classes.Rival needs to avoid Classes.NPC cars
    public void setDistanceX_Avoidance(int _distanceX) {
        this.distanceX = _distanceX;
        this.isAvoiding = true;
    }

    //  receives the value for Y when Classes.Rival need to avoid Classes.NPC cars
    public void setDistanceY_Avoidance(int _distanceY) {
        this.distanceY = _distanceY;
        this.isAvoiding = true;
    }


    //  Methods
    public void move() {

        // move all Position coordinates
        x += distanceX / speed;
        x2 += distanceX / speed;

        y += distanceY / speed;
        y2 += distanceY / speed;

        centerX += distanceX / speed;
        centerY += distanceY / speed;

        carRect.translate(distanceX / speed, distanceY / speed);    // Classes.Rival Rectangle moves with car
    }

    //  Reaction when being pushed
    public void movementAccordingToShield(String direction, int force, int time) {

        // sets at the first frame of use
        this.isShield = true;
        System.out.println("R -- Shield movement start");

        // move Classes.Rival in the direction of impact
        switch (direction) {
            case "UP": this.distanceY = -2 * force;
            break;

            case "DOWN": this.distanceY = 2 * force;
            break;

            case "RIGHT": this.distanceX = 2 * force;
            break;

            case "LEFT": this.distanceX = -2 * force;
            break;

            default: this.distanceX = 0; this.distanceY = 0;
        }

        // return values to normal after specified time
        regainControlAfterXseconds(time);
    }


    public void regainControlAfterXseconds(int time) {

        // regain control after X seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { isShield = false; distanceX = 0; distanceY = 0; }
                }, 1000 * time);
    }


    //  Slow-down when Off-street
    public void verifySpeed() {
        if ((this.x <= 427) || (this.x2 >= 854))
            this.speed = 2;

        else
            speed = 1;
    }
}
