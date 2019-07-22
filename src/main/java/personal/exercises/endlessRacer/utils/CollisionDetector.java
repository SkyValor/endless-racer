package personal.exercises.endlessRacer.utils;

import personal.exercises.endlessRacer.car.Car;
import personal.exercises.endlessRacer.car.Direction;
import personal.exercises.endlessRacer.car.specialCars.PlayerCar;
import personal.exercises.endlessRacer.car.specialCars.rival.RivalCar;

import java.util.LinkedList;

public class CollisionDetector {

    /**
     * Tests if the received {@link Car} is intersecting with another car from the list of existing cars.
     *
     * @param cars the list of existing cars
     * @param mockCar the car to test collision
     * @return true if there is no collision; false otherwise
     */
    static boolean isSpawnPermitted(LinkedList<Car> cars, Car mockCar) {

        for (Car car : cars) {

            if (car.getCollider().intersects(mockCar.getCollider())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if there is a collision between the {@link PlayerCar} and the {@link RivalCar}.
     * <p>
     * In case of collision, forces both parties to move apart from each other, according to the details
     * of the collision.
     *
     * @param player the player
     * @param rival the rival
     * @see PlayerCar#moveFromImpact(Direction, int, int)
     * @see RivalCar#moveFromImpact(Direction, int, int)
     */
    public static void detectCollisionPlayerRival(PlayerCar player, RivalCar rival) {

        if (!player.getCollider().intersects(rival.getCollider())) {
            return;
        }

        // if playerCar is at left  &  not in front or behind
        if ((player.getCenterX() < rival.getCenterX())
                && !((player.getY2() < rival.getY())  || (player.getY() > rival.getY2()))) {

            if (player.isMovingRight() && !rival.isMovingLeft()) {

                player.moveFromImpact(Direction.LEFT, 1, 1);
                rival.moveFromImpact(Direction.RIGHT, 2, 1);
                return;
            }

            if (!player.isMovingRight() && rival.isMovingLeft()) {

                player.moveFromImpact(Direction.LEFT, 2, 1);
                rival.moveFromImpact(Direction.RIGHT, 1, 1);
                return;
            }

            if (player.isMovingRight() && rival.isMovingLeft()) {

                player.moveFromImpact(Direction.LEFT, 1, 1);
                rival.moveFromImpact(Direction.RIGHT, 1, 1);
                return;
            }
        }

        // if playerCar is at right  &  not in front or behind
        if ((player.getCenterX() > rival.getCenterX())
                && !((player.getY2() < rival.getY()) || (player.getY() > rival.getY2()))) {

            if (player.isMovingLeft() && !rival.isMovingRight()) {

                player.moveFromImpact(Direction.RIGHT, 1, 1);
                rival.moveFromImpact(Direction.LEFT, 2, 1);
                return;
            }

            if (!player.isMovingLeft() && rival.isMovingRight()) {

                player.moveFromImpact(Direction.RIGHT, 2, 1);
                rival.moveFromImpact(Direction.LEFT, 1, 1);
                return;
            }

            if (player.isMovingLeft() && rival.isMovingRight()) {

                player.moveFromImpact(Direction.RIGHT, 1, 1);
                rival.moveFromImpact(Direction.LEFT, 1, 1);
                return;
            }
        }

        // if playerCar is in front  &  not at left or right
        if ((player.getCenterY() < rival.getCenterY())
                && !((player.getX2() < rival.getX()) || (player.getX() > rival.getX2()))) {

            if (player.isMovingDown() && !rival.isMovingUp()) {

                player.moveFromImpact(Direction.UP, 1, 1);
                rival.moveFromImpact(Direction.DOWN, 2, 1);
                return;
            }

            if (rival.isMovingUp() && !player.isMovingDown()) {

                player.moveFromImpact(Direction.UP, 2, 1);
                rival.moveFromImpact(Direction.DOWN, 1, 1);
                return;
            }

            if (player.isMovingDown() && rival.isMovingUp()) {

                player.moveFromImpact(Direction.UP, 1, 1);
                rival.moveFromImpact(Direction.DOWN, 1, 1);
                return;
            }
        }

        // if playerCar is behind  &  not at left or right
        if ((player.getCenterY() > rival.getCenterY())
                && !((player.getX2() < rival.getX()) || (player.getX() > rival.getX2()))) {

            if (player.isMovingUp() && !rival.isMovingDown()) {

                player.moveFromImpact(Direction.DOWN, 1, 1);
                rival.moveFromImpact(Direction.UP, 2, 1);
                return;
            }

            if (rival.isMovingDown() && !player.isMovingUp()) {

                player.moveFromImpact(Direction.DOWN, 2, 1);
                rival.moveFromImpact(Direction.UP, 1, 1);
                return;
            }

            // both are pushing
            if (player.isMovingUp() && rival.isMovingDown()) {

                player.moveFromImpact(Direction.DOWN, 1, 1);
                rival.moveFromImpact(Direction.UP, 1, 1);
            }
        }
    }

    /**
     * Checks if there is a collision between the {@link PlayerCar} and one {@link Car} from the list of
     * {@code enemyCars}.
     * <p>
     * In case of collision, destroys said car and forces player to stand still for a while, in {@code shielded} state.
     * Should the player not have any remaining {@code lives}, the Game Over screen should be called.
     *
     * @param player the player
     * @param enemyCars the list of enemy enemyCars
     * @see PlayerCar#moveFromImpact(Direction, int, int)
     */
    public static void detectCollisionPlayerNPC(PlayerCar player, LinkedList<Car> enemyCars) {

        Car collidedCar = null;
        for (Car car : enemyCars) {

            if (car.getCollider().intersects(player.getCollider())) {
                collidedCar = car;
                break;
            }
        }

        if (collidedCar == null) {
            return;
        }

        if (player.getLives() > 1) {

            enemyCars.remove(collidedCar);
            player.loseLife();
            player.moveFromImpact(Direction.NONE, 0, 2);
        }

        else {
            // call Game Over screen
        }
    }

    /**
     * Checks if there is a collision between the {@link RivalCar} and one {@link Car} from the list of
     * {@code enemyCars}.
     * <p>
     * In case of collision, if the rival is {@code shielded}, destroy the respective enemy car and force
     * the rival to stand still, in {@code shielded} state. Otherwise, force the rival to move in the opposite
     * direction of collision, in {@code shielded} state.
     *
     * @param rival the rival
     * @param enemyCars the list of enemy enemyCars
     * @see RivalCar#moveFromImpact(Direction, int, int)
     */
    public static void detectCollisionRivalNPC(RivalCar rival, LinkedList<Car> enemyCars) {

        Car collidedCar = null;
        for (Car car : enemyCars) {

            if (car.getCollider().intersects(rival.getCollider())) {
                collidedCar = car;
                break;
            }
        }

        if (collidedCar == null) {
            return;
        }

        if (rival.isShielded()) {
            enemyCars.remove(collidedCar);   // TODO: check if enemy does not need to be destroyed, even without the shield
            rival.moveFromImpact(Direction.NONE, 0, 2);
            return;
        }

        // if enemy car is colliding from the left
        if (collidedCar.getCenterX() < rival.getCenterX()
                && !((collidedCar.getY2() < rival.getY()) || (collidedCar.getY() > rival.getY2()))) {
            rival.moveFromImpact(Direction.RIGHT, 2, 2);
        }

        // if enemy car is colliding from the right
        if (collidedCar.getCenterX() > rival.getCenterX()
                && !((collidedCar.getY2() < rival.getY()) || (collidedCar.getY() > rival.getY2()))) {
            rival.moveFromImpact(Direction.LEFT, 2, 2);
        }

        // if enemy car is colliding from the front
        if (collidedCar.getCenterY() < rival.getCenterY()
                && !((collidedCar.getX2() < rival.getX()) || (collidedCar.getX() > rival.getX2()))) {
            rival.moveFromImpact(Direction.DOWN, 2, 2);
        }

        // if enemy car is colliding from behind
        if (collidedCar.getCenterY() > rival.getCenterY()
                && !((collidedCar.getX2() < rival.getX()) || (collidedCar.getX() > rival.getX2()))) {
            rival.moveFromImpact(Direction.UP, 2, 2);
        }
    }


    // overloading method | in construction
    // TODO: some logic goes outside this method and into where it is being called
    private Direction detectCollisionRivalNpc(RivalCar rival, LinkedList<Car> cars) {

        Car collidedCar = null;
        for (Car car : cars) {

            if (car.getCollider().intersects(rival.getCollider())) {
                collidedCar = car;
                break;
            }
        }

        if (collidedCar == null) {
            return Direction.NONE;
        }

        // if enemy car is colliding from the left
        if (collidedCar.getCenterX() < rival.getCenterX()
                && !((collidedCar.getY2() < rival.getY()) || (collidedCar.getY() > rival.getY2()))) {
            return Direction.LEFT;
        }

        // if enemy car is colliding from the right
        if (collidedCar.getCenterX() > rival.getCenterX()
                && !((collidedCar.getY2() < rival.getY()) || (collidedCar.getY() > rival.getY2()))) {
            return Direction.RIGHT;
        }

        // if enemy car is colliding from the front
        if (collidedCar.getCenterY() < rival.getCenterY()
                && !((collidedCar.getX2() < rival.getX()) || (collidedCar.getX() > rival.getX2()))) {
            return Direction.UP;
        }

        // if enemy car is colliding from behind
        if (collidedCar.getCenterY() > rival.getCenterY()
                && !((collidedCar.getX2() < rival.getX()) || (collidedCar.getX() > rival.getX2()))) {
            return Direction.DOWN;
        }

        return Direction.NONE;
    }
}
