package personal.exercises.endlessRacer;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int BOUNDS_OFFSET = 50;

    private GameWindow() throws InterruptedException {

        super.add(new Game());

        super.setTitle("Race to the finish! ...And don't die trying");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setSize(WIDTH, HEIGHT);
        super.setBackground(Color.BLUE);

        super.setLocationRelativeTo(null);
        super.setResizable(false);
        super.setVisible(true);
    }

    public static void main (String[] args) throws InterruptedException {
        new GameWindow();
    }
}
