import javax.swing.*;
import java.awt.*;

/**
 * Panel for adding/importing new user sotires to the app
 *
 * @author Owen McManus
 * @version 1
 */
public class T4ANewStoryPanel extends JPanel {
    private final JTextArea storyTextArea;

    public T4ANewStoryPanel(T4ANewStoryNanny storiesNanny){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Create New Story", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        add(titleLabel, BorderLayout.NORTH);

        storyTextArea = new JTextArea("Put your stories text here. Each line contains new story.");
        JScrollPane scrollPane = new JScrollPane(storyTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        JButton saveAddNewButton = new JButton("Save & Add New");
        JButton saveCloseButton = new JButton("Save & Close");
        JButton importButton = new JButton("Import");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveAddNewButton);
        buttonPanel.add(saveCloseButton);
        buttonPanel.add(importButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveAddNewButton.addActionListener(e -> storiesNanny.saveAndAddNew(storyTextArea.getText(), this::reset));
        saveCloseButton.addActionListener(e -> storiesNanny.saveAndClose(storyTextArea.getText()));
        importButton.addActionListener(e -> storiesNanny.importStories(this::setStoryText));
        cancelButton.addActionListener(e -> storiesNanny.cancel());

    }

    public void reset(){
        storyTextArea.setText("Put your stories text here. Each line contains new story.");
    }

    public void setStoryText(String text){
        storyTextArea.setText(text);
    }
}
