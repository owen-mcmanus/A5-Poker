import javax.swing.*;

/**
 * Helper window that holds the NewStoryPanel
 *
 * @author Owen McManus
 * @version 1
 */
public class T4ANewStoryWindow extends JFrame {
    public T4ANewStoryWindow(){
        T4ANewStoryNanny newStoryNanny = new T4ANewStoryNanny(this);
        JPanel newStoryPanel = new T4ANewStoryPanel(newStoryNanny);
        add(newStoryPanel);
    }

    public void showWindow(){
        setSize(600,400);
        setTitle("New Story");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void hideWindow(){
        setVisible(false);
    }
}
