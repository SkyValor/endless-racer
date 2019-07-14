import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {


    public GameWindow() throws InterruptedException {
        super.add(new Game());
//this is a comment
        super.setTitle("Race to the finish! ...And don't die trying");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setSize(1280, 720);
        super.setBackground(Color.BLUE);
        super.setLocationRelativeTo(null);
        super.setResizable(false);
        super.setVisible(true);
    }

    public static void main (String[] args) throws InterruptedException {

        new GameWindow();
    }
}
