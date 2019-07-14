import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Car {


    private int x, y, x2, y2;               // actual position of object
    private int centerX, centerY;           // center of Object
    private int distanceX, distanceY;       // makes travel happen
    private boolean isPlayer;               // for Player methods
    private int speed = 1;                  // divided by
    private Image image_Car;                // actual image of each Object
    private Image image_invincible;         // an image that occurs when Player is invincible
    public int lives;                       // destruction of Object at 0
    private boolean isShield;               // checks if Player is invincible versus other cars | Also, let's player control car

    private Rectangle carRect;              //  Rectangle for collisions

    private boolean isUp, isDown, isLeft, isRight;      // for collision purposes


    //  Constructor
    public Car(String carType, int _x, int _y) {

        //  assign coordinates
        this.x = _x;
        this.y = _y;

        if (carType == "PLAYER") {  // playerCar

            ImageIcon imgIcon = new ImageIcon("Resources//car1.gif");
            image_Car = imgIcon.getImage();

            // image for Invincibility
            ImageIcon imgIcon2 = new ImageIcon("Resources//shield.png");
            image_invincible = imgIcon2.getImage();

            this.lives = 10;
            this.isPlayer = true;

            this.isShield = false;

            this.x2 = this.x + 52;
            this.y2 = this.y + 62;

            isUp = false;
            isDown = false;
            isLeft = false;
            isRight = false;

            //System.out.println("Player car was created");
        }

        if (carType == "SMALL_CAR") {   // generate one random small car

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

            //System.out.println("Random number is " + temp);

            this.lives = 1;

            this.x2 = this.x + 44;
            this.y2 = this.y + 58;

            //System.out.println("Small car:\tX1= " + this.x + "  X2= " + this.x2);
        }

        if (carType == "TRUCK") {   // truck-vehicle

            ImageIcon imgIcon = new ImageIcon("Resources//truck.png");
            image_Car = imgIcon.getImage();

            this.lives = 1;

            this.x2 = this.x + 58;
            this.y2 = this.y + 128;

            //System.out.println("Truck:\tX1= " + this.x + "  X2= " + this.x2);
        }

        this.centerX = (x + x2) / 2;
        this.centerY = (y + y2) / 2;

        carRect = new Rectangle(x, y, x2 - x, y2 - y);
    }


    //  Getters and Setters
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getX2() { return this.x2; }
    public int getY2() { return this.y2; }
    public int getCenterX() { return this.centerX; }
    public int getCenterY() { return this.centerY; }

    public Image getImage() { return this.image_Car; }
    public Image getImage_invincible() { return this.image_invincible; }

    public boolean getShield() { return this.isShield; }

    public Rectangle getRect() { return this.carRect; }

    public boolean getUp() { return this.isUp; }
    public boolean getDown() { return this.isDown; }
    public boolean getLeft() { return this.isLeft; }
    public boolean getRight() { return this.isRight; }


    //  Methods

    //  Movement method for Player
    public void move() {

        if (!isShield) {

            // stop Player from going outBounds vertically
            if ( (this.y <= 50 && isUp) || (this.y2 >= 670 && isDown) )         // (720 - 50 = 670)
                distanceY = 0;

            // stop Player from going outBounds horizontally
            if ( (this.x <= 50 && isLeft) || (this.x2 >= 1230 && isRight) )     // (1280 - 50 = 1230)
                distanceX = 0;
/*
/////////////////////////////////////////////////////////////////////////

            // know when to stop being pushed in X
            if (this.pushHappeningX) {
                if ((force == 1 && centerX >= initialX + 40) || (force == 2 && centerX >= initialX + 80)
                        ||
                        (force == 1 && centerX <= initialX - 40) || (force == 2 && centerX <= initialX - 80)) {

                    // reset all values
                    pushHappeningX = false;
                    this.control = true;
                    System.out.println("////// True");
                    distanceX = 0;
                    force = 0;
                    initialX = -10;
                }
            }

            // know when to stop being pushed in Y
            if (this.pushHappeningY) {
                if ((force == 1 && centerY >= initialY + 40) || (force == 2 && centerY >= initialY + 80)
                        ||
                        (force == 1 && centerY <= initialY - 40) || (force == 2 && centerY <= initialY - 80)) {

                    // reset all values
                    pushHappeningY = false;
                    this.control = true;
                    System.out.println("////// True");
                    distanceY = 0;
                    force = 0;
                    initialY = -10;
                }
            }

//////////////////////////////////////////////////////////////////
*/
            x += distanceX / speed;
            x2 += distanceX / speed;

            y += distanceY / speed;
            y2 += distanceY / speed;

            centerX += distanceX / speed;
            centerY += distanceY / speed;

            carRect.translate(distanceX / speed, distanceY / speed);
        }
    }


    //  Decides movement to Player when collision happens
    public void movementAccordingToShield(String direction, int force, int time) {

        // sets at the first frame of use
        this.isShield = true;
        System.out.println("P -- Shield movement start");


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


    //  Movement pattern of Small Cars
    public void enemyMove(boolean turbo) {

        if (turbo) {
            y += 25;
            y2 += 25;
            carRect.translate(0, 25);
        }
        else {
            y += 5;
            y2 += 5;
            carRect.translate(0, 5);
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


    //  Slow-down if Player is off-track
    public void verifySpeed() {
        if ( (this.x <= 427) || (this.x2 >= 854) )
            this.speed = 2;

        else
            speed = 1;
    }


    //  Happens when Player collides with an NPC and still has lives left
    public void regainControlAfterXseconds(int time) {

        // regain control after X seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { isShield = false; }
                }, 1000 * time);
    }

    //  After colliding, change Player position, if necessary
    public void changePosition(int _centerX, int _centerY) {

        // put Player car in target position
    }


    //  when Player collides with Rival, control is temporarily disabled
    public void changeControlForCollisionRival() {

        // regain control after 1 second
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { isShield = false; }
                }, 1000);
    }


    //  Control keys
    public void keyPressed(KeyEvent key) throws InterruptedException {

        // method works only if Player has control over his car
        if (!isShield) {

            int code = key.getKeyCode();
            if (code == KeyEvent.VK_UP) {

                if (y >= 20) {
                    distanceY = -8;
                    isUp = true;
                } else
                    distanceY = 0;
            }

            if (code == KeyEvent.VK_DOWN) {

                if (y2 <= 700) {
                    distanceY = 8;
                    isDown = true;
                } else
                    distanceY = 0;
            }

            if (code == KeyEvent.VK_RIGHT) {
                distanceX = 4;
                isRight = true;
            }

            if (code == KeyEvent.VK_LEFT) {
                distanceX = -4;
                isLeft = true;
            }
        }
    }

    public void keyReleased(KeyEvent key) {

        // method works only if Player has control over his car
        if (!isShield) {

            int code = key.getKeyCode();

            if (code == KeyEvent.VK_UP) {
                distanceY = 0;
                isUp = false;
            }

            if (code == KeyEvent.VK_DOWN) {
                distanceY = 0;
                isDown = false;
            }

            if (code == KeyEvent.VK_LEFT) {
                distanceX = 0;
                isLeft = false;
            }

            if (code == KeyEvent.VK_RIGHT) {
                distanceX = 0;
                isRight = false;
            }
        }
    }
}
