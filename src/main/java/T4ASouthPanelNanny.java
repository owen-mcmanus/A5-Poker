import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Nanny class to handle the logic for T4ASouthPanel.
 * Processes updates to stories and logs events.
 */
public class T4ASouthPanelNanny {
    private static final Logger logger = LoggerFactory.getLogger(T4ASouthPanelNanny.class);

    private final JTextArea activeArea;
    private final JTextArea completedArea;
    private final JTextArea upcomingArea;

    public T4ASouthPanelNanny(JTextArea activeArea, JTextArea completedArea, JTextArea upcomingArea) {
        this.activeArea = activeArea;
        this.completedArea = completedArea;
        this.upcomingArea = upcomingArea;
    }

    public void handlePropertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        try {
            switch (prop) {
                case "newStory" -> {
                    @SuppressWarnings("unchecked")
                    Queue<String> storiesQueue = (Queue<String>) evt.getNewValue();
                    StringBuilder builder = new StringBuilder();
                    storiesQueue.forEach(story -> builder.append(story).append("\n"));
                    upcomingArea.setText(builder.toString());
//                    logger.info("Updated upcoming stories with {} items.", storiesQueue.size());
                }

                case "completedStory" -> {
                    @SuppressWarnings("unchecked")
                    List<String[]> storiesList = (List<String[]>) evt.getNewValue();
                    StringBuilder builder = new StringBuilder();
                    for (String[] story : storiesList) {
                        builder.append(story[0]).append(": ").append(story[1]).append("\n");
                    }
                    completedArea.setText(builder.toString());
//                    logger.info("Completed stories updated with {} entries.", storiesList.size());
                }

                case "activeStory" -> {
                    String activeStory = (String) evt.getNewValue();
                    activeArea.setText(activeStory);
//                    logger.info("Active story set to: {}", activeStory);
                }

                default -> {
                    logger.debug("Unhandled property change: {}", prop);
                }
            }
        } catch (ClassCastException e) {
            logger.error("Property '{}' value could not be cast correctly", prop, e);
        } catch (Exception e) {
            logger.error("Unexpected error processing property change '{}'", prop, e);
        }
    }
}
