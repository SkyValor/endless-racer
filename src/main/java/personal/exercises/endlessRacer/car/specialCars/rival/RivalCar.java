package personal.exercises.endlessRacer.car.specialCars.rival;

import personal.exercises.endlessRacer.car.Car;
import personal.exercises.endlessRacer.car.Direction;
import personal.exercises.endlessRacer.car.DirectionMapper;
import personal.exercises.endlessRacer.car.specialCars.SpecialCar;

import java.awt.*;
import java.util.LinkedList;

public class RivalCar extends SpecialCar {

    private static final int VELOCITY = 4;

    //private int initialX, initialY;                     // for Push method
    //private boolean pushHappeningX, pushHappeningY;     // checks if method is in course
    //private boolean isAvoidingNPC;                      // returns TRUE if it's in the middle of avoiding the NPCs

    private RivalAi ai;

    //  Constructor
    public RivalCar(int posX, int posY) {
        super(posX, posY, VELOCITY, VELOCITY);

        String imageFilePath = "rivalCar.png";
        super.setImageAndBound(imageFilePath);

        //initialX = -10;
        //initialY = -10;
        //pushHappeningX = false;
        //pushHappeningY = false;

        ai = new RivalAi();
    }

    public void moveFromImpact(Direction direction, int force, int time) {
        super.moveFromImpact(direction, force, time, VELOCITY, VELOCITY);
    }

    /**
     * The {@link RivalAi} kick-in by setting the correct values for the movement of this {@link RivalCar} and follows
     * with the proper movement of all the colliders associated with this {@code rival car}.
     * <p>
     * The list of enemy cars and the player (both received as parameters) are to be passed to the {@code rival AI} for
     * proper calculations.
     *
     * @param cars the list of enemy cars
     * @param player the player
     * @see RivalAi#setAiMovement(LinkedList, Car)
     * @see RivalAi#move()
     */
    // TODO: update the documentation ; no longer works this way
    public void move(LinkedList<Car> cars, Car player) {

        ai.setAiMovement(cars, player);
        ai.move();
    }



    /**
     * Class that works up the artificial intelligence of the {@link RivalCar}.
     */
    private class RivalAi {

        // colliders for frontal NPC detection / a.k.a Lanes
        private Rectangle colCenter;    // 1
        private Rectangle colRight1;    // 2
        private Rectangle colLeft1;     // 3
        private Rectangle colRight2;    // 4
        private Rectangle colLeft2;     // 5

        // colliders for left and right NPC detection
        private Rectangle squareLeft;
        private Rectangle squareRight;

        // dimensions for colliders
        private final int colWidth = 72;
        private final int colHeight = 100;
        private final int squareDimension = 80;

        private RivalAiState state;

        /**
         * Constructor for the artificial intelligence. Should be called when {@link RivalCar} is instantiated.
         */
        private RivalAi() {

            colCenter = new Rectangle(collider.x - 16, collider.y - colHeight, colWidth, colHeight);
            colLeft1 = new Rectangle(colCenter.x - colWidth, colCenter.y, colWidth, colHeight);
            colLeft2 = new Rectangle(colLeft1.x - colWidth, colCenter.y, colWidth, colHeight);
            colRight1 = new Rectangle(colCenter.x + colWidth, colCenter.y, colWidth, colHeight);
            colRight2 = new Rectangle(colRight1.x + colWidth, colCenter.y, colWidth, colHeight);

            squareLeft = new Rectangle(collider.x - squareDimension - 1, collider.y, squareDimension, squareDimension);
            squareRight = new Rectangle(collider.x + collider.width + 1, collider.y, squareDimension, squareDimension);
        }

        /**
         * Performs the necessary verifications within the AI logic to implement the best movement deltas of this car.
         * <p>
         * If the car is in {@code shielded} state, the movement should depend on {@code force}. Otherwise, invoke
         * each of this AI's movement methods in the correct order of importance, to set the correct properties for
         * the movement of {@link RivalCar}.
         * <p>
         * If nothing is needed, the {@code direction} is set to {@link Direction#NONE}.
         *
         * @param enemyCars the list of enemy cars
         * @param player the player
         * @see RivalAi#moveByAvoiding(LinkedList)
         * @see RivalAi#moveByClosingIn(LinkedList, Car)
         * @see RivalAi#moveByCrashing(Car)
         */
        private void setAiMovement(LinkedList<Car> enemyCars, Car player) {

            if (shielded) {
                return;
            }

            // TODO: check if the order does not need to change
            // TODO: each item should invoke the next, in which affirmative case should override the action

            if (moveByAvoiding(enemyCars)) {
                return;
            }

            if (moveByClosingIn(enemyCars, player)) {
                return;
            }

            if (moveByCrashing(player)) {
                return;
            }

            //setDeltaX(0);
            //setDeltaY(0);

            setDirection(Direction.NONE);
        }

