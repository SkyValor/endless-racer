package personal.exercises.endlessRacer.car.enemyCars;

import personal.exercises.endlessRacer.car.AbstractCar;

public class SmallCar extends AbstractCar {

    private static final int DELTA_Y = 5;
    private static final int turboMultiplier = 5;

    public SmallCar(int posX, int posY, SmallCarColor color) {
        super(posX, posY, 0, DELTA_Y);

        String imageFilePath = "resources/enemyCar_";
        switch (color) {
            case BLUE:
                imageFilePath += "blue";
                break;

            case TURQUOISE:
                imageFilePath += "turquoise";
                break;

            case PURPLE:
                imageFilePath += "purple";
                break;
        }

        imageFilePath += ".png";
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
