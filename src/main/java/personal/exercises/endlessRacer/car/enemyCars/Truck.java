package personal.exercises.endlessRacer.car.enemyCars;

import personal.exercises.endlessRacer.car.AbstractCar;

public class Truck extends AbstractCar {

    private static final int DELTA_Y = 2;
    private static final int turboMultiplier = 11;

    public Truck(int posX, int posY) {
        super(posX, posY, 0, DELTA_Y);

        String imageFilePath = "resources/truck.png";
        super.setImageAndBound(imageFilePath);
    }

    @Override
    public void move() {

        if (getTurbo()) {
            getCollider().translate(0, turboMultiplier * getDeltaY());
        }

        else {
            getCollider().translate(0, getDeltaY());
        }
    }
}
