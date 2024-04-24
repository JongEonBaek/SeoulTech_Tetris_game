package Menu;

import component.Board;

import javax.swing.*;
import java.awt.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static JFrame frame;
    public static JPanel mainPanel;
    public static CardLayout cardLayout;
    public static final int SCREEN_WIDTH[] = {1280, 1024, 960};
    public static final int SCREEN_HEIGHT[] = {720, 576, 540};
    public static MainMenuLabel1 mainMenu1;
    public static OptionsLabel1 optionMenu1;
    public static MainMenuLabel2 mainMenu2;
    public static OptionsLabel2 optionMenu2;
    public static MainMenuLabel3 mainMenu3;
    public static OptionsLabel3 optionMenu3;

    public static GameModeLabel1 gameMode1;
    public static GameModeLabel2 gameMode2;
    public static GameModeLabel3 gameMode3;

    public static ClassicModeLabel1 classicMode1;
    public static ClassicModeLabel2 classicMode2;
    public static ClassicModeLabel3 classicMode3;

    public static ItemModeLabel1 itemMode1;
    public static ItemModeLabel2 itemMode2;
    public static ItemModeLabel3 itemMode3;
    public static Board gamePanel;
    public static KeyControl1 keyControl1;
    public static KeyControl2 keyControl2;
    public static KeyControl3 keyControl3;
    public static ClassicScoreBoard1 classicScoreBoard1;
    public static ItemScoreBoard1 itemScoreBoard1;
    public static ClassicScoreBoard2 classicscoreBoard2;
    public static ClassicScoreBoard3 classicScoreBoard3;
    public static ScoreBoardMenu1 scoreBoardMenu1;


    /////////////////////////////설정값들 관리.
    public static JSONParser parser;
    public static JSONObject SettingObject;

    public static boolean isInputing = false; // 사용자가 키값을 바꾸려고 할 때인가?
    public static String currentChangingKey = "";
    public static String path;

    public static void main(String[] args) throws IOException {
        parser = new JSONParser();
        System.out.println();
        System.out.println(System.getProperty("user.dir"));
        path = System.getProperty("user.dir");

        try (FileReader reader = new FileReader("../Settings.json")) {
            // 파일로부터 JSON 객체를 읽어오기
            SettingObject = (JSONObject) parser.parse(reader);

            // 데이터 읽기
            System.out.println("Screen size : " + SettingObject.get("Screen"));
            System.out.println("K_UP : " + SettingObject.get("K_UP"));
            System.out.println("K_DOWN : " + SettingObject.get("K_DOWN"));
            System.out.println("K_LEFT : " + SettingObject.get("K_LEFT"));
            System.out.println("K_RIGHT : " + SettingObject.get("K_RIGHT"));
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
        mainMenu2 = new MainMenuLabel2();
        mainMenu2.setName("MainMenu2");
        optionMenu2 = new OptionsLabel2();
        optionMenu2.setName("Options2");
        mainMenu3 = new MainMenuLabel3();
        mainMenu3.setName("MainMenu3");
        optionMenu3 = new OptionsLabel3();
        optionMenu3.setName("Options3");

        gameMode1 = new GameModeLabel1();
        gameMode1.setName("GameMode1");
        gameMode2 = new GameModeLabel2();
        gameMode2.setName("GameMode2");
        gameMode3 = new GameModeLabel3();
        gameMode3.setName("GameMode3");

        classicMode1 = new ClassicModeLabel1();
        classicMode1.setName("ClassicMode1");
        classicMode2 = new ClassicModeLabel2();
        classicMode2.setName("ClassicMode2");
        classicMode3 = new ClassicModeLabel3();
        classicMode3.setName("ClassicMode3");

        itemMode1 = new ItemModeLabel1();
        itemMode1.setName("ItemMode1");
        itemMode2= new ItemModeLabel2();
        itemMode2.setName("ItemMode2");
        itemMode3 = new ItemModeLabel3();
        itemMode3.setName("ItemMode3");

        gamePanel = new Board();
        //gamePanel.mode = 2;
        gamePanel.setSize(SCREEN_WIDTH[0], SCREEN_HEIGHT[0]);
        gamePanel.setVisible(true);
        gamePanel.setName("game");

        keyControl1 = new KeyControl1();
        keyControl1.setName("Control1");
        keyControl2 = new KeyControl2();
        keyControl2.setName("Control2");
        keyControl3 = new KeyControl3();
        keyControl3.setName("Control3");

        classicScoreBoard1 = new ClassicScoreBoard1();
        classicScoreBoard1.setName("NormalScoreBoard1");
        classicscoreBoard2 = new ClassicScoreBoard2();
        classicscoreBoard2.setName("NormalScoreBoard2");
        classicScoreBoard3 = new ClassicScoreBoard3();
        classicScoreBoard3.setName("NormalScoreBoard3");

        itemScoreBoard1 = new ItemScoreBoard1();
        itemScoreBoard1.setName("ItemScoreBoard1");

        scoreBoardMenu1 = new ScoreBoardMenu1();
        scoreBoardMenu1.setName("ScoreBoardMenu1");

        mainPanel.add(mainMenu1, "MainMenu1");
        mainPanel.add(optionMenu1, "Options1");
        mainPanel.add(mainMenu2, "MainMenu2");
        mainPanel.add(optionMenu2, "Options2");
        mainPanel.add(mainMenu3, "MainMenu3");
        mainPanel.add(optionMenu3, "Options3");

        mainPanel.add(gameMode1, "GameMode1");
        mainPanel.add(gameMode2, "GameMode2");
        mainPanel.add(gameMode3, "GameMode3");

        mainPanel.add(classicMode1, "ClassicMode1");
        mainPanel.add(classicMode2, "ClassicMode2");
        mainPanel.add(classicMode3, "ClassicMode3");

        mainPanel.add(itemMode1, "ItemMode1");
        mainPanel.add(itemMode2, "ItemMode2");
        mainPanel.add(itemMode3, "ItemMode3");

        mainPanel.add(gamePanel, "game");

        mainPanel.add(keyControl1, "Control1");
        mainPanel.add(keyControl2, "Control2");
        mainPanel.add(keyControl3, "Control3");

        mainPanel.add(classicScoreBoard1, "NormalScoreBoard1");
        mainPanel.add(classicscoreBoard2, "NormalScoreBoard2");
        mainPanel.add(classicScoreBoard3, "NormalScoreBoard3");

        mainPanel.add(itemScoreBoard1, "ItemScoreBoard1");

        mainPanel.add(scoreBoardMenu1, "ScoreBoardMenu1");

        cardLayout.show(mainPanel, "MainMenu1");

        frame.add(mainPanel);
        frame.setVisible(true);

        // 밑에는 EXIT버튼이 아니라 종료버튼을 눌러서 나갔을 때 저장되게
        try (FileWriter file = new FileWriter("../Settings.json")) {
            file.write(SettingObject.toJSONString());
            file.flush();
        } catch (Exception e) {
        e.printStackTrace();
        }


    }
}
