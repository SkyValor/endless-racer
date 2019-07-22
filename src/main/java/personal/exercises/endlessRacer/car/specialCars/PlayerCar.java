package personal.exercises.endlessRacer.car.specialCars;

import personal.exercises.endlessRacer.GameWindow;
import personal.exercises.endlessRacer.car.Direction;

public class PlayerCar extends SpecialCar {

    private static final int DX = 4;
    private static final int DY = 8;

    private int lives;

    /**
     * Constructor method for the player car. Receives its position as arguments.
     *
     * @param posX the position in the X axis
     * @param posY the position in the Y axis
     */
    public PlayerCar(int posX, int posY) {
        super(posX, posY, DX, DY);
        init();
    }

    /**
     * Initializes the properties
     */
    private void init() {

        lives = 10;

        String imageFilePath = "playerCar.gif";
        super.setImageAndBound(imageFilePath);
    }

    /**
     * Gets the correct number of lives of player
     * @return the number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Decrements one unit to lives.
     */
    public void loseLife() {
        --lives;
    }

    public void moveFromImpact(Direction direction, int force, int time) {
        super.moveFromImpact(direction, force, time, DX, DY);
    }

    /**
     * Makes the car setAiMovement according to its deltaX and deltaY values, if the car is not shielded.
     */
    @Override
    public void move() {

        if (shielded) {
            getCollider().translate(getDeltaX(), getDeltaY());
            return;
        }

        setDeltaX((DX * getDirection().getPolarityX()) / slowness);
        setDeltaY((DY * getDirection().getPolarityY()) / slowness);
        checkPlayerMovePermission();    // TODO: update this part in Documentation

        getCollider().translate(getDeltaX() / slowness, getDeltaY() / slowness);
    }

    /**
     * Verifies if the player is already close to the bounds of this window and is attempting to move towards it.
     * If that is the case, forcefully sets the respective delta(s) to zero.
     */
    private void checkPlayerMovePermission() {

        if ((getX() <= GameWindow.BOUNDS_OFFSET && isMovingLeft())
            || (getX2() >= GameWindow.WIDTH - GameWindow.BOUNDS_OFFSET && isMovingRight())) {
            setDeltaX(0);
        }

        if ((getY() <= GameWindow.BOUNDS_OFFSET && isMovingUp())
            || (getY2() >= GameWindow.HEIGHT - GameWindow.BOUNDS_OFFSET && isMovingDown())) {
            setDeltaY(0);
        }
    }
}
