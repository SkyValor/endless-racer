package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {

    private int x, y, x2, y2;               // actual position of object
    private int centerX, centerY;           // center of Object
    private int distanceX, distanceY;       // makes travel happen
    private int offStreetSpeed = 1;         // divided by , when Player is off-street
    private Image image_Car;                // actual image of each Object
    private Image image_invincible;         // an image that occurs when Player is invincible
    private int lives;                      // GAME OVER at value 0
    private boolean isShield;               // checks if Player is invincible versus other cars | Also, let's player control car

    private Rectangle carRect;              //  Rectangle for collisions

    private boolean isUp, isDown, isLeft, isRight;      // for collision purposes

    private int turbosInt;                  // number of available turbos


    //  Constructor
    public Player(int _x, int _y) {

        //  assign coordinates
        this.x = _x;
        this.y = _y;

        this.x2 = this.x + 52;
        this.y2 = this.y + 62;

        this.centerX = (x + x2) / 2;
        this.centerY = (y + y2) / 2;

        // set the image of the car
        ImageIcon imgIcon = new ImageIcon("Resources//car1.gif");
        image_Car = imgIcon.getImage();

        // set the image for Shield
        ImageIcon imgIcon2 = new ImageIcon("Resources//shield.png");
        image_invincible = imgIcon2.getImage();

        this.lives = 50;
        this.turbosInt = 0;
        this.isShield = false;

        isUp = false;
        isDown = false;
        isLeft = false;
        isRight = false;

        // set the collision Rectangle
        carRect = new Rectangle(x, y, x2 - x, y2 - y);
    }


    ///////////////////////////
    //  Getters and Setters  //
    ///////////////////////////

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getX2() { return this.x2; }
    public int getY2() { return this.y2; }
    public int getCenterX() { return this.centerX; }
    public int getCenterY() { return this.centerY; }

    public Image getImage() { return this.image_Car; }
    public Image getImage_invincible() { return this.image_invincible; }

    public int getLives() { return this.lives; }
    public void decreaseLives() { --this.lives; }

    public boolean getShield() { return this.isShield; }

    public Rectangle getRect() { return this.carRect; }

    public boolean getUp() { return this.isUp; }
    public boolean getDown() { return this.isDown; }
    public boolean getLeft() { return this.isLeft; }
    public boolean getRight() { return this.isRight; }

    public void addTurbo() { ++this.turbosInt; }


    //////////////
    //  Methods //
    //////////////

    public void move() {

        if (!isShield) {

            // stop Player from going out of bounds vertically
            if ( (this.y <= 50 && isUp) || (this.y2 >= 670 && isDown) )         // (720 - 50 = 670)
                distanceY = 0;

            // stop Player from going out of bounds horizontally
            if ( (this.x <= 50 && isLeft) || (this.x2 >= 1230 && isRight) )     // (1280 - 50 = 1230)
                distanceX = 0;

            // if Player is Off-street
            if (this.centerX <= 427 || this.centerX >= 854) {

                this.offStreetSpeed = 2;    // set speed for slow-down movement

                // if Player is not moving vertically , slowly move backward
                if (distanceY == 0)
                    distanceY = 4;
            }
            else    // if Player is On-street
                this.offStreetSpeed = 1;    // set speed for normal movement




            x += distanceX / offStreetSpeed;
            x2 += distanceX / offStreetSpeed;
            centerX += distanceX / offStreetSpeed;


            y += distanceY / offStreetSpeed;
            y2 += distanceY / offStreetSpeed;
            centerY += distanceY / offStreetSpeed;


            carRect.translate(distanceX / offStreetSpeed, distanceY / offStreetSpeed);
        }
    }


    //  Decides movement to Player when collision happens
    public void movementAccordingToShield(String direction, int force, int time) {

        // sets at the first frame of use
        this.isShield = true;
        System.out.println("P -- Shield movement start");


        if (direction.equals("RIGHT"))
            this.distanceX = 4 * force;

        if (direction.equals("LEFT"))
            this.distanceX = -4 * force;

        if (direction.equals("UP"))
            this.distanceY = -4 * force;

        if (direction.equals("DOWN"))
            this.distanceY = 4 * force;

        if (direction.equals("NONE")) {
            this.distanceX = 0;
            this.distanceY = 0;
        }

        regainControlAfterXseconds(time);
    }


    //  Happens when Player collides with an NPC and still has lives left
    public void regainControlAfterXseconds(int time) {

        // regain control after X seconds
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { isShield = false; System.out.println("P -- Shield movement stop"); }
                }, 1000 * time);
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
                } else {
                    distanceY = 0;
                    isUp = false;
                }

                // if Player is Off-street , decrease movement forward
                if (this.centerX <= 427 || this.centerX >= 854) {
                    distanceY += 4;
                }
            }

            if (code == KeyEvent.VK_DOWN) {

                if (y2 <= 700) {
                    distanceY = 8;
                    isDown = true;
                } else {
                    distanceY = 0;
                    isDown = false;
                }

                // if Player is Off-street , increase movement backward
                if (this.centerX <= 427 || this.centerX >= 854) {
                    distanceY += 4;
                }
            }

            if (code == KeyEvent.VK_RIGHT) {

                if (x >= 20) {
                    distanceX = 4;
                    isRight = true;
                } else {
                    distanceX = 0;
                    isRight = false;
                }
            }

            if (code == KeyEvent.VK_LEFT) {

                if (x <= 1260) {
                    distanceX = -4;
                    isLeft = true;
                } else {
                    distanceX = 0;
                    isLeft = false;
                }
            }

            if (code == KeyEvent.VK_SPACE) {

                if (turbosInt > 0 && !Game.isTurbo) {

                    // can only be used if there is no Turbo being used
                    if (!Game.isTurbo) {
                        turbosInt--;
                        Game.isTurbo = true;

                        // turbo ends after 5 seconds
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {

                                    @Override
                                    public void run() { Game.isTurbo = false; }
                                }, 5000);
                    }

                }

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
