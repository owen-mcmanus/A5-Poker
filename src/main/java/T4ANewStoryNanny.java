import org.json.JSONArray;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

/**
 * Contains handlers for managing the NewStoryPanel and adding new stories to the app
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4ANewStoryNanny {
    private final T4ANewStoryWindow window;

    public T4ANewStoryNanny(T4ANewStoryWindow window) {
        this.window = window;
    }

    public void saveAndAddNew(String text, Runnable reset) {
        addStories(text);
        reset.run();
    }

    public void saveAndClose(String text) {
        addStories(text);
        window.hideWindow();
    }

    private void addStories(String text){
        String[] stories = text.split("\n");
        for(String story : stories){
            if (!story.isEmpty())
                T4ABlackboard.getInstance().addNewStory(story);
        }
    }

    public void importStories(String username, String password, String id) {
        try {
            String authToken = T4ATaigaStoryFetcher.loginAndGetToken(username, password);
            int projectId = T4ATaigaStoryFetcher.getProjectId(authToken, id);
            JSONArray stories = T4ATaigaStoryFetcher.fetchUserStories(authToken, projectId);
            T4ABlackboard.getInstance().setStoryQueue(T4ATaigaStoryFetcher.parseIntoList(stories));
            window.hideWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        window.hideWindow();
    }
}
