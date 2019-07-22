package personal.exercises.endlessRacer.car.specialCars;

import personal.exercises.endlessRacer.Game;
import personal.exercises.endlessRacer.GameWindow;
import personal.exercises.endlessRacer.car.AbstractCar;
import personal.exercises.endlessRacer.car.Direction;

import javax.swing.*;
import java.awt.*;

public class SpecialCar extends AbstractCar {

    int slowness;
    protected boolean shielded;
    private Image invincibleImage;

    protected SpecialCar(int posX, int posY, int deltaX, int deltaY) {
        super(posX, posY, deltaX, deltaY);

        slowness = 1;
        shielded = false;

        ImageIcon imgIcon = new ImageIcon("shield.png");
        invincibleImage = imgIcon.getImage();
        setDirection(Direction.NONE);
    }

    /**
     * Gets the shielded state of this {@link SpecialCar}
     * @return the shielded state
     */
    public boolean isShielded() {
        return shielded;
    }

    /**
     * Gets the image for the {@link SpecialCar}'s invincibility
     * @return the image
     */
    public Image getInvincibleImage() {
        return invincibleImage;
    }

    /**
     * Makes the car setAiMovement according to its {@code deltaX} and {@code deltaY} values, if the car is not
     * {@code shielded}.
     */
    @Override
    public void move() {

        if (shielded) {
            return;
        }

        getCollider().translate(getDeltaX() / slowness, getDeltaY() / slowness);
    }

    /**
     * Sets the {@code shielded} state to {@code true} and forces the {@link SpecialCar} to have a certain movement
     * pattern or no movement at all, for a given {@code time} in seconds. The effect should wear-off after
     * the has passed.
     *
     * @param direction the direction for which the car will be moving to
     * @param force how fast the movement will be done for
     * @param time for how long should the effect take place
     * @see SpecialCar#unshieldAfter(int)
     */
    protected void moveFromImpact(Direction direction, int force, int time, int deltaX, int deltaY) {

        // TODO: update the new logic with deltaX and deltaY on documentation

        shielded = true;
        //this.force = force;
        setDirection(direction);

        setDeltaX((deltaX * direction.getPolarityX() * force) / slowness);
        setDeltaY((deltaY * direction.getPolarityY() * force) / slowness);

        unshieldAfter(time);
    }

    /**
     * After a given amount of {@code time}, in seconds, sets the value of the {@code shielded} state back
     * to {@code false}, allowing this {@link SpecialCar} to control again.
     *
     * @param seconds the time in seconds
     */
    private void unshieldAfter(int seconds) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() {
                        shielded = false;
                    }
                }, 1000 * seconds);
    }

    /**
     * Checks if this {@link SpecialCar} is off-track, in which case sets the value of {@code slowness}
     * to 2 (slow speed). Otherwise, sets it to 1 (normal speed).
     */
    public void verifySpeed() {

        if ((getX() <= Game.OFF_STREET_OFFSET) || (getX2() >= GameWindow.WIDTH - Game.OFF_STREET_OFFSET)) {
            this.slowness = 2;

        } else {
            slowness = 1;
        }
    }
}
