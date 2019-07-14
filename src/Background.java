import javax.swing.*;
import java.awt.*;

public class Background {


    private enum StreetType {STRAIGHT , TURN_RIGHT , TURN_LEFT}

    private int y;
    private Image image_Background;
    private String backgroundType;
    private boolean isAlone;

    // Lines for STRAIGHT Background
    //private ArrayList<Line2D> List_of_lines = new ArrayList<>();


    //  Constructor
    public Background(int _y) {

        /*
        // generate one random Background
        Random random = new Random();
        int typeInt = random.nextInt(3);

        switch (typeInt) {
            case 0: backgroundType = "STRAIGHT";
                break;
            case 1: backgroundType = "TURN_RIGHT";
                break;
            default: backgroundType = "TURN_LEFT";
        }
        */

        this.y = _y;
        ImageIcon imgIcon = new ImageIcon("Resources//field_1.jpg");
        image_Background = imgIcon.getImage();

        // set the List of lines
        //Line2D line = new Line2D.Double();
        //line.setLine(1,2,3,4);
        //List_of_lines.add(line);


        //line_Str_L.setLine(427, _y, 427, _y + 1440);
        //line_Str_R.setLine(854, _y, 854, _y + 1440);

        isAlone = true;
    }

    //  Methods

    public int getY() { return this.y; }

    public Image getImage() { return this.image_Background; }

    public String getBackgroundType() { return this.backgroundType; }

    public boolean getIsAlone() { return this.isAlone; }
    public void setIsAlone(boolean coiso) { this.isAlone = coiso; }

    public void move(boolean turbo) {

        if (turbo)
            this.y += 30;
        else
            this.y += 10;
    }
}
