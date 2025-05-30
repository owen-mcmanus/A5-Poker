import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

/**
 * Contains handlers for managing the NewStoryPanel and adding new stories to the app
 *
 * @author Owen McManus
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

    public void importStories(Consumer<String> setText) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                setText.accept(content);
            } catch (IOException e) {
                System.out.println( "Error: " + e.getMessage());
            }
        }
    }

    public void cancel() {
        window.hideWindow();
    }
}
