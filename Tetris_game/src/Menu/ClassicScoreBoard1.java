package Menu;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClassicScoreBoard1 extends JPanel implements KeyListener {
    private JLabel keyMessage;
    private JPanel scoreTable;
    private Timer messageTimer;
    private JSONArray sortedScoreArray;
    private JSONParser scoreParser;

    public ClassicScoreBoard1() {
        this.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
        this.setLayout(new BorderLayout());
        JLabel title = new JLabel(String.format("\n") + "Classic Score Board");// "<html>Invalid Key Input. <br>If you want to exit, Press Enter</html>"
        title.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[0] / 40));
        title.setForeground(Color.BLACK);
        title.setBounds(Main.SCREEN_WIDTH[0] / 2 - 200, Main.SCREEN_HEIGHT[0] / 20, 400, 50);
        title.setHorizontalAlignment(0);
        this.add(title, BorderLayout.NORTH);

        this.scoreParser = new JSONParser();
        this.sortedScoreArray = new JSONArray();


        try (FileReader reader = new FileReader(String.format(Main.path) + "/Tetris_game/src/ClassicScoreData.json")) {
            JSONArray scoreArray = (JSONArray) scoreParser.parse(reader);
            List<JSONObject> scoreList = new ArrayList<>();
            for (Object item : scoreArray) {
                scoreList.add((JSONObject) item);
            }

            scoreList.sort((a, b) -> {
                long valA = (long) a.get("scores");
                long valB = (long) b.get("scores");
                return Long.compare(valB, valA);
            });

            for (int i = 0; i < 10; i++) {
                if(i >= scoreList.size())
                {
                    JSONObject temp = new JSONObject();
                    temp.put("mode", 0);
                    temp.put("scores", 0);
                    temp.put("item", 0);
                    temp.put("name", "Default Value");
                    sortedScoreArray.add(temp);
                }
                else
                    sortedScoreArray.add(scoreList.get(i));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        scoreTable = new JPanel(new GridLayout(sortedScoreArray.size() + 1, 5, 50, 20));
        scoreTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scoreTable, BorderLayout.CENTER);
        // 컬럼 헤더 추가
        String[] columnNames = {"Rank", "Name", "Mode", "Difficulty" ,"Score"};
        for (String columnName : columnNames) {
            JLabel headerLabel = new JLabel(columnName, SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            scoreTable.add(headerLabel);
        }

        // 데이터 행 추가
        for (int i = 0; i < sortedScoreArray.size(); i++) {
            JSONObject score = (JSONObject) sortedScoreArray.get(i);
            scoreTable.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            scoreTable.add(new JLabel((String) score.get("name"), SwingConstants.CENTER));
            scoreTable.add(new JLabel(String.valueOf(score.get("item")).equals("1") ? "Item" : "Normal", SwingConstants.CENTER));
            scoreTable.add(new JLabel(String.valueOf(score.get("mode")).equals("0") ? "Easy" : String.valueOf(score.get("mode")).equals("1") ? "Normal" : "Hard" , SwingConstants.CENTER));
            scoreTable.add(new JLabel(String.valueOf(score.get("scores")), SwingConstants.CENTER));
        }

        this.keyMessage = new JLabel(" ");
        this.keyMessage.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[0] / 30));
        this.keyMessage.setForeground(Color.BLACK);
        this.add(this.keyMessage, BorderLayout.CENTER);
        this.messageTimer = new Timer(700, (e) -> {
            this.keyMessage.setVisible(false);
        });
        this.messageTimer.setRepeats(false);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setVisible(true);
    }

    public void update()
    {
        this.removeAll();

        this.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
        this.setLayout(new BorderLayout());
        JLabel title = new JLabel("Normal Score Board");
        title.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[0] / 40));
        title.setForeground(Color.BLACK);
        title.setBounds(Main.SCREEN_WIDTH[0] / 2 - 200, Main.SCREEN_HEIGHT[0] / 20, 400, 50);
        title.setHorizontalAlignment(0);
        this.add(title, BorderLayout.NORTH);

        this.scoreParser = new JSONParser();
        this.sortedScoreArray = new JSONArray();


        try (FileReader reader = new FileReader(String.format(Main.path) + "/Tetris_game/src/ClassicScoreData.json")) {
            JSONArray scoreArray = (JSONArray) scoreParser.parse(reader);
            List<JSONObject> scoreList = new ArrayList<>();
            for (Object item : scoreArray) {
                scoreList.add((JSONObject) item);
            }

            scoreList.sort((a, b) -> {
                long valA = (long) a.get("scores");
                long valB = (long) b.get("scores");
                return Long.compare(valB, valA);
            });

            for (int i = 0; i < 10; i++) {
                if(i >= scoreList.size())
                {
                    JSONObject temp = new JSONObject();
                    temp.put("mode", 0);
                    temp.put("scores", 0);
                    temp.put("item", 0);
                    temp.put("name", "Default Value");
                    temp.put("recent", 0);
                    sortedScoreArray.add(temp);
                }
                else
                    sortedScoreArray.add(scoreList.get(i));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        scoreTable = new JPanel(new GridLayout(sortedScoreArray.size() + 1, 5, 50, 20));
        scoreTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(scoreTable, BorderLayout.SOUTH);
        // 컬럼 헤더 추가
        String[] columnNames = {"Rank", "Name", "Mode", "Difficulty" ,"Score"};
        for (String columnName : columnNames) {
            JLabel headerLabel = new JLabel(columnName, SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            scoreTable.add(headerLabel);
        }

        // 데이터 행 추가
        for (int i = 0; i < sortedScoreArray.size(); i++) {
            JSONObject score = (JSONObject) sortedScoreArray.get(i);
            if(String.valueOf(score.get("recent")).equals("0")) {
                scoreTable.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
                scoreTable.add(new JLabel((String) score.get("name"), SwingConstants.CENTER));
                scoreTable.add(new JLabel(String.valueOf(score.get("item")).equals("1") ? "Item" : "Normal", SwingConstants.CENTER));
                scoreTable.add(new JLabel(String.valueOf(score.get("mode")).equals("0") ? "Easy" : String.valueOf(score.get("mode")).equals("1") ? "Normal" : "Hard", SwingConstants.CENTER));
                scoreTable.add(new JLabel(String.valueOf(score.get("scores")), SwingConstants.CENTER));
            }
            else
            {
                scoreTable.add(new JLabel("*" + String.valueOf(i + 1) + "*", SwingConstants.CENTER));
                scoreTable.add(new JLabel("*" + ((String) score.get("name")) +"*", SwingConstants.CENTER));
                scoreTable.add(new JLabel("*" + (String.valueOf(score.get("item")).equals("1") ? "Item" : "Normal") + "*", SwingConstants.CENTER));
                scoreTable.add(new JLabel("*" + (String.valueOf(score.get("mode")).equals("0") ? "Easy" : String.valueOf(score.get("mode")).equals("1") ? "Normal" : "Hard") +"*", SwingConstants.CENTER));
                scoreTable.add(new JLabel("*" + (String.valueOf(score.get("scores"))) +"*", SwingConstants.CENTER));
            }
        }

        this.keyMessage = new JLabel(" ");
        this.keyMessage.setFont(new Font("Arial", 1, Main.SCREEN_WIDTH[0] / 30));
        this.keyMessage.setForeground(Color.BLACK);
        this.add(this.keyMessage, BorderLayout.CENTER);
        this.messageTimer = new Timer(700, (e) -> {
            this.keyMessage.setVisible(false);
        });
        this.messageTimer.setRepeats(false);
    }
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == ((Number)(Main.SettingObject.get("K_ENTER"))).intValue()) {
            this.switchToScreen(Main.scoreBoardMenu1);
        } else {
            this.showTemporaryMessage("<html>Invalid Key Input. <br>If you want to exit, Press Enter</html>");
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void switchToScreen(JPanel newScreen) {
        Main.cardLayout.show(Main.mainPanel, newScreen.getName());
        System.out.println(newScreen.getName());
        newScreen.setFocusable(true);
        newScreen.requestFocusInWindow();
    }

    private void showTemporaryMessage(String message) {
        this.keyMessage.setText(message);
        this.keyMessage.setVisible(true);
        this.messageTimer.restart();
    }
}
