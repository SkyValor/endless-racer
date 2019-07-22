package personal.exercises.endlessRacer.car;

import java.awt.*;

public interface Car {

    /**
     * Gets the position of this car in the X axis
     * @return the x
     */
    int getX();

    /**
     * Gets the position of this car in the Y axis
     * @return the y
     */
    int getY();

    /**
     * Gets opposite side of this car in the X axis
     * @return the max value of x
     */
    int getX2();

    /**
     * Gets the opposite side of this car in the Y axis
     * @return the max value of y
     */
    int getY2();

    /**
     * Gets the center point between the origin X and the ending X of the image
     * @return the center point of x
     */
    int getCenterX();

    /**
     * Gets the center point between the origin Y and the ending Y of the image
     * @return the center point of y
     */
    int getCenterY();

    /**
     * Gets the image of the car
     * @return the image
     */
    Image getImage();

    /**
     * Gets the {@link Rectangle} of this car
     * @return the collider
     */
    Rectangle getCollider();

    /**
     * Gets the width of this car
     * @return the width
     */
    int getWidth();

    /**
     * Gets the direction which this car is moving to.
     * @return the direction
     */
    Direction getDirection();

    /**
     * Performs the movement of the car's {@link Rectangle}
     */
    void move();
}
