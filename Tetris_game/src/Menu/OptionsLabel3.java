package Menu;

import blocks.Block;
import component.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class OptionsLabel3 extends JPanel implements KeyListener {
    private int currentIndex = 1; // 현재 선택된 메뉴 인덱스
    private final String cursorSymbol = "> "; // 현재 선택된 메뉴룰 따라갈 커서
    private final String nonSelected = "  "; // 커서가 있을 위치
    private final String[] labels = {"Main Menu", String.format("Screen : %d x %d",  Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]), "Controls", "Color Blindness Mode", "Reset"}; // 메인 메뉴에 있을 서브 메뉴들.
    java.util.List<JLabel> menuItems;
    public final JLabel optionLabel3;
    private boolean isColorBlindnessMode = false;

    private OptionsReset optionsReset;

    private JLabel keyMessage;
    private javax.swing.Timer messageTimer;
    public OptionsLabel3() {
        this.optionsReset = new OptionsReset();
        setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);
        setLayout(null);

        menuItems = new ArrayList<>();

        ImageIcon backgroundIcon = new ImageIcon(Main.class.getResource("../images/introBackground3.jpg"));
        optionLabel3 = new JLabel(new ImageIcon(backgroundIcon.getImage().getScaledInstance(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2], Image.SCALE_SMOOTH)));
        optionLabel3.setSize(Main.SCREEN_WIDTH[2], Main.SCREEN_HEIGHT[2]);

        JLabel title = new JLabel("Options");
        title.setFont(new Font("Arial", Font.BOLD, 40)); // 폰트 설정
        title.setForeground(Color.BLACK); // 텍스트 색상 설정
        title.setBounds(50, Main.SCREEN_HEIGHT[2] / 8, 400, 50); // 위치와 크기 설정
        optionLabel3.add(title);

        keyMessage = new JLabel(" ");
        keyMessage.setFont(new Font("Arial", Font.BOLD, Main.SCREEN_WIDTH[2] / 30)); // 폰트 설정
        keyMessage.setForeground(Color.BLACK); // 텍스트 색상 설정
        keyMessage.setBounds(Main.SCREEN_WIDTH[2]/2 - 300, Main.SCREEN_HEIGHT[0] / 2 - 100, 600, 100); // 위치와 크기 설정
        keyMessage.setHorizontalAlignment(JLabel.CENTER);
        keyMessage.setVerticalAlignment(JLabel.CENTER);
        add(keyMessage);

        messageTimer = new javax.swing.Timer(3000, e -> keyMessage.setVisible(false));
        messageTimer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정


        int Start_y = Main.SCREEN_HEIGHT[2] * 5 / 9;
        for (String i : labels) {
            addMenuItem(i, Start_y);
            Start_y += Main.SCREEN_HEIGHT[1] / 18;
        }

        updateMenuDisplay(); // 메뉴 디스플레이 업데이트
        add(optionLabel3);
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
            showTemporaryMessage(String.format("<html>Invalid Key Input. <br>Please press %s, %s, Enter</html>",
                    KeyEvent.getKeyText(((Number)Main.SettingObject.get("K_UP")).intValue()),
                    KeyEvent.getKeyText(((Number)Main.SettingObject.get("K_DOWN")).intValue())));
        updateMenuDisplay();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void activateMenuItem(int index) {
        switch (index) {
            case 0: // MainMenu
                System.out.println("Main Menu");
                switchToScreen(Main.mainMenu3);
                // 다시 메인메뉴로 이동함.
                break;
            case 1:
                System.out.println("Screen"); // 키설정을 바꾸는 로직 추가.
                Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
                Main.SettingObject.put("Screen", 1280);
                switchToScreen(Main.optionMenu1);
                break;
            case 2: // Exits
                System.out.println("Controls");
                switchToScreen(Main.keyControl3);
                break;
            case 3:
                isColorBlindnessMode = !isColorBlindnessMode; // 색맹 모드 토글
                Main.isColorBlindnessMode = isColorBlindnessMode; // 상태 저장
                System.out.println("Color Blindness Mode: " + (isColorBlindnessMode ? "Enabled" : "Disabled"));
                Board.setColorBlindMode(isColorBlindnessMode);
                break;
            case 4:
                System.out.println("Reset");
                optionsReset.resetOptions(); // 변경된 옵션 초기화
                optionsReset.applyInitialSettings(); // 초기 설정 적용
                Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
                switchToScreen(Main.optionMenu1);
                break;
            default:
                break;
        }
    }

    private void addMenuItem(String text, int y) {
        JLabel menuItem = new JLabel(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, Main.SCREEN_HEIGHT[2] / 24)); // 폰트 설정
        menuItem.setForeground(Color.BLACK); // 텍스트 색상 설정
        menuItem.setBounds((Main.SCREEN_HEIGHT[2] / 24) + Main.SCREEN_HEIGHT[2] / 72, y, 400, Main.SCREEN_HEIGHT[2] / 24); // 위치와 크기 설정
        menuItems.add(menuItem);
        optionLabel3.add(menuItem);
    }
    private void showTemporaryMessage(String message)
    { // 화면에 키입력 메시지를 띄움
        keyMessage.setText(message); // 메시지 표시
        keyMessage.setVisible(true); // 라벨을 보이게 설정
        messageTimer.restart(); // 타이머 시작 (이전 타이머가 실행 중이었다면 재시작)
    }
    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName()); // 화면 전환
        newScreen.setFocusable(true); // 새 화면이 포커스를 받을 수 있도록 설정
        newScreen.requestFocusInWindow(); // 새 화면에게 포커스 요청
    }
}
