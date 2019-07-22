package personal.exercises.endlessRacer.utils;

import personal.exercises.endlessRacer.Game;
import personal.exercises.endlessRacer.car.Car;
import personal.exercises.endlessRacer.car.enemyCars.EnemyCarType;
import personal.exercises.endlessRacer.car.enemyCars.SmallCar;
import personal.exercises.endlessRacer.car.enemyCars.SmallCarColor;
import personal.exercises.endlessRacer.car.enemyCars.Truck;

import java.util.LinkedList;
import java.util.Random;

public class CarFactory {

    private static final int MIN_SPAWN_Y = 200;
    private static final int MAX_SPAWN_Y = 1300;

    /**
     * With the specified {@link personal.exercises.endlessRacer.car.enemyCars.EnemyCarType} received as a parameter,
     * perform a loop to generate a new car and test if it can be spawned on the randomized position.
     *
     * @param carType the car type to be created
     * @param cars the list of existing cars
     * @return the car if spawn is possible; null otherwise
     * @see CollisionDetector#isSpawnPermitted(LinkedList, Car)
     * @see CarFactory#generateSmallCar(int, int)
     */
    public static Car createNPC(EnemyCarType carType, LinkedList<Car> cars) {

        Car tempCar = null;
        Car returningCar = null;
        Random rand = new Random();

        int numberOfTries = 1;
        boolean isPermitted = false;

        while (!isPermitted && numberOfTries <= 3) {

            // TODO: explain this logic in doc , maybe?

            int randomPosX = Game.ROAD_OFFSET +
                    rand.nextInt(Game.ROAD_WIDTH - Game.ROAD_OFFSET + 1 - Game.LARGEST_CAR_WIDTH);
            int randomPosY = -(MIN_SPAWN_Y + rand.nextInt(MAX_SPAWN_Y - MIN_SPAWN_Y + 1));

            switch (carType) {
                case SMALL_CAR:
                    tempCar = generateSmallCar(randomPosX, randomPosY);
                    break;

                case TRUCK:
                    tempCar = new Truck(randomPosX, randomPosY);
            }
            isPermitted = CollisionDetector.isSpawnPermitted(cars, tempCar);
            ++numberOfTries;
        }

        if (isPermitted) {
            returningCar = tempCar;
        }

        return returningCar;
    }

    /**
     * Method for instantiation and return of a {@link SmallCar} with a randomized {@link SmallCarColor}
     *
     * @param posX the position in the X axis
     * @param posY the position in the Y axis
     * @return the instantiated small car
     */
    private static SmallCar generateSmallCar(int posX, int posY) {

        Random rand = new Random();
        SmallCarColor[] colors = SmallCarColor.values();
        int index = rand.nextInt(colors.length - 1);

        return new SmallCar(posX, posY, colors[index]);
    }

    /**
     * Generates and returns a {@link LinkedList} populated with {@link Car}s manually positioned on the field
     * @return the list of cars
     */
    public static LinkedList<Car> generateInitialNpcs() {

        LinkedList<Car> npcs = new LinkedList<Car>();

        npcs.add(generateSmallCar(500, -100));
        npcs.add(generateSmallCar(600, -890));
        npcs.add(generateSmallCar(750, -540));

        npcs.add(new Truck(500, -1440));
        npcs.add(new Truck(700, -500));

        return npcs;
    }
}
