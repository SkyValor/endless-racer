package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Game extends JPanel implements ActionListener{


    private Player playerCar;
    private Rival rivalCar;

    private ArrayList<Background> List_of_backgrounds;

    private final int MAX_SMALL_CARS = 3;
    private ArrayList<NPC> List_of_small_cars;

    private final int MAX_TRUCKS = 2;
    private ArrayList<NPC> List_of_trucks;

    private ArrayList<ArrayList<NPC>> List_of_NPCs;     // this List will hold all NPCs in one -- for iteration purposes

    private ArrayList<Turbo> List_of_turbos;

    public static boolean isTurbo;                      // speeds-up if Player used Turbo
    private boolean isAwaitingTurboGeneration;          // TRUE , if turboGenerator is already in process


    //  Constructor
    public Game() throws InterruptedException {

        super.setFocusable(true);
        super.setDoubleBuffered(true);
        super.addKeyListener(new KeyboardAdapter());

        List_of_backgrounds = new ArrayList<>();
        List_of_backgrounds.add(new Background(-749));

        playerCar = new Player(540, 600);
        rivalCar = new Rival(700, 300);

        List_of_small_cars = new ArrayList<>(MAX_SMALL_CARS);
        List_of_small_cars.add(new NPC("SMALL_CAR", 500, -100));
        List_of_small_cars.add(new NPC("SMALL_CAR", 600, -890));
        List_of_small_cars.add(new NPC("SMALL_CAR", 750, -540));

        List_of_trucks = new ArrayList<>(MAX_TRUCKS);
        List_of_trucks.add(new NPC("TRUCK", 500, -1440));
        List_of_trucks.add(new NPC("TRUCK", 700, -500));

        List_of_NPCs = new ArrayList<>();
        List_of_NPCs.add(List_of_small_cars);
        List_of_NPCs.add(List_of_trucks);

        List_of_turbos = new ArrayList<>(2);    // when there are 2, one is destroyed

        isTurbo = false;
        isAwaitingTurboGeneration = false;
    }


    //  Methods


    //  Generate Player left or right, depending on availability
    private void generatePlayer() {

        if (rivalCar.getX() > 382)
            playerCar = new Player(382, 600);
        else if (rivalCar.getX2() < 898)
            playerCar = new Player(898, 600);

        // give a 1-second Shield time
        playerCar.movementAccordingToShield("NONE", 0, 1);
    }


    //  Generate Rival left or right, depending on availability
    private void generateRival() {

        if (playerCar.getX() > 382)
            rivalCar = new Rival(382, 600);
        else if (playerCar.getX2() < 898)
            rivalCar = new Rival(898, 600);

        // give a 1-second Shield time
        rivalCar.movementAccordingToShield("NONE", 0, 1);
    }


    //  Proceed to generate a random NPC car
    private void generateNPC(String carType) {

        int randomX, randomY;   // random position

        if (carType.equals("SMALL_CAR")) {

            //  check if new position is acceptable
            //  LOGIC: min + (max - min + 1)

            do {
                Random tempX = new Random();
                randomX = 464 + tempX.nextInt(816 - 464 + 1 - 58);  // largest Width

                Random tempY = new Random();
                randomY = -(200 + tempY.nextInt(1300 - 200 + 1));

            } while (!verifyPositionX(randomX, randomX + 44) && !verifyPositionY(randomY, randomY + 58));  // Small car's width is 44  &  Small car's height is 58

            List_of_small_cars.add(new NPC("SMALL_CAR", randomX, randomY));
        }


        if (carType.equals("TRUCK")) {

            //  check if new position is acceptable
            //  LOGIC: min + (max - min + 1)

            do {
                Random tempX = new Random();
                randomX = 464 + tempX.nextInt(816 - 464 + 1 - 58);  // largest Width

                Random tempY = new Random();
                randomY = -(700 + tempY.nextInt(2400 - 700 + 1));

            } while (!verifyPositionX(randomX, randomX + 58) && !verifyPositionY(randomY, randomY + 128));  // Truck's width is 58  &  Truck's height is 128

            List_of_trucks.add(new NPC("TRUCK", randomX, randomY));
        }
    }


    //  Generate Turbo mutator between X and Y intervals
    private void generateTurbo() {

        Random temp = new Random();
        //int randomX = 13 + temp.nextInt(7); // between 13 and 20
        int randomX = 5;

        int x = 490 + temp.nextInt(793 - Turbo.getWidth() - 490);

        this.isAwaitingTurboGeneration = true;

        // wait the appointed number of seconds before generating another Turbo
        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() { List_of_turbos.add(new Turbo(x, -100)); isAwaitingTurboGeneration = false; }
                }, 1000 * randomX);
    }


    //  Rival should ram onto Player if it's possible and when it's close enough
    private void rivalCrash() {

        // if Player is close enough -- withing X units of distance
        double distance = sqrt( pow ( playerCar.getCenterX() - rivalCar.getCenterX(), 2) + pow ( playerCar.getCenterY() - rivalCar.getCenterY(), 2 ));

        if (distance <= 100) {

            // if Player is in front of Rival
            if ( (playerCar.getY2() <= rivalCar.getY()) && (playerCar.getX2() > rivalCar.getX() || playerCar.getX() < rivalCar.getX2()) )
                rivalCar.setDistance("UP");

            // if Player is behind Rival
            if ( (playerCar.getY() >= rivalCar.getY2()) && (playerCar.getX2() > rivalCar.getX() || playerCar.getX() < rivalCar.getX2()) )
                rivalCar.setDistance("DOWN");

            // if Player is at right of Rival
            if ( (playerCar.getX() >= rivalCar.getX2()) && (playerCar.getY2() > rivalCar.getY() || playerCar.getY() < rivalCar.getY2()) )
                rivalCar.setDistance("RIGHT");

            // if Player is at left of Rival
            if ( (playerCar.getX2() <= rivalCar.getX()) && (playerCar.getY2() > rivalCar.getY() || playerCar.getY() < rivalCar.getY2()) )
                rivalCar.setDistance("LEFT");
        }

        else
            rivalCar.setDistance("NONE");
    }


    //  Rival closes-in on Player if this one is too far, until not so far -- HORIZONTAL AXIS
    private void rivalCloseInX() {

        int distanceX;

        // calculate distance in horizontal units
        if (playerCar.getCenterX() < rivalCar.getCenterX())
            distanceX = rivalCar.getCenterX() - playerCar.getCenterX();
        else
            distanceX = playerCar.getCenterX() - rivalCar.getCenterX();


        // move Rival to close-in if distance is bigger than X units
        if (distanceX > 200) {

            if (playerCar.getCenterX() < rivalCar.getCenterY())
                rivalCar.setDistance("LEFT");

            if (playerCar.getCenterX() > rivalCar.getCenterX())
                rivalCar.setDistance("RIGHT");

            rivalCar.setCloseIn(true);
        }

        else {
            rivalCar.setDistanceX("NONE");  // this prevents Rival from moving too much
            rivalCar.setCloseIn(false);
        }
    }


    //  Rival closes-in on Player if this one is too far, until not so far -- VERTICAL AXIS
    private void rivalCloseInY() {

        int distanceY;

        // calculate distance in vertical units
        if (playerCar.getCenterY() < rivalCar.getCenterY())
            distanceY = rivalCar.getCenterY() - playerCar.getCenterY();
        else
            distanceY = playerCar.getCenterY() - rivalCar.getCenterY();


        // move Rival to close-in if distance is bigger than X units
        if (distanceY > 200) {

            if (playerCar.getCenterY() < rivalCar.getCenterY())
                rivalCar.setDistance("UP");

            if (playerCar.getCenterY() > rivalCar.getCenterY())
                rivalCar.setDistance("DOWN");

            rivalCar.setCloseIn(true);
        }

        else {
            rivalCar.setDistanceY("NONE");  // this prevents Rival from moving too much
            rivalCar.setCloseIn(false);
        }
    }


    //  Rival avoids NPC cars
    private void rivalAvoid() {

        double distance = 800;      // start with something big
        double temp;
        int distanceTrigger = 200;  // at which distance an NPC triggers Rival behaviour

        // when Turbo is being used, double the distance of detection, because NPC's move faster
        if (isTurbo)
            distanceTrigger *= 2;

        NPC closestNPC = null;  // create a null pointer for start

        for ( int i = 0; i < List_of_small_cars.size() + List_of_trucks.size(); i++) {

            // iterate through all NPCs
            for (ArrayList<NPC> List : List_of_NPCs) {
                for (NPC npc : List) {

                    // if NPC is past Rival or isn't in range, doesn't count
                    if (npc.getY() > rivalCar.getY2() || rivalCar.getCenterY() - npc.getCenterY() > distanceTrigger)
                        continue;

                    // calculate distance
                    temp = sqrt(pow(rivalCar.getCenterX() - npc.getCenterX(), 2) + pow(rivalCar.getCenterY() - npc.getCenterY(), 2));

                    // if this value is lower than current distance, assign it as new value
                    if (temp < distance) {
                        distance = temp;
                        closestNPC = npc;
                    }


                    // if there is actually a closest npc
                    if (closestNPC != null) {

                        // if it's quicker to steer left
                        if (closestNPC.getX() >= rivalCar.getX() && closestNPC.getX() <= rivalCar.getX2()) {
                            rivalCar.setDistance("LEFT");
                            rivalCar.setAvoid(true);
                            return;

                        }
                        // if it's quicker to steer right
                        else if (closestNPC.getX2() >= rivalCar.getX() && closestNPC.getX2() <= rivalCar.getX2()) {
                            rivalCar.setDistance("RIGHT");
                            rivalCar.setAvoid(true);
                            return;

                        }
                        // otherwise...
                        else {

                            // if NPC is not in collision course with Rival, skip this NPC
                            if (closestNPC.getX2() <= rivalCar.getX() || closestNPC.getX() >= rivalCar.getX2())
                                continue;

                            // if NPC is at left, steer right
                            if (closestNPC.getCenterX() < rivalCar.getCenterX()) {
                                rivalCar.setDistance("RIGHT");
                                rivalCar.setAvoid(true);
                                return;
                            }

                            // if NPC is at right, steer left
                            else if (closestNPC.getCenterX() > rivalCar.getCenterX()) {
                                rivalCar.setDistance("LEFT");
                                rivalCar.setAvoid(true);
                                return;
                            }
                        }
                    }
                }
            }
        }

        // reaching here means that no avoiding is necessary
        rivalCar.setDistance("NONE");
        rivalCar.setAvoid(false);
    }


    //  Verifies if new NPC can be placed in random position X
    private boolean verifyPositionX(int x, int x2) {

        // iterate through all NPCs
        for (ArrayList<NPC> List : List_of_NPCs) {
            for (NPC npc : List) {

                // if the test values collide with any NPC already initialised, return false
                if ((x >= npc.getX()) && (x <= npc.getX2()) || (x2 >= npc.getX()) && (x2 <= npc.getX2()))
                    return false;
            }
        }

        // otherwise, return true
        return true;
    }


    //  Verifies if new NPC can be placed in random position Y
    private boolean verifyPositionY(int posY_up, int posY_down) {

        // run all small cars
        for (NPC smallCar : List_of_small_cars) {

            if ( (posY_up >= smallCar.getY()) && (posY_up <= smallCar.getY2())
                    ||
                 (posY_down <= smallCar.getY2()) && (posY_down >= smallCar.getY()) )
                return false;
        }

        // run all trucks
        for (NPC truck : List_of_trucks) {

            if ( (posY_up >= truck.getY()) && (posY_up <= truck.getY2())
                    ||
                 (posY_down <= truck.getY2()) && (posY_down >= truck.getY()) )
                return false;
        }

        return true;
    }


    //  Verify what type of collision is there between Player and Rival
    private void detectCollisionPlayerRival() throws InterruptedException {

        // see if there is collision
        if (playerCar.getRect().intersects(rivalCar.getRect())) {


            // NOTE: this only happens if one of them has Shield up already
            if (rivalCar.getShield() && !playerCar.getShield())
                generatePlayer();

            if (playerCar.getShield() && !rivalCar.getShield())
                generateRival();





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
    private void detectCollisionPlayerNPC() {

        // see if there is collision with any NPC
        for (ArrayList<NPC> List : List_of_NPCs) {
            for (NPC npc : List) {

                if (playerCar.getRect().intersects(npc.getRect())) {

                    // destroy NPC
                    List.remove(npc);

                    // the next only happens if Shield if not up
                    if (!playerCar.getShield()) {

                        // if Player has more than 1 life, decrease 1 life and cause Shield for 2 seconds
                        if (playerCar.getLives() > 1) {
                            playerCar.decreaseLives();
                            playerCar.movementAccordingToShield("NONE", 0, 2);
                        }
                    }
                }
            }
        }
    }


    //  Verify if there is collision between Rival and NPC -- SUPER INCOMPLETE
    private void detectCollisionRivalNPC() {

        // if Rival has shield up (is not in control) , destroy NPC
        if (rivalCar.getShield()) {

            // see if there is collision with Small Cars
            for (NPC smallCar : List_of_small_cars) {

                if (rivalCar.getRect().intersects(smallCar.getRect())) {
                    List_of_small_cars.remove(smallCar);
                    rivalCar.movementAccordingToShield("NONE", 0, 2);
                }
            }

            // see if there is collision with Trucks
            for (NPC truck : List_of_trucks) {

                if (rivalCar.getRect().intersects(truck.getRect())) {
                    List_of_trucks.remove(truck);
                    rivalCar.movementAccordingToShield("NONE", 0, 2);
                }
            }
        }

        // if Rival does not have shield up (is in control) , performs pushing
        else {

            // if collision with Small Car
            for (NPC smallCar : List_of_small_cars) {
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
            for (NPC truck : List_of_trucks) {
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


    //  Verify if there is collision between Player and Turbo
    private void detectCollisionPlayerTurbo() {

        if (playerCar.getRect().intersects(List_of_turbos.get(0).getRect())) {
            List_of_turbos.clear(); // Turbo gets destroyed from field

            // if Player doesn't have Shield up, the Turbo is added to it's inventory
            if (!playerCar.getShield())
                playerCar.addTurbo();
        }
    }


    //  Verify is there is collision between Rival and Turbo
    private void detectCollisionRivalTurbo() {

        if (rivalCar.getRect().intersects(List_of_turbos.get(0).getRect()))
            List_of_turbos.clear(); // Turbo gets destroyed and lost
    }


    //  Performs the painting of the game and gameplay flow  -- while gameCycle == true
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;


        //////////////////
        //  Background  //
        //////////////////

        // paint the Backgrounds
        for (Background background : List_of_backgrounds) {

            background.descend(isTurbo);

            g2d.drawImage(background.getImage(), 0, background.getY(), this);
        }

        //  add new background, when necessary
        if ((List_of_backgrounds.get(0).getY() >= 0) && (List_of_backgrounds.size() == 1))
            List_of_backgrounds.add(new Background(List_of_backgrounds.get(0).getY() - 1440));

        //  remove old background when Off-screen
        if (List_of_backgrounds.get(0).getY() >= 720)
            List_of_backgrounds.remove(List_of_backgrounds.get(0));


        //////////////
        //  Turbos  //
        //////////////

        // start the process of generating a new Turbo is there is none in field and none is being used
        if (!isAwaitingTurboGeneration && !isTurbo)
            generateTurbo();

        // if Turbo is being used, remove Turbo from the field, if there's any
        if (isTurbo)
            List_of_turbos.clear();

        // apply movement
        for (Turbo turbo : List_of_turbos)
            turbo.descend();

        // if there is a Turbo on field, destroys it if Off-screen
        if (List_of_turbos.size() > 0) {
            if (List_of_turbos.get(0).getY() > 725)
                List_of_turbos.clear();
        }

        // paint the turbos
        for (Turbo turbo : List_of_turbos)
            g2d.drawImage(turbo.getImage(), turbo.getX(), turbo.getY(), this);


        ///////////////////////////////
        //  Collision and Slow-down  //
        ///////////////////////////////

        // check if cars are Off-street
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

        if (List_of_turbos.size() > 0) {

            // check if Player and Turbo collide
            detectCollisionPlayerTurbo();

            // check if Rival and Turbo collide
            detectCollisionRivalTurbo();
        }


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

        // AI works when Rival not under Shield
        if (!rivalCar.getShield()) {

            // priority is to avoid the NPCs
            rivalAvoid();

            // Rival only closes-in on Player if he is not avoiding the NPCs
            if (!rivalCar.getAvoid()) {
                rivalCloseInX();
                rivalCloseInY();

                // Rival only tries to crash onto Player if there is no Shield up
                if (!rivalCar.getCloseIn() && !playerCar.getShield())
                    rivalCrash();
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
        for (NPC smallCar : List_of_small_cars) {
            smallCar.smallCarMove(isTurbo);
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
        for (NPC truck : List_of_trucks) {
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


    //  Perform the end-game painting -- when gameCycle == false
    public void endPaint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;


        // paint the background

        // paint the Player

        // paint the Rival

        // paint the endGame message
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
        }

        @Override
        public void keyReleased (KeyEvent e) {

            playerCar.keyReleased(e);
        }
    }
}
