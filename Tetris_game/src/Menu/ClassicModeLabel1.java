package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import component.Board;


public class ClassicModeLabel1  extends JPanel implements KeyListener {
    private int currentIndex = 0; // 현재 선택된 메뉴 인덱스
    private final String cursorSymbol = "> "; // 현재 선택된 메뉴를 따라갈 커서
    private final String nonSelected = "  "; // 커서가 없는 위치
    private final String[] labels = {"Easy", "Normal", "Hard", "Back"}; // 게임 모드 메뉴
    private List<JLabel> menuItems;
    private JLabel mainLabel;
    private JLabel keyMessage;
    private Timer messageTimer;

    public Board board;



    public ClassicModeLabel1() {
        setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
        setLayout(null);

        menuItems = new ArrayList<>();

        // 배경 이미지 설정
        ImageIcon backgroundIcon = new ImageIcon(Main.class.getResource("../images/introBackground.jpg"));
        mainLabel = new JLabel(new ImageIcon(backgroundIcon.getImage().getScaledInstance(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0], Image.SCALE_SMOOTH)));
        mainLabel.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);

        // 타이틀 레이블 설정
        JLabel title = new JLabel("Select Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.BLACK);
        title.setBounds(50, Main.SCREEN_HEIGHT[0] / 8, 500, 50);
        mainLabel.add(title);

        // 메뉴 아이템 추가
        int startY = Main.SCREEN_HEIGHT[0] * 5 / 9;
        for (String label : labels) {
            addMenuItem(label, startY);
            startY += 50;
        }

        // 임시 메시지 레이블 설정
        keyMessage = new JLabel(" ");
        keyMessage.setFont(new Font("Arial", Font.BOLD, 40));
        keyMessage.setForeground(Color.BLACK);
        keyMessage.setBounds(Main.SCREEN_WIDTH[0] / 2 - 300, Main.SCREEN_HEIGHT[0] / 2 - 100, 600, 100);
        add(keyMessage);

        // 메시지를 일정 시간 동안 표시하기 위한 타이머 설정
        messageTimer = new Timer(3000, e -> keyMessage.setVisible(false));
        messageTimer.setRepeats(false);

        // 메뉴 디스플레이 업데이트
        updateMenuDisplay();

        add(mainLabel);

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    private void updateMenuDisplay() {
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == currentIndex) {
                menuItems.get(i).setText(cursorSymbol + labels[i]);
            } else {
                menuItems.get(i).setText(nonSelected + labels[i]);
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == ((Number)(Main.SettingObject.get("K_UP"))).intValue())
            currentIndex = (currentIndex - 1 + menuItems.size()) % menuItems.size();
        else if(keyCode == ((Number)(Main.SettingObject.get("K_DOWN"))).intValue())
            currentIndex = (currentIndex + 1) % menuItems.size();
        else if(keyCode == ((Number)(Main.SettingObject.get("K_ENTER"))).intValue()) {
            activateMenuItem(currentIndex);

        }
        else
            showTemporaryMessage("<html>Invalid Key Input. <br>Please press W, S, Enter</html>");
        updateMenuDisplay();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void activateMenuItem(int index) {
        switch (index) {
            case 0: // Easy
                System.out.println("Easy Selected1");
                if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[1])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[1], Main.SCREEN_HEIGHT[1]);
                    Main.gamePanel.item = 0;
                    Main.gamePanel.mode = 0;
                    Main.gamePanel.GameInit();
                    switchToScreen(Main.gamePanel);
                    Main.gamePanel.timer.start();
                    Main.gamePanel.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[2])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);
                    Main.gamePanel2.item = 0;
                    Main.gamePanel2.mode = 0;
                    Main.gamePanel2.GameInit();
                    switchToScreen(Main.gamePanel2);
                    Main.gamePanel2.timer.start();
                    Main.gamePanel2.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[3])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[3], Main.SCREEN_HEIGHT[3]);
                    Main.gamePanel3.item = 0;
                    Main.gamePanel3.mode = 0;
                    Main.gamePanel3.GameInit();
                    switchToScreen(Main.gamePanel3);
                    Main.gamePanel3.timer.start();
                    Main.gamePanel3.setVisible(true);
                }
                else
                    System.out.println("EORRORORORORORORORROROROROR!");


                // 여기서 다음 화면으로 넘어가는 로직을 구현
                break;
            case 1: // Normal
                if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[1])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[1], Main.SCREEN_HEIGHT[1]);
                    Main.gamePanel.item = 0;
                    Main.gamePanel.mode = 1;
                    Main.gamePanel.GameInit();
                    switchToScreen(Main.gamePanel);
                    Main.gamePanel.timer.start();
                    Main.gamePanel.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[2])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);
                    Main.gamePanel2.item = 0;
                    Main.gamePanel2.mode = 1;
                    Main.gamePanel2.GameInit();
                    switchToScreen(Main.gamePanel2);
                    Main.gamePanel2.timer.start();
                    Main.gamePanel2.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[3])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[3], Main.SCREEN_HEIGHT[3]);
                    Main.gamePanel3.item = 0;
                    Main.gamePanel3.mode = 1;
                    Main.gamePanel3.GameInit();
                    switchToScreen(Main.gamePanel3);
                    Main.gamePanel3.timer.start();
                    Main.gamePanel3.setVisible(true);
                }
                else
                    System.out.println("EORRORORORORORORORROROROROR!");
                break;
            case 2: // Hard
                System.out.println("Hard Selected");
                if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[1])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[1], Main.SCREEN_HEIGHT[1]);
                    Main.gamePanel.item = 0;
                    Main.gamePanel.mode = 2;
                    Main.gamePanel.GameInit();
                    switchToScreen(Main.gamePanel);
                    Main.gamePanel.timer.start();
                    Main.gamePanel.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[2])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);
                    Main.gamePanel2.item = 0;
                    Main.gamePanel2.mode = 2;
                    Main.gamePanel2.GameInit();
                    switchToScreen(Main.gamePanel2);
                    Main.gamePanel2.timer.start();
                    Main.gamePanel2.setVisible(true);
                }
                else if(((Number)Main.SettingObject.get("Screen")).intValue() == Main.SCREEN_WIDTH[3])
                {
                    Main.frame.setSize(Main.SCREEN_WIDTH[3], Main.SCREEN_HEIGHT[3]);
                    Main.gamePanel3.item = 0;
                    Main.gamePanel3.mode = 2;
                    Main.gamePanel3.GameInit();
                    switchToScreen(Main.gamePanel3);
                    Main.gamePanel3.timer.start();
                    Main.gamePanel3.setVisible(true);
                }
                else
                    System.out.println("EORRORORORORORORORROROROROR!");
                break;
            case 3: // Back(이전화면)
                System.out.println("Back to GameMode");
                switchToScreen(Main.gameMode1);
                break;
        }
    }
    private void showTemporaryMessage(String message)
    { // 화면에 키입력 메시지를 띄움
        keyMessage.setText(message); // 메시지 표시
        keyMessage.setVisible(true); // 라벨을 보이게 설정
        messageTimer.restart(); // 타이머 시작 (이전 타이머가 실행 중이었다면 재시작)
    }

    private void addMenuItem(String text, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, 30)); // 폰트 설정
        menuItem.setForeground(Color.BLACK); // 텍스트 색상 설정
        menuItem.setBounds(50, y, 400, 30); // 위치와 크기 설정
        menuItems.add(menuItem);
        mainLabel.add(menuItem);
    }


    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName()); // 화면 전환
        System.out.println(newScreen.getName());
        newScreen.setFocusable(true); // 새 화면이 포커스를 받을 수 있도록 설정
        newScreen.requestFocusInWindow(); // 새 화면에게 포커스 요청
    }



}