        /**
         * Moves all the colliders that affect the {@link RivalCar}, based on the delta values, which are the main
         * collider (from the {@code RivalCar}, the frontal colliders and side squares for movement AI decision-making.
         *
         * @see Rectangle#translate(int, int)
         */
        private void move() {

            // main collider
            collider.translate(getDeltaX(), getDeltaY());

            // frontal colliders
            colLeft2.translate(getDeltaX(), getDeltaY());
            colLeft1.translate(getDeltaX(), getDeltaY());
            colCenter.translate(getDeltaX(), getDeltaY());
            colRight1.translate(getDeltaX(), getDeltaY());
            colRight2.translate(getDeltaX(), getDeltaY());

            // side colliders
            squareLeft.translate(getDeltaX(), getDeltaY());
            squareRight.translate(getDeltaX(), getDeltaY());
        }

        /**
         * Verifies if the first lane of collision detection intersects an enemy car, in which case follows the method
         * to the next lane and so on. Based on the first lane that is free from collision with an enemy car, moves the
         * {@link RivalCar} to the {@code direction} of the respective lane.
         * <p>
         * If all lanes are being intersected with enemy cars, there is no chance to escape and the {@code RivalCar}
         * ends up giving up trying to avoid them. Otherwise, if there is no need to avoid any cars, the AI de-selects
         * this {@code state} from its property (if it was selected).
         *
         * @param enemyCars the list of enemy cars
         * @return true if avoiding enemy cars is needed; false otherwise
         */
        private boolean moveByAvoiding(LinkedList<Car> enemyCars) {

            // check if any enemy car collides with lane 1
            for (Car car1 : enemyCars) {
                if (carCollidesWithLane(car1.getCollider(), 1)) {


                    // if TRUE, check if any enemy car collides with lane 2
                    for (Car car2 : enemyCars) {
                        if (carCollidesWithLane(car2.getCollider(), 2)) {


                            // if TRUE, check if any enemy car collides with lane 3
                            for (Car car3 : enemyCars) {
                                if (carCollidesWithLane(car3.getCollider(), 3)) {


                                    // if TRUE, check if any enemy car collides with lane 4
                                    for (Car car4 : enemyCars) {
                                        if (carCollidesWithLane(car4.getCollider(), 4)) {


                                            // if TRUE, check if any enemy car collides with lane 5
                                            for (Car car5 : enemyCars) {
                                                if (carCollidesWithLane(car5.getCollider(), 5)) {
                                                    // if TRUE, there is no way to escape
                                                    return false;
                                                }
                                            }

                                            // if nothing collides with lane 5 , set movement to LEFT
                                            setDirection(Direction.LEFT);
                                            return true;
                                        }
                                    }

                                    // if nothing collides with lane 4 , set movement to RIGHT
                                    setDirection(Direction.RIGHT);
                                    return true;
                                }
                            }

                            // if nothing collides with lane 3 , set movement to LEFT
                            setDirection(Direction.LEFT);
                            return true;
                        }
                    }

                    // if nothing collides with lane 2 , set movement to RIGHT
                    setDirection(Direction.RIGHT);
                    return true;
                }
            }

            // reaching here means that no NPC collides with lane 1, therefore no need to avoid
            if (state.equals(RivalAiState.AVOIDING)) {
                state = null;
            }

            return false;
        }

        /**
         * Verifies if the {@code collider} of the car given as parameter intersects with the respective {@code lane}
         * (frontal collider) of the {@link RivalCar}.
         *
         * @param enemyCarCollider {@link Rectangle} of the enemy car
         * @param lane the lane for collision detection
         * @return true is enemy car collides with respective lane; false otherwise
         * @see Rectangle#intersects(Rectangle)
         */
        private boolean carCollidesWithLane(Rectangle enemyCarCollider, int lane) {

            switch (lane) {
                case 1:
                    if (colCenter.intersects(enemyCarCollider)) {
                        return true;
                    }
                    break;

                case 2:
                    if (colRight1.intersects(enemyCarCollider)) {
                        return true;
                    }
                    break;

                case 3:
                    if (colLeft1.intersects(enemyCarCollider)) {
                        return true;
                    }
                    break;

                case 4:
                    if (colRight2.intersects(enemyCarCollider)) {
                        return true;
                    }
                    break;

                case 5:
                    if (colLeft2.intersects(enemyCarCollider)) {
                        return true;
                    }
            }

            return false;
        }

