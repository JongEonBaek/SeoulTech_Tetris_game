package Menu;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OptionsReset {

    private static final String INITIAL_SETTINGS_JSON =
            "{\"K_DOWN\":83,\"K_LEFT\":68,\"K_ENTER\":10,\"Screen\":\"1280\"," +
                    "\"K_ROTATE\":\"SPACE\",\"K_UP\":87,\"K_RIGHT\":65,\"color_blind\":Off}";

    // Reset 메서드 구현
    public static void resetOptions() {
        try (FileWriter file = new FileWriter(String.format(Main.path) + "/Tetris_game/src/Settings.json")) {
            file.write(INITIAL_SETTINGS_JSON);
            file.flush();
            System.out.println("Options reset successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 초기 설정을 읽어오는 메서드
    public static JSONObject readInitialSettings() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(String.format(Main.path) + "/Tetris_game/src/Settings.json")) {
            //System.out.println("왜 안될까");
            return (JSONObject) parser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 초기 설정 적용 메서드
    public void applyInitialSettings() {
        JSONObject initialSettings = readInitialSettings();
        if (initialSettings != null) {
            // 변경된 옵션 초기 설정으로 변경
            Main.SettingObject = initialSettings;
        }
    }
}
