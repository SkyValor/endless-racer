import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener {


    private Car playerCar;
    private Rival rivalCar;

    private int yBackground = -749;             // first background position
    private ArrayList<Background> List_of_backgrounds;

    private final int MAX_SMALL_CARS = 3;
    private ArrayList<Car> List_of_small_cars;

    private final int MAX_TRUCKS = 2;
    private ArrayList<Car> List_of_trucks;

    //private int MAX_MUTATION = 2;
    //private ArrayList<Mutation> List_of_mutation = new ArrayList<Mutation>(MAX_MUTATION);

    private boolean isTurbo;                // speeds-up if Player used Turbo
    //private boolean verifyFlag;

    private int collisionKind;              // determines the collision-pushing behaviour
    private boolean collisionHappening;     // stops the collision detection method


    //  Constructor
    public Game() throws InterruptedException {

        super.setFocusable(true);
        super.setDoubleBuffered(true);
        super.addKeyListener(new KeyboardAdapter());

        List_of_backgrounds = new ArrayList<>();
        List_of_backgrounds.add(new Background(yBackground));

        playerCar = new Car("PLAYER", 540, 600);
        rivalCar = new Rival(700, 300);

        List_of_small_cars = new ArrayList<>(MAX_SMALL_CARS);
        List_of_small_cars.add(new Car("SMALL_CAR", 500, -100));
        List_of_small_cars.add(new Car("SMALL_CAR", 600, -890));
        List_of_small_cars.add(new Car("SMALL_CAR", 750, -540));

        List_of_trucks = new ArrayList<>(MAX_TRUCKS);
        List_of_trucks.add(new Car("TRUCK", 500, -1440));
        List_of_trucks.add(new Car("TRUCK", 700, -500));

        isTurbo = false;
        //verifyFlag = false;
        collisionKind = -1;
        collisionHappening = false;
    }


    //  Methods

    //  Proceed to generate a random NPC car
    private void generateNPC(String carType) {

        int randomX, randomY;   // random position

        if (carType == "SMALL_CAR") {

            //  check if new position is acceptable
            //  LOGIC: min + (max - min + 1)
            do {
                Random tempX = new Random();
                randomX = 464 + tempX.nextInt(816 - 464 + 1 - 58);  // largest Width
                Random tempY = new Random();
                randomY = -(200 + tempY.nextInt(1300 - 200 + 1));

            } while (!verifyPositionX(randomX, randomX + 46)); // Small car's width is 44

            List_of_small_cars.add(new Car("SMALL_CAR", randomX, randomY));
        }


        if (carType == "TRUCK") {

            //  check if new position is acceptable
            //  LOGIC: min + (max - min + 1)
            do {
                Random tempX = new Random();
                randomX = 464 + tempX.nextInt(816 - 464 + 1 - 58);  // largest Width
                Random tempY = new Random();
                randomY = -( 700 + tempY.nextInt(2400 - 700 + 1) );

            } while (!verifyPositionX(randomX, randomX + 60)); // Truck's width is 58

            List_of_trucks.add(new Car("TRUCK", randomX, randomY));
        }
    }


    //  Verifies if new NPC can be placed in random position X
    private boolean verifyPositionX(int posX_left, int posX_right) {

        //  run all small cars
        for (Car smallCar : List_of_small_cars) {

            if ( (posX_left >= smallCar.getX()) && (posX_left <= smallCar.getX2())
                    ||
                    (posX_right >= smallCar.getX()) && (posX_right <= smallCar.getX2())  )
                return false;
        }

        //  run all trucks
        for (Car truck : List_of_trucks) {

            if ( (posX_left >= truck.getX()) && (posX_left <= truck.getX2())
                    ||
                    (posX_right >= truck.getX()) && (posX_right <= truck.getX2())  )
                return false;
        }

        return true;
    }


    //  Verify what type of collision is there between Player and Rival
    private void detectCollisionPlayerRival() throws InterruptedException {

        // see if there is collision
        if (playerCar.getRect().intersects(rivalCar.getRect())) {


            // if playerCar is at left  &  not in front or behind
            if ( (playerCar.getCenterX() < rivalCar.getCenterX())  && !( (playerCar.getY2() < rivalCar.getY())  || (playerCar.getY() > rivalCar.getY2()) ) ) {

                // playerCar is pushing right and rivalCar isn't pushing left
                if (playerCar.getRight() && !rivalCar.getLeft()) {

                    playerCar.movementAccordingToShield("LEFT", 1, 1);
                    rivalCar.movementAccordingToShield("RIGHT", 2, 1);
                    return;
                }

                // rivalCar is pushing left and playerCar isn't pushing
                if (!playerCar.getRight() && rivalCar.getLeft()) {

                    playerCar.movementAccordingToShield("LEFT", 2, 1);
                    rivalCar.movementAccordingToShield("RIGHT", 1, 1);
                    return;
                }

                // both are pushing
                else if (playerCar.getRight() && rivalCar.getLeft()) {

                    playerCar.movementAccordingToShield("LEFT", 1, 1);
                    rivalCar.movementAccordingToShield("RIGHT", 1, 1);
                    return;
                }
            }


            // if playerCar is at right  &  not in front or behind
            if ( (playerCar.getCenterX() > rivalCar.getCenterX()) && !( (playerCar.getY2() < rivalCar.getY()) || (playerCar.getY() > rivalCar.getY2()) ) ) {

                // playerCar is pushing left and rivalCar isn't pushing right
                if (playerCar.getLeft() && !rivalCar.getRight()) {

                    playerCar.movementAccordingToShield("RIGHT", 1, 1);
                    rivalCar.movementAccordingToShield("LEFT", 2, 1);
                    return;
                }

                // rivalCar is pushing right and playerCar isn't pushing
                if (!playerCar.getLeft() && rivalCar.getRight()) {

                    playerCar.movementAccordingToShield("RIGHT", 2, 1);
                    rivalCar.movementAccordingToShield("LEFT", 1, 1);
                    return;
                }

                // both are pushing
                else if (playerCar.getLeft() && rivalCar.getRight()) {

                    playerCar.movementAccordingToShield("RIGHT", 1, 1);
                    rivalCar.movementAccordingToShield("LEFT", 1, 1);
                    return;
                }
            }


            // if playerCar is in front  &  not at left or right
            if ( (playerCar.getCenterY() < rivalCar.getCenterY()) && !( (playerCar.getX2() < rivalCar.getX()) || (playerCar.getX() > rivalCar.getX2()) ) ) {

                // playerCar is pushing back and rivalCar isn't pushing
                if (playerCar.getDown() && !rivalCar.getUp()) {

                    playerCar.movementAccordingToShield("UP", 1, 1);
                    rivalCar.movementAccordingToShield("DOWN", 2, 1);
                    return;
                }

                // rivalCar is pushing forward and playerCar isn't pushing
                if (rivalCar.getUp() && !playerCar.getDown()) {

                    playerCar.movementAccordingToShield("UP", 2, 1);
                    rivalCar.movementAccordingToShield("DOWN", 1, 1);
                    return;
                }

                // both are pushing
                else if (playerCar.getDown() && rivalCar.getUp()) {

                    playerCar.movementAccordingToShield("UP", 1, 1);
                    rivalCar.movementAccordingToShield("DOWN", 1, 1);
                    return;
                }
            }


            // if playerCar is behind  &  not at left or right
            if ( (playerCar.getCenterY() > rivalCar.getCenterY()) && !( (playerCar.getX2() < rivalCar.getX()) || (playerCar.getX() > rivalCar.getX2()) ) ) {

                // playerCar is pushing forward and rivalCar isn't pushing
                if (playerCar.getUp() && !rivalCar.getDown()) {

                    playerCar.movementAccordingToShield("DOWN", 1, 1);
                    rivalCar.movementAccordingToShield("UP", 2, 1);
                    return;
                }

                // rivalCar is pushing backwards and playerCar isn't pushing
                if (rivalCar.getDown() && !playerCar.getUp()) {

                    playerCar.movementAccordingToShield("DOWN", 2, 1);
                    rivalCar.movementAccordingToShield("UP", 1, 1);
                    return;
                }

                // both are pushing
                else if (playerCar.getUp() && rivalCar.getDown()) {

                    playerCar.movementAccordingToShield("DOWN", 1, 1);
                    rivalCar.movementAccordingToShield("UP", 1, 1);
                    return;
                }
            }
        }
    }


    //  Verify if there is collision between Player and NPC
    /////////////////////////////   UNFINISHED
    private void detectCollisionPlayerNPC() {

        // see if there is collision with Small Cars
        for (Car smallCar : List_of_small_cars) {

            if (playerCar.getRect().intersects(smallCar.getRect())) {

                if (playerCar.lives > 1) {
                    List_of_small_cars.remove(smallCar);
                    playerCar.lives--;
                    playerCar.movementAccordingToShield("NONE", 0, 2);
                }
                else {
                    // call GAME OVER screen
                }
            }
        }

        // see if there is collision with Trucks
        for (Car truck : List_of_trucks) {

            if (playerCar.getRect().intersects(truck.getRect())) {

                if (playerCar.lives > 1) {
                    List_of_trucks.remove(truck);
                    playerCar.lives--;
                    playerCar.movementAccordingToShield("NONE", 0, 2);
                }
                else {
                    // call GAME OVER screen
                }
            }
        }

        // GAME OVER
        //else
    }


    //  Verify if there is collision between Rival and NPC -- SUPER INCOMPLETE
    private void detectCollisionRivalNPC() {

        // if Rival has shield up (is not in control) , destroy NPC
        if (rivalCar.getShield()) {

            // see if there is collision with Small Cars
            for (Car smallCar : List_of_small_cars) {

                if (rivalCar.getRect().intersects(smallCar.getRect())) {
                    List_of_small_cars.remove(smallCar);
                    rivalCar.movementAccordingToShield("NONE", 0, 2);
                }
            }

            // see if there is collision with Trucks
            for (Car truck : List_of_trucks) {

                if (rivalCar.getRect().intersects(truck.getRect())) {
                    List_of_trucks.remove(truck);
                    rivalCar.movementAccordingToShield("NONE", 0, 2);
                }
            }
        }

        // if Rival does not have shield up (is in control) , performs pushing
        else {

            // if collision with Small Car
            for (Car smallCar : List_of_small_cars) {
                if (rivalCar.getRect().intersects(smallCar.getRect())) {

                    // if Small Car is colliding from the left
                    if (smallCar.getCenterX() < rivalCar.getCenterX() && !( (smallCar.getY2() < rivalCar.getY()) || (smallCar.getY() > rivalCar.getY2()) ))
                        rivalCar.movementAccordingToShield("RIGHT", 2, 2);

                    // if Small Car is colliding from the right
                    if (smallCar.getCenterX() > rivalCar.getCenterX() && !( (smallCar.getY2() < rivalCar.getY()) || (smallCar.getY() > rivalCar.getY2()) ))
                        rivalCar.movementAccordingToShield("LEFT", 2, 2);

                    // if Small Car is colliding from the front
                    if (smallCar.getCenterY() < rivalCar.getCenterY() && !( (smallCar.getX2() < rivalCar.getX()) || (smallCar.getX() > rivalCar.getX2()) ))
                        rivalCar.movementAccordingToShield("DOWN", 2, 2);

                    // if Small Car is colliding from behind
                    if (smallCar.getCenterY() > rivalCar.getCenterY() && !( (smallCar.getX2() < rivalCar.getX()) || (smallCar.getX() > rivalCar.getX2()) ))
                        rivalCar.movementAccordingToShield("UP", 2, 2);
                }
            }

            // if collision with Truck
            for (Car truck : List_of_trucks) {
                if (rivalCar.getRect().intersects(truck.getRect())) {

                    // if Truck is colliding from the left
                    if (truck.getCenterX() < rivalCar.getCenterX() && !( (truck.getY2() < rivalCar.getY()) || (truck.getY() > rivalCar.getY2()) ))
                        rivalCar.movementAccordingToShield("RIGHT", 2, 2);

                    // if Truck is colliding from the right
                    if (truck.getCenterX() > rivalCar.getCenterX() && !( (truck.getY2() < rivalCar.getY()) || (truck.getY() > rivalCar.getY2()) ))
                        rivalCar.movementAccordingToShield("LEFT", 2, 2);

                    // if Truck is colliding from the front
                    if (truck.getCenterY() < rivalCar.getCenterY() && !( (truck.getX2() < rivalCar.getX()) || (truck.getX() > rivalCar.getX2()) ))
                        rivalCar.movementAccordingToShield("DOWN", 2, 2);

                    // if Truck is colliding from behind
                    if (truck.getCenterY() > rivalCar.getCenterY() && !( (truck.getX2() < rivalCar.getX()) || (truck.getX() > rivalCar.getX2()) ))
                        rivalCar.movementAccordingToShield("UP", 2, 2);
                }
            }
        }
    }


    //  Checks if there is collision between one given enemy Rectangle and a designated lane ( Rival area of detection )
    //  To be used inside rivalAvoidance()
    private boolean checkAreaOfDetection(Rectangle carRect, int lane) {

        switch (lane) {
            case 1:
                if (rivalCar.getRectCenter().intersects(carRect))
                    return true;

            case 2:
                if (rivalCar.getRectRight1().intersects(carRect))
                    return true;

            case 3:
                if (rivalCar.getRectLeft1().intersects(carRect))
                    return true;

            case 4:
                if (rivalCar.getRectRight2().intersects(carRect))
                    return true;

            default:
                if (rivalCar.getRectLeft2().intersects(carRect))
                    return true;
        }

        return false;
    }


    //  Checks if there is an NPC right
    private boolean checkRight() {

        for (Car smallCar : List_of_small_cars) {
            if (rivalCar.getRightSquare().intersects(smallCar.getRect()))
                return true;
        }

        for (Car truck : List_of_trucks) {
            if (rivalCar.getRightSquare().intersects(truck.getRect()))
                return true;
        }

        return false;
    }


    //  Checks if there is an NPC left
    private boolean checkLeft() {

        for (Car smallCar : List_of_small_cars) {
            if (rivalCar.getLeftSquare().intersects(smallCar.getRect()))
                return true;
        }

        for (Car truck : List_of_trucks) {
            if (rivalCar.getLeftSquare().intersects(truck.getRect()))
                return true;
        }

        return false;
    }


    //  Checks cars that collide with area of detection & send movement direction
    private void rivalAvoidance() {

        //define values for movement
        int right = 2;
        int left = -2;

        // check if any Small Car collide with lane 1
        for (Car smallCar1 : List_of_small_cars) {
            if (checkAreaOfDetection(smallCar1.getRect(), 1)) {


                // if TRUE, check if any Small Car collide with lane 2
                for (Car smallCar2 : List_of_small_cars) {
                    if (checkAreaOfDetection(smallCar2.getRect(), 2)) {


                        // if TRUE, check if any Small Car collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE, check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE, check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise, check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise, check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival Left
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // otherwise , check if any Truck collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }

                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }

                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE, check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // if nothing collides with lane 3 , move Rival LEFT

                        rivalCar.setDistanceX_Avoidance(left);
                        return;
                    }
                }


                // otherwise , check if any Truck collides with lane 2
                for (Car smallCar2 : List_of_small_cars) {
                    if (checkAreaOfDetection(smallCar2.getRect(), 2)) {


                        // if TRUE, check if any Small Car collide with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {

                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }

                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // otherwise, check if any Truck collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // if nothing collides with lane 3 , move Rival LEFT
                        rivalCar.setDistanceX_Avoidance(left);
                        return;
                    }
                }


                // if nothing collides with lane 2 , move Rival RIGHT
                rivalCar.setDistanceX_Avoidance(right);
                return;
            }
        }


        // otherwise , check if any Truck collides with lane 1
        for (Car smallCar1 : List_of_small_cars) {
            if (checkAreaOfDetection(smallCar1.getRect(), 1)) {


                // if TRUE, check if any Small Car collide with lane 2
                for (Car smallCar2 : List_of_small_cars) {
                    if (checkAreaOfDetection(smallCar2.getRect(), 2)) {


                        // if TRUE, check if any Small Car collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE, check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE, check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise, check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise, check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival Left
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // otherwise , check if any Truck collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {

                                        // if TRUE, check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // if nothing collides with lane 3 , move Rival LEFT
                        rivalCar.setDistanceX_Avoidance(left);
                        return;
                    }
                }


                // otherwise , check if any Truck collides with lane 2
                for (Car smallCar2 : List_of_small_cars) {
                    if (checkAreaOfDetection(smallCar2.getRect(), 2)) {


                        // if TRUE, check if any Small Car collide with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // otherwise, check if any Truck collides with lane 3
                        for (Car smallCar3 : List_of_small_cars) {
                            if (checkAreaOfDetection(smallCar3.getRect(), 3)) {


                                // if TRUE , check if any Small Car collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // otherwise , check if any Truck collides with lane 4
                                for (Car smallCar4 : List_of_small_cars) {
                                    if (checkAreaOfDetection(smallCar4.getRect(), 4)) {


                                        // if TRUE , check if any Small Car collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // otherwise , check if any Truck collides with lane 5
                                        for (Car smallCar5 : List_of_small_cars) {
                                            if (checkAreaOfDetection(smallCar5.getRect(), 5)) {return;} // nothing happens here
                                        }


                                        // if nothing collides with lane 5 , move Rival LEFT
                                        rivalCar.setDistanceX_Avoidance(left);
                                        return;
                                    }
                                }


                                // if nothing collides with lane 4 , move Rival RIGHT
                                rivalCar.setDistanceX_Avoidance(right);
                                return;
                            }
                        }


                        // if nothing collides with lane 3 , move Rival LEFT
                        rivalCar.setDistanceX_Avoidance(left);
                        return;
                    }
                }


                // if nothing collides with lane 2 , move Rival RIGHT
                rivalCar.setDistanceX_Avoidance(right);
                return;
            }
        }


        // reaching here means that no NPC collides with lane 1
        rivalCar.setIsAvoidingNPC(false);
    }


    // Checks if Rival is not too far from Player
    private void rivalCloseDistanceToPlayer() {

        // if Player is too far left
        if (playerCar.getCenterX() <= rivalCar.getCenterX() - 200)
            rivalCar.setDistanceX(-2);
        else
            rivalCar.setDistanceX(0);

        // if Player is too far right
        if (playerCar.getCenterX() >= rivalCar.getCenterX() + 200)
            rivalCar.setDistanceX(2);
        else
            rivalCar.setDistanceX(0);

        // if Player is too far forward
        if (playerCar.getCenterY() <= rivalCar.getCenterY() - 200)
            rivalCar.setDistanceY(-2);
        else
            rivalCar.setDistanceX(0);

        // if Player is too far behind
        if (playerCar.getCenterY() >= rivalCar.getCenterY() + 200)
            rivalCar.setDistanceY(2);
        else
            rivalCar.setDistanceX(0);
    }


    //  Rival attempts to enter a crash course with Player
    public void rivalCrashAttempt() {

        if (playerCar.getX2() < rivalCar.getX()) // Player is at left
            rivalCar.setDistanceX(-4);

        if (playerCar.getX() > rivalCar.getX2()) // Player is at right
            rivalCar.setDistanceX(4);

        if (playerCar.getY2() < rivalCar.getY()) // Player is in front
            rivalCar.setDistanceY(-4);

        if (playerCar.getY() > rivalCar.getY2()) // Player is behind
            rivalCar.setDistanceY(4);
    }


    //  Performs the painting of the game and gameplay flow
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

         //////////////////
        //  Background  //
       //////////////////

        // paint the images
        for (Background background : List_of_backgrounds) {

            background.move(isTurbo);
            g2d.drawImage(background.getImage(), 0, background.getY(), this);
        }

        //  add new background, when necessary
        if ( (List_of_backgrounds.get(0).getY() >= 0) && (List_of_backgrounds.get(0).getIsAlone()) ) {

            List_of_backgrounds.get(0).setIsAlone(false);
            List_of_backgrounds.add(new Background(List_of_backgrounds.get(0).getY() - 1440));
        }

        //  remove old background when Off-screen
        if (List_of_backgrounds.get(0).getY() >= 720) {

            List_of_backgrounds.remove(List_of_backgrounds.get(0));
        }


         ///////////////////////////////
        //  Collision and Slow-down  //
       ///////////////////////////////

        // check if cars are Off-street
        playerCar.verifySpeed();
        rivalCar.verifySpeed();

        // check if Player and Rival collide
        try {
            detectCollisionPlayerRival();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check if Player and NPC collide
            detectCollisionPlayerNPC();

        // check if Rival and NPC collide
        detectCollisionRivalNPC();


         //////////////
        //  Player  //
       //////////////

        // paint the image
        g2d.drawImage(playerCar.getImage(), playerCar.getX(), playerCar.getY(), this);

        // apply movement of Player
        playerCar.move();

        //  If Player is invincible, paint the Shield
        if (playerCar.getShield())
            g2d.drawImage(playerCar.getImage_invincible(), playerCar.getX(), playerCar.getY(), this);


         /////////////
        //  Rival  //
       /////////////

        //  Paint the rival
        g2d.drawImage(rivalCar.getImage(), rivalCar.getX(), rivalCar.getY(), this);

        //  Rival AI -- [1] movementShield   [2] movementAvoid   [3] movementCloseIn   [4] movementCrashing
        if (!rivalCar.getShield()) {
            rivalAvoidance();

            if (!rivalCar.getAvoid()) {
                rivalCloseDistanceToPlayer();

                if (!rivalCar.getCloseIn()) {
                    rivalCrashAttempt();
                }
            }
        }

        // apply Rival movement
        rivalCar.move();

        //  If Rival is invincible, paint the Shield
        if (rivalCar.getShield())
            g2d.drawImage(rivalCar.getImage_invincible(), rivalCar.getX(), rivalCar.getY(), this);


         //////////////////
        //  Small Cars  //
       //////////////////

        // paint the small cars
        for (Car smallCar : List_of_small_cars) {

            smallCar.enemyMove(isTurbo);
            g2d.drawImage(smallCar.getImage(), smallCar.getX(), smallCar.getY(), this);

            //  Destroy small car if Off-screen
            if (smallCar.getY() >= 725)
                List_of_small_cars.remove(smallCar);
        }

        // generate new Small Car if needed
        if (List_of_small_cars.size() < MAX_SMALL_CARS)
            generateNPC("SMALL_CAR");


         //////////////
        //  Trucks  //
       //////////////

        // paint the trucks
        for (Car truck : List_of_trucks) {

            truck.truckMove(isTurbo);
            g2d.drawImage(truck.getImage(), truck.getX(), truck.getY(), this);

            //  Destroy truck if Off-screen
            if (truck.getY() >= 725)
                List_of_trucks.remove(truck);
        }

        // generate new Truck if needed
        if (List_of_trucks.size() < MAX_TRUCKS)
            generateNPC("TRUCK");
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class KeyboardAdapter extends KeyAdapter {

        @Override
        public void keyPressed (KeyEvent e) {

            try {
                playerCar.keyPressed(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            //System.out.println("X= " + playerCar.getX() + " Y= " + playerCar.getY());
        }

        @Override
        public  void keyReleased (KeyEvent e) {

            playerCar.keyReleased(e);
        }
    }
}
