package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameModeLabel1 extends JPanel implements KeyListener {
    private int currentIndex = 0; // 현재 선택된 메뉴 인덱스
    private final String cursorSymbol = "> "; // 현재 선택된 메뉴를 따라갈 커서
    private final String nonSelected = "  "; // 커서가 없는 위치
    private final String[] labels = {"Classic Mode", "Item Mode", "Back"}; // 게임 모드 메뉴
    private List<JLabel> menuItems;
    private JLabel mainLabel;
    private JLabel keyMessage;
    private Timer messageTimer;


    public GameModeLabel1() {
        setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
        setLayout(null);

        menuItems = new ArrayList<>();

        // 배경 이미지 설정
        ImageIcon backgroundIcon = new ImageIcon(Main.path + "/Tetris_game/src/images/introBackground.jpg");
        mainLabel = new JLabel(new ImageIcon(backgroundIcon.getImage().getScaledInstance(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0], Image.SCALE_SMOOTH)));
        mainLabel.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);

        // 타이틀 레이블 설정
        JLabel title = new JLabel("Select Game Mode");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.BLACK);
        title.setBounds(50, Main.SCREEN_HEIGHT[0] / 8, 400, 50);
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
        else if(keyCode == ((Number)(Main.SettingObject.get("K_ENTER"))).intValue())
            activateMenuItem(currentIndex);
        else
            showTemporaryMessage("<html>Invalid Key Input. <br>Please press W, S, Enter</html>");
        updateMenuDisplay();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    private void activateMenuItem(int index) {
        switch (index) {
            case 0: // Classic Mode
                System.out.println("Classic Mode Selected");
                switchToScreen(Main.classicMode1);
                // Classic Mode에 대한 동작 추가
                break;
            case 1: // Item Mode
                System.out.println("Item Mode Selected");
                switchToScreen(Main.itemMode1);
                // Item Mode에 대한 동작 추가
                break;
            case 2: // Back
                System.out.println("Back to Main");
                switchToScreen(Main.mainMenu1);
                break;
        }
    }


    private void showTemporaryMessage(String message) {
        keyMessage.setText(message);
        keyMessage.setVisible(true);
        messageTimer.restart();
    }

    private void addMenuItem(String text, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, 30));
        menuItem.setForeground(Color.BLACK);
        menuItem.setBounds(50, y, 400, 30);
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
