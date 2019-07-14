import com.sun.org.apache.regexp.internal.RE;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Rival {


    private int x, y, x2, y2;                   // actual position of Object
    private int centerX, centerY;               // center position of Object
    private int distanceX, distanceY;           // makes travel happen
    private int speed = 1;                      // divided by
    private Image image_Car;                    // actual image of Object
    private Image image_invincible;             // an image that occurs when Rival is invincible

    private Rectangle carRect;                  // Rectangle for collisions

    // control variables
    private boolean isShield;
    private boolean isAvoiding;
    private boolean isClosingIn;
    private boolean isCrashing;

    // dimensions for area of detection
    private int height;
    private int width;

    // special Rectangles for NPC detection
    private Rectangle areaOfCollision_Left2;
    private Rectangle areaOfCollision_Left1;
    private Rectangle areaOfCollision_Center;
    private Rectangle areaOfCollision_Right1;
    private Rectangle areaOfCollision_Right2;

    // special Rectangles for left and right NPC detection
    private Rectangle squareLeft;
    private Rectangle squareRight;

    private boolean isUp, isDown, isLeft, isRight;      // for collision purposes

    private int initialX, initialY;                     // for Push method
    private int force;                                  // for Push method
    private boolean pushHappeningX, pushHappeningY;     // checks if method is in course
    private boolean isAvoidingNPC;                      // returns TRUE if it's in the middle of avoiding the NPC's

    private Car playerCar;

    //private ArrayList<Car> List_of_small_cars;
    //private ArrayList<Car> List_of_trucks;


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

        carRect = new Rectangle(x, y, x2 - x, y2 - y);

        isShield = false;
        isAvoiding = false;
        isClosingIn = false;
        isCrashing = false;

        width = 72;
        height = 100;

        // old values
        /*
        areaOfCollision_Left2 = new Rectangle(x - 5 - (2*width), y - height, width, height);
        areaOfCollision_Left1 = new Rectangle(x - 5 - width, y - height, width, height);
        areaOfCollision_Center = new Rectangle(x - 5, y - height, width, height);
        areaOfCollision_Right1 = new Rectangle(x2 + 5, y - height, width, height);
        areaOfCollision_Right2 = new Rectangle(x2 + 5 + width, y - height, width, height );
        */

        // new values
        /*
        areaOfCollision_Center = new Rectangle(this.x, y - height, width, height);

        areaOfCollision_Right1 = new Rectangle(this.x2 + 1, y - height, width, height);
        areaOfCollision_Right2 = new Rectangle(this.x2 + 1 + width + 1, y - height, width, height );

        areaOfCollision_Left1 = new Rectangle(x - 1 - width, y - height, width, height);
        areaOfCollision_Left2 = new Rectangle(x - 1 - width - 1 - width, y - height, width, height);
        */

        // newest values
        areaOfCollision_Left2 = new Rectangle(460, this.y + height, width, height);
        areaOfCollision_Left1 = new Rectangle(532, this.y + height, width, height);
        areaOfCollision_Center = new Rectangle(604, this.y + height, width, height);
        areaOfCollision_Right1 = new Rectangle(676, this.y + height, width, height);
        areaOfCollision_Right2 = new Rectangle(748, this.y + height, width, height);
        squareLeft = new Rectangle(this.x - 81, this.y, 80, 80);
        squareRight = new Rectangle(this.x2 + 1, this.y, 80, 80);


        isUp = false;
        isDown = false;
        isLeft = false;
        isRight = false;

        initialX = -10;
        initialY = -10;
        pushHappeningX = false;
        pushHappeningY = false;
        force = 0;

        isAvoidingNPC = false;

        //System.out.println("Rival car was created");
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

    public void setDistanceX(int _distanceX) { this.distanceX = _distanceX; }
    public void setDistanceY(int _distanceY) { this.distanceY = _distanceY; }

    public boolean getShield() { return this.isShield; }
    public boolean getAvoid() { return this.isAvoiding; }
    public boolean getCloseIn() { return this.isClosingIn; }
    public boolean getCrash() { return this.isCrashing; }

    public Rectangle getRectCenter() { return this.areaOfCollision_Center; }
    public Rectangle getRectRight1() { return this.areaOfCollision_Right1; }
    public Rectangle getRectRight2() { return this.areaOfCollision_Right2; }
    public Rectangle getRectLeft1() { return this.areaOfCollision_Left1; }
    public Rectangle getRectLeft2() { return this.areaOfCollision_Left2; }

    public Rectangle getRightSquare() { return this.squareRight; }
    public Rectangle getLeftSquare() { return this.squareLeft; }

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

    public void setIsAvoidingNPC(boolean _value) { this.isAvoidingNPC = _value; }

    //  receives the value for X when Rival needs to avoid NPC cars
    public void setDistanceX_Avoidance(int _distanceX) {
        this.distanceX = _distanceX;
        this.isAvoidingNPC = true;
    }

    //  receives the value for Y when Rival need to avoid NPC cars
    public void setDistanceY_Avoidance(int _distanceY) {
        this.distanceY = _distanceY;
        this.isAvoidingNPC = true;
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

        carRect.translate(distanceX / speed, distanceY / speed);    // Rival Rectangle moves with car

        // translate frontal Rectangles , only in Y
        areaOfCollision_Left2.translate(0, distanceY / speed);
        areaOfCollision_Left1.translate(0, distanceY / speed);
        areaOfCollision_Center.translate(0, distanceY / speed);
        areaOfCollision_Right1.translate(0, distanceY / speed);
        areaOfCollision_Right2.translate(0, distanceY / speed);

        // lateral Rectangles move with car
        squareLeft.translate(distanceX / 2, distanceY / speed);
        squareRight.translate(distanceX / 2, distanceY / speed);
    }

    //  Reaction when being pushed
    public void movementAccordingToShield(String direction, int _force, int time) {

        // sets at the first frame of use
        this.isShield = true;
        System.out.println("R -- Shield movement start");


        if (direction == "RIGHT")
            this.distanceX = 4 * force;

        if (direction == "LEFT")
            this.distanceX = -4 * force;

        if (direction == "UP")
            this.distanceY = -4 * force;

        if (direction == "DOWN")
            this.distanceY = 4 * force;

        if (direction == "NONE") {
            this.distanceX = 0;
            this.distanceY = 0;
        }

        regainControlAfterXseconds(time);
    }


    public void regainControlAfterXseconds(int time) {

        // regain control after X seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { isShield = false; }
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
