package personal.exercises.endlessRacer.car;

import personal.exercises.endlessRacer.car.enemyCars.EnemyCarType;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractCar implements Car {

    private static boolean TURBO;

    private int deltaX;   // TODO: getDeltaX() = deltaX * direction.getPolarityX()
    private int deltaY;   // TODO: getDeltaY() = deltaY * direction.getPolarityY()
    private Image image;
    protected Rectangle collider;
    private Direction direction;
    private EnemyCarType enemyCarType;

    /**
     * Basic constructor. Instantiates the car in the given position and with its unique speed.
     *
     * @param posX position in the X axis
     * @param posY position in the Y axis
     * @param deltaX how much this car moves horizontally
     * @param deltaY how much this car moves vertically
     */
    protected AbstractCar(int posX, int posY, int deltaX, int deltaY) {

        collider = new Rectangle(posX, posY, 0, 0);
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        direction = Direction.DOWN;
    }

    /**
     * @see Car#getX()
     */
    public int getX() {
        return (int) collider.getX();
    }

    /**
     * @see Car#getY()
     */
    public int getY() {
        return (int) collider.getY();
    }

    /**
     * @see Car#getX2()
     */
    public int getX2() {
        return (int) collider.getMaxX();
    }

    /**
     * @see Car#getY2()
     */
    public int getY2() {
        return (int) collider.getMaxY();
    }

    /**
     * @see Car#getCenterX()
     */
    public int getCenterX() {
        return (int) ((collider.getX() + collider.width) / 2);
    }

    /**
     * @see Car#getCenterY()
     */
    public int getCenterY() {
        return (int) ((collider.getY() + collider.height) / 2);
    }

    /**
     * Gets the value of deltaX for movement in the X axis. For this reason, the value to be get should be multiplied
     * with the polarity of direction.
     *
     * @return the delta in the X axis
     * @see Direction#getPolarityX()
     */
    protected int getDeltaX() {
        return deltaX * direction.getPolarityX();
    }

    /**
     * Sets the value of deltaX to the specified in parameter.
     * @param deltaX the value to be set
     */
    protected void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    /**
     * Gets the value of deltaY for movement in the Y axis. For this reason, the value to be get should be multiplied
     * with the polarity of direction.
     *
     * @return the delta in the Y axis
     * @see Direction#getPolarityY()
     */
    protected int getDeltaY() {
        return deltaY * direction.getPolarityY();
    }

    /**
     * Sets the value of deltaY to the specified in parameter.
     * @param deltaY the value to be set
     */
    protected void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    /**
     * @see Car#getImage()
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Receives the file path for the image and sets it to the car. Also, using this image, sets its width and height
     * to the collider.
     *
     * @param filePath the path for the image
     */
    protected void setImageAndBound(String filePath) {

        ImageIcon imgIcon = new ImageIcon(filePath);
        image = imgIcon.getImage();

        collider.width = imgIcon.getIconWidth();
        collider.height = imgIcon.getIconHeight();
    }

    /**
     * @see Car#getCollider()
     */
    public Rectangle getCollider() {
        return collider;
    }

    /**
     * @see Car#getWidth()
     */
    public int getWidth() {
        return collider.width;
    }

    /**
     * @see Car#getDirection()
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction for which the car is moving to
     * @param direction the direction to be set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns true if this car is moving left, regardless if it's moving vertically.
     * @return true if moving left; false otherwise
     */
    public boolean isMovingLeft() {
        return (direction.equals(Direction.LEFT) || direction.equals(Direction.UPPER_LEFT) || direction.equals(Direction.LOWER_LEFT));
    }

    /**
     * Returns true if this car is moving right, regardless if it's moving vertically.
     * @return true if moving right; false otherwise
     */
    public boolean isMovingRight() {
        return (direction.equals(Direction.RIGHT) || direction.equals(Direction.UPPER_RIGHT) || direction.equals(Direction.LOWER_RIGHT));
    }

    /**
     * Returns true if this car is moving up, regardless if it's moving horizontally
     * @return true if moving up; false otherwise
     */
    public boolean isMovingUp() {
        return (direction.equals(Direction.UP) || direction.equals(Direction.UPPER_RIGHT) || direction.equals(Direction.UPPER_LEFT));
    }

    /**
     * Returns true if this car is moving down, regardless if it's moving horizontally
     * @return true if moving down; false otherwise
     */
    public boolean isMovingDown() {
        return (direction.equals(Direction.DOWN) || direction.equals(Direction.LOWER_RIGHT) || direction.equals(Direction.LOWER_LEFT));
    }

    /**
     * Gets the state of turbo
     * @return true if turbo is active; false otherwise
     */
    public static boolean getTurbo() {
        return TURBO;
    }

    /**
     * Sets the value of turbo to what is received as a parameter
     * @param turbo the value to be set on turbo
     */
    public static void setTurbo(boolean turbo) {
        TURBO = turbo;
    }



    /**
     * @see Car#move()
     */
    public abstract void move();

    /*
    {

        collider.translate(deltaX / speed, deltaY / speed);


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
                    deltaX = 0;
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
                    deltaY = 0;
                    force = 0;
                    initialY = -10;
                }
            }

        //////////////////////////////////////////////////////////////////
    */

    //  After colliding, change Player position, if necessary
    public void changePosition(int _centerX, int _centerY) {

        // put Player car in target position
    }

    /*

    //  Control keys
    public void keyPressed(KeyEvent key) throws InterruptedException {

        // method works only if Player has control over his car
        if (!isShield) {

            int code = key.getKeyCode();
            if (code == KeyEvent.VK_UP) {

                if (y >= 20) {
                    deltaY = -8;
                    isUp = true;
                } else
                    deltaY = 0;
            }

            if (code == KeyEvent.VK_DOWN) {

                if (y2 <= 700) {
                    deltaY = 8;
                    isDown = true;
                } else
                    deltaY = 0;
            }

            if (code == KeyEvent.VK_RIGHT) {
                deltaX = 4;
                isRight = true;
            }

            if (code == KeyEvent.VK_LEFT) {
                deltaX = -4;
                isLeft = true;
            }
        }
    }

    public void keyReleased(KeyEvent key) {

        // method works only if Player has control over his car
        if (!isShield) {

            int code = key.getKeyCode();

            if (code == KeyEvent.VK_UP) {
                deltaY = 0;
                isUp = false;
            }

            if (code == KeyEvent.VK_DOWN) {
                deltaY = 0;
                isDown = false;
            }

            if (code == KeyEvent.VK_LEFT) {
                deltaX = 0;
                isLeft = false;
            }

            if (code == KeyEvent.VK_RIGHT) {
                deltaX = 0;
                isRight = false;
            }
        }
    }

    */
}
