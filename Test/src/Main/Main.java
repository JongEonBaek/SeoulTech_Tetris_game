package Main;

import component.Board;
import javax.swing.*;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main{
    public static JPanel mainPanel;
    public static Board gamePanel;
    public static JFrame frame;
    public static void main(String[] args){
        gamePanel = new Board();
        mainPanel.add(gamePanel);
        frame.add(mainPanel);
        gamePanel.timer.start();
        gamePanel.setSize(1920, 1280);
        gamePanel.setVisible(true);

    }

}
