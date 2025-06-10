import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Contains handlers for managing the NewStoryPanel and adding new stories to the app
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4ANewStoryNanny {
    private final T4ANewStoryWindow window;
    private final Logger logger = LoggerFactory.getLogger(T4ANewStoryNanny.class);

    public T4ANewStoryNanny(T4ANewStoryWindow window) {
        this.window = window;
    }

    public void importStories(String username, String password, String id) {
        try {
            logger.info("Attempting to get stories with uname:{} and id: {}", username, id);
            String authToken = T4ATaigaStoryFetcher.loginAndGetToken(username, password);
            int projectId = T4ATaigaStoryFetcher.getProjectId(authToken, id);
            JSONArray stories = T4ATaigaStoryFetcher.fetchUserStories(authToken, projectId);
            T4ABlackboard.getInstance().setStoryQueue(T4ATaigaStoryFetcher.parseIntoList(stories));
            window.hideWindow();
            logger.info("Received {} stories from Taiga", stories.length());
        } catch (Exception e) {
            logger.error("Error getting stories from Taiga: {}", Arrays.toString(e.getStackTrace()));
        }
    }
}