        /**
         * Verifies if the {@link personal.exercises.endlessRacer.car.specialCars.PlayerCar} is too far from this
         * {@code rival}, in which case forces the rival to pursue the player.
         * <p>
         * The verification takes into account the positions of the {@code player} and the {@code rival}. Should this
         * verification return {@code true}, change the {@code direction} of this car to close-in on the player's
         * and perform a second verification for the other primitive directions, so to check if {@code rival} should
         * also move in that direction to get closer to the {@code player}.
         *
         * @param enemyCars the list of enemy cars
         * @param player the player
         * @return true if rival should close-in to player and direction was updated; false otherwise
         * @see RivalAi#canMoveLeft(LinkedList)
         * @see RivalAi#canMoveRight(LinkedList)
         * @see DirectionMapper#addDirection(Direction, Direction)
         */
        private boolean moveByClosingIn(LinkedList<Car> enemyCars, Car player) {

            // TODO: make squares more size-appropriate for verification

            Direction toAddDirection;
            int necessaryDistance = 200;
            int distanceMargin = 5;

            // if Player is too far left
            if (player.getCenterX() <= getCenterX() - necessaryDistance && canMoveLeft(enemyCars)) {
                toAddDirection = Direction.LEFT;

                if (player.getCenterY() < getCenterY() - distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.UP);
                }

                if (player.getCenterY() > getCenterY() + distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.DOWN);
                }

                setDirection(toAddDirection);
                return true;
            }

            // if Player is too far right
            else if (player.getCenterX() >= getCenterX() + necessaryDistance && canMoveRight(enemyCars)) {
                toAddDirection = Direction.RIGHT;

                if (player.getCenterY() < getCenterY() - distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.UP);
                }

                if (player.getCenterY() > getCenterY() + distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.DOWN);
                }

                setDirection(toAddDirection);
                return true;
            }


            // if Player is too far forward
            if (player.getCenterY() <= getCenterY() - necessaryDistance) {
                toAddDirection = Direction.UP;

                if (player.getCenterX() < getCenterX() - distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.LEFT);
                }

                if (player.getCenterX() > getCenterX() + distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.RIGHT);
                }

                setDirection(toAddDirection);
                return true;
            }

            // if player is too far behind
            else if (player.getCenterY() >= getCenterY() + necessaryDistance) {
                toAddDirection = Direction.DOWN;

                if (player.getCenterX() < getCenterX() - distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.LEFT);
                }

                if (player.getCenterX() > getCenterX() + distanceMargin) {
                    toAddDirection = DirectionMapper.addDirection(toAddDirection, Direction.RIGHT);
                }

                setDirection(toAddDirection);
                return true;
            }

            return false;
        }

        /**
         * Verifies if the {@link personal.exercises.endlessRacer.car.specialCars.PlayerCar} is too close to this car,
         * in which case forces the rival to enter a crash-course with the respective {@code player}, by setting the
         * correct value of {@code direction}.
         *
         * @param player the player
         * @return true if rival is crashing on the player and direction was updated; false otherwise
         */
        private boolean moveByCrashing(Car player) {

            // TODO: if rival is not crashing and stands still next to player
            if (player.getX2() < getX()) { // Player is at left     // TODO: make getX() + 1 or something
                setDirection(Direction.LEFT);
                return true;
            }

            if (player.getX() > getX2()) { // Player is at right
                setDirection(Direction.RIGHT);
                return true;
            }

            if (player.getY2() < getY()) { // Player is in front
                setDirection(Direction.UP);
                return true;
            }

            if (player.getY() > getY2()) { // Player is behind
                setDirection(Direction.DOWN);
                return true;
            }

            return false;
        }

        /**
         * Verifies if an {@code enemy car} is colliding with the rightward square of this AI. The movement to the
         * sides strictly depends if there would be a collision with a car other than
         * {@link personal.exercises.endlessRacer.car.specialCars.PlayerCar}.
         *
         * @param cars the list of enemy cars
         * @return true if the movement does not result in a collision with an enemy car; false otherwise
         */
        private boolean canMoveRight(LinkedList<Car> cars) {

            for (Car car : cars) {
                if (squareRight.intersects(car.getCollider())) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Verifies if an {@code enemy car} is colliding with the leftward square of this AI. The movement to the
         * sides strictly depends if there would be a collision with a car other than
         * {@link personal.exercises.endlessRacer.car.specialCars.PlayerCar}.
         *
         * @param cars the list of enemy cars
         * @return true if the movement does not result in a collision with an enemy car; false otherwise
         */
        private boolean canMoveLeft(LinkedList<Car> cars) {

            for (Car car : cars) {
                if (squareLeft.intersects(car.getCollider())) {
                    return true;
                }
            }

            return false;
        }
    }
}
