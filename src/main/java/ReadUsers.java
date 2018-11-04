import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class ReadUsers {

    private String path;
    ReadUsers(String path) {        this.path = path;    }

    Map read_users() {
        Map<String, String> users = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(this.path), "UTF-8"))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                users.put(sub.substring(0, sub.indexOf(' ')),sub.substring(sub.indexOf(' ') + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
