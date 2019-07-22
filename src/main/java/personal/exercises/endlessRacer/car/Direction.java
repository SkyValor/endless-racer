package personal.exercises.endlessRacer.car;

public enum Direction {

    UP(0, -1),
    UPPER_RIGHT(1, -1),
    RIGHT(1, 0),
    LOWER_RIGHT(1, 1),
    DOWN(0, 1),
    LOWER_LEFT(-1, 1),
    LEFT(-1, 0),
    UPPER_LEFT(-1, -1),
    NONE(0, 0);

    private int polarityX;
    private int polarityY;

    /**
     * Constructor method. Takes in the parameters for polarity in the X axis and in the Y axis.
     *
     * @param polarityX polarity in the X axis
     * @param polarityY polarity in the Y axis
     */
    Direction(int polarityX, int polarityY) {
        this.polarityX = polarityX;
        this.polarityY = polarityY;
    }

    /**
     * Gets the polarity in the X axis
     * @return the polarity
     */
    public int getPolarityX() {
        return polarityX;
    }

    /**
     * Gets the polarity in the Y axis
     * @return the polarity
     */
    public int getPolarityY() {
        return polarityY;
    }
}
