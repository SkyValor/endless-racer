package personal.exercises.endlessRacer;

import personal.exercises.endlessRacer.car.AbstractCar;
import personal.exercises.endlessRacer.car.Car;
import personal.exercises.endlessRacer.car.Direction;
import personal.exercises.endlessRacer.car.enemyCars.EnemyCarType;
import personal.exercises.endlessRacer.car.enemyCars.SmallCar;
import personal.exercises.endlessRacer.car.enemyCars.Truck;
import personal.exercises.endlessRacer.car.specialCars.PlayerCar;
import personal.exercises.endlessRacer.car.specialCars.rival.RivalCar;
import personal.exercises.endlessRacer.utils.CarFactory;
import personal.exercises.endlessRacer.utils.CollisionDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.*;

public class Game extends JPanel implements ActionListener {

    public static final int ROAD_OFFSET = 464;
    public static final int ROAD_WIDTH = 816;
    public static final int OFF_STREET_OFFSET = 426;
    private static final int ENEMY_OUT_OF_BOUNDS_OFFSET = 725;
    public static final int LARGEST_CAR_WIDTH = new Truck(0,0).getWidth();

    private PlayerCar playerCar;
    private RivalCar rivalCar;

    private ArrayList<Background> backgrounds;

    private LinkedList<Car> enemyCars;
    private EnemyCarType nextCarType;

    //private int MAX_MUTATION = 2;
    //private ArrayList<Mutation> List_of_mutation = new ArrayList<Mutation>(MAX_MUTATION);

    //private boolean verifyFlag;

    /**
     * Constructor method. Sets key features to the application.
     */
    Game() {

        super.setFocusable(true);
        super.setDoubleBuffered(true);
        super.addKeyListener(new KeyboardAdapter());

        init();
    }

    /**
     * Initializes some properties and generate the initial enemy cars.
     */
    private void init() {

        backgrounds = new ArrayList<Background>();
        backgrounds.add(new Background(0));

        playerCar = new PlayerCar(540, 600);
        rivalCar = new RivalCar(700, 300);

        enemyCars = new LinkedList<Car>();
        enemyCars = CarFactory.generateInitialNpcs();

        //verifyFlag = false;
    }

    /**
     * Calls the {@link CarFactory} to attempt to instantiate another car of the missing type in the list of enemy
     * cars, which doesn't intersect with any existing car.
     *
     * @param nextCarType the type of car to be instantiated
     */
    private void generateNPC(EnemyCarType nextCarType) {

        Car nextCar = CarFactory.createNPC(nextCarType, enemyCars);

        if (nextCar != null) {
            enemyCars.add(nextCar);
        }
    }

    /**
     * Performs the necessary method invocation of each component, which maintains the flow of the game in proper way.
     */
    private void gameLoop() {

        System.out.println("Game loop");

        for (Background background : backgrounds) {
            background.move(AbstractCar.getTurbo());
        }

        //  add new background, when necessary
        if ((backgrounds.get(0).getY() >= 0) && (backgrounds.get(0).isAlone())) {

            backgrounds.get(0).setAloneToFalse();
            backgrounds.add(new Background(backgrounds.get(0).getY() - 1440));  // TODO: logic without magic number
        }

        //  remove old background, when off-screen
        if (backgrounds.get(0).getY() >= GameWindow.HEIGHT) {
            backgrounds.remove(backgrounds.get(0));
        }

        // check if cars are off-street
        playerCar.verifySpeed();
        rivalCar.verifySpeed();

        CollisionDetector.detectCollisionPlayerRival(playerCar, rivalCar);
        CollisionDetector.detectCollisionPlayerNPC(playerCar, enemyCars);
        CollisionDetector.detectCollisionRivalNPC(rivalCar, enemyCars);

        playerCar.move();
        rivalCar.move();

        for (Car enemyCar : enemyCars) {
            enemyCar.move();

            //  destroy enemyCar if off-screen and build new enemyCar of same type
            if (enemyCar.getY() >= ENEMY_OUT_OF_BOUNDS_OFFSET) {

                System.out.println("Enemy past its bounds deadline");

                nextCarType = (enemyCar instanceof SmallCar) ? EnemyCarType.SMALL_CAR : EnemyCarType.TRUCK;
                generateNPC(nextCarType);
                nextCarType = null;

                enemyCars.remove(enemyCar);
                break;
            }
        }
    }

    /**
     * Calls the game loop to maintain it working frame-by-frame. It also draws every visual component of this
     * {@code Game} on their respective positions.
     *
     * @param g the graphics context to use for painting
     */
    public void paint(Graphics g) {
        gameLoop();

        Graphics2D g2d = (Graphics2D) g;

        System.out.println("paint");

        for (Background background : backgrounds) {
            g2d.drawImage(background.getImage(), 0, background.getY(), this);
            System.out.println("paint - backgrounds");
        }

        g2d.drawImage(playerCar.getImage(), playerCar.getX(), playerCar.getY(), this);
        System.out.println("paint - playerCar");

        if (playerCar.isShielded()) {
            g2d.drawImage(playerCar.getInvincibleImage(), playerCar.getX(), playerCar.getY(), this);
            System.out.println("paint - playerShield");
        }

        g2d.drawImage(rivalCar.getImage(), rivalCar.getX(), rivalCar.getY(), this);
        System.out.println("paint - rivalCar");

        if (rivalCar.isShielded()) {
            g2d.drawImage(rivalCar.getInvincibleImage(), rivalCar.getX(), rivalCar.getY(), this);
            System.out.println("paint - rivalShield");
        }

        for (Car enemyCar : enemyCars) {
            g2d.drawImage(enemyCar.getImage(), enemyCar.getX(), enemyCar.getY(), this);
            System.out.println("paint - enemyCars");
        }
    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class KeyboardAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent key) {

            // method works only if player has control over his car
            if (playerCar.isShielded()) {
                return;
            }

            // TODO: implement addDirection for diagonals
            // TODO: probably will have to use if-statement

            int keyCode = key.getKeyCode();
            switch (keyCode) {
                case VK_UP:
                    playerCar.setDirection(Direction.UP);
                    break;

                case VK_RIGHT:
                    playerCar.setDirection(Direction.RIGHT);
                    break;

                case VK_DOWN:
                    playerCar.setDirection(Direction.DOWN);
                    break;

                case VK_LEFT:
                    playerCar.setDirection(Direction.LEFT);
            }
        }

        @Override
        public void keyReleased(KeyEvent key) {

            // method works only if Player has control over his car
            if (playerCar.isShielded()) {
                return;
            }

            // TODO: implement the removeDirection, from diagonal to primitive

            int code = key.getKeyCode();
            if (code == VK_UP || code == VK_DOWN || code == VK_RIGHT || code == VK_LEFT) {
                playerCar.setDirection(Direction.NONE);
            }
        }
    }

}
