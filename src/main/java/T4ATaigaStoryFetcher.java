import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

/**
 * Connects to Taiga to download stories
 *
 * @version 1
 * @author  owen-mcmanus
 */
public class T4ATaigaStoryFetcher {
    private final static Logger logger = LoggerFactory.getLogger(T4ATaigaStoryFetcher.class);

    public static String loginAndGetToken(String username, String password) throws Exception {
        URL url = new URL("https://api.taiga.io/api/v1/auth");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        String jsonInput = String.format("{\"type\": \"normal\", \"username\": \"%s\", \"password\": \"%s\"}", username, password);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()
        ));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        JSONObject json = new JSONObject(response.toString());
        if (responseCode != 200) {
            String errorMessage = json.optString("_error_message", "Unknown error");
            logger.error(errorMessage);
        }
        String authToken = json.getString("auth_token");
        logger.info("Received auth token");
        return authToken;
    }

    public static int getProjectId(String token, String projectSlug) throws Exception {
        URL url = new URL("https://api.taiga.io/api/v1/projects/by_slug?slug=" + projectSlug);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + token);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONObject json = new JSONObject(response.toString());
        logger.info("Received ID: {} for slug: {}", json.getInt("id"), projectSlug);
        return json.getInt("id");
    }

    public static JSONArray fetchUserStories(String token, int projectId) throws Exception {
        URL url = new URL("https://api.taiga.io/api/v1/userstories?project=" + projectId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + token);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONArray allStories = new JSONArray(response.toString());
        JSONArray backlogStories = new JSONArray();
        for (int i = 0; i < allStories.length(); i++) {
            JSONObject story = allStories.getJSONObject(i);
            if (story.isNull("milestone")) {
                backlogStories.put(story);
            }
        }
        return backlogStories;
    }

    public static LinkedList<String> parseIntoList(JSONArray stories) {
        LinkedList<String> output = new LinkedList<>();
        for (int i = 0; i < stories.length(); i++) {
            output.add(stories.getJSONObject(i).optString("subject"));
        }
        return  output;
    }
}