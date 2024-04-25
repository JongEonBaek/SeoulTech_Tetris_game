package Menu;

import component.*;

import javax.swing.*;
import java.awt.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class Main {
    public static JFrame frame;

    public static JPanel mainPanel;
    public static CardLayout cardLayout;
    public static final int SCREEN_WIDTH[] = {1280, 460, 517, 368};
    public static final int SCREEN_HEIGHT[] = {720, 740, 814, 592};
    public static MainMenuLabel1 mainMenu1;
    public static OptionsLabel1 optionMenu1;
    public static GameModeLabel1 gameMode1;
    public static ClassicModeLabel1 classicMode1;
    public static ItemModeLabel1 itemMode1;
    public static Board gamePanel;
    public static Board2 gamePanel2;
    public static Board3 gamePanel3;
    public static KeyControl1 keyControl1;
    public static ClassicScoreBoard1 classicScoreBoard1;
    public static ItemScoreBoard1 itemScoreBoard1;
    public static ScoreBoardMenu1 scoreBoardMenu1;


    /////////////////////////////설정값들 관리.
    public static JSONParser parser;
    public static JSONObject SettingObject;

    public static boolean isColorBlindnessMode; // 색맹 모드 상태 저장


    public static boolean isInputing = false; // 사용자가 키값을 바꾸려고 할 때인가?
    public static String currentChangingKey = "";
    public static String path;

    public static void main(String[] args) throws IOException {
        parser = new JSONParser();
        System.out.println();
        System.out.println(System.getProperty("user.dir"));
        path = System.getProperty("user.dir");

        try (FileReader reader = new FileReader("Tetris_game/src/Settings.json")) {
            // 파일로부터 JSON 객체를 읽어오기
            SettingObject = (JSONObject) parser.parse(reader);

            // 데이터 읽기
            System.out.println("Screen size : " + SettingObject.get("Screen"));
            System.out.println("K_UP : " + SettingObject.get("K_UP"));
            System.out.println("K_DOWN : " + SettingObject.get("K_DOWN"));
            System.out.println("K_LEFT : " + SettingObject.get("K_LEFT"));
            System.out.println("K_RIGHT : " + SettingObject.get("K_RIGHT"));
            System.out.println("color_blind : " + SettingObject.get("color_blind"));

            // "color_blind" 값 읽기
            isColorBlindnessMode = SettingObject.get("color_blind").toString().equals("On") ? true : false;
            Board.colorBlindMode = isColorBlindnessMode;
            if(Board.colorBlindMode) {Board.setColorBlindMode(true);}


        } catch (Exception e) {
            e.printStackTrace();
        }


        frame = new JFrame("Tetris Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH[0], SCREEN_HEIGHT[0]);
        frame.setLocationRelativeTo(null);

        // 메인 패널 초기화 및 레이아웃 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel();


        mainPanel.setLayout(cardLayout);

        // 메뉴와 옵션 패널 추가
        mainMenu1 = new MainMenuLabel1();
        mainMenu1.setName("MainMenu1");
        optionMenu1 = new OptionsLabel1();
        optionMenu1.setName("Options1");


        gameMode1 = new GameModeLabel1();
        gameMode1.setName("GameMode1");


        classicMode1 = new ClassicModeLabel1();
        classicMode1.setName("ClassicMode1");


        itemMode1 = new ItemModeLabel1();
        itemMode1.setName("ItemMode1");


        
        gamePanel = new Board();
        gamePanel.setSize(SCREEN_WIDTH[1], SCREEN_HEIGHT[1]);
        gamePanel.setVisible(true);
        gamePanel.setName("game");
        gamePanel2 = new Board2();
        gamePanel2.setSize(SCREEN_WIDTH[2], SCREEN_HEIGHT[2]);
        gamePanel2.setVisible(true);
        gamePanel2.setName("game2");
        gamePanel3 = new Board3();
        gamePanel3.setSize(SCREEN_WIDTH[3], SCREEN_HEIGHT[3]);
        gamePanel3.setVisible(true);
        gamePanel3.setName("game3");





        keyControl1 = new KeyControl1();
        keyControl1.setName("Control1");


        classicScoreBoard1 = new ClassicScoreBoard1();
        classicScoreBoard1.setName("NormalScoreBoard1");

        itemScoreBoard1 = new ItemScoreBoard1();
        itemScoreBoard1.setName("ItemScoreBoard1");

        scoreBoardMenu1 = new ScoreBoardMenu1();
        scoreBoardMenu1.setName("ScoreBoardMenu1");

        mainPanel.add(mainMenu1, "MainMenu1");
        mainPanel.add(optionMenu1, "Options1");


        mainPanel.add(gameMode1, "GameMode1");


        mainPanel.add(classicMode1, "ClassicMode1");


        mainPanel.add(itemMode1, "ItemMode1");


        mainPanel.add(gamePanel, "game");
        mainPanel.add(gamePanel2, "game2");
        mainPanel.add(gamePanel3, "game3");

        mainPanel.add(keyControl1, "Control1");


        mainPanel.add(classicScoreBoard1, "NormalScoreBoard1");


        mainPanel.add(itemScoreBoard1, "ItemScoreBoard1");

        mainPanel.add(scoreBoardMenu1, "ScoreBoardMenu1");

        cardLayout.show(mainPanel, "MainMenu1");

        frame.add(mainPanel);
        frame.setVisible(true);

        // 밑에는 EXIT버튼이 아니라 종료버튼을 눌러서 나갔을 때 저장되게
        try (FileWriter file = new FileWriter("Tetris_game/src/Settings.json")) {
            file.write(SettingObject.toJSONString());
            file.flush();
        } catch (Exception e) {
        e.printStackTrace();
        }


    }
}
