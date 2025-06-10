import javax.swing.*;
import java.awt.*;

/**
 * Panel for adding/importing new user sotires to the app
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4ANewStoryPanel extends JPanel {
    private final JTextArea storyTextArea;

    public T4ANewStoryPanel(T4ANewStoryNanny storiesNanny){
        setLayout(new GridLayout(8, 1));

//        JLabel titleLabel = new JLabel("Create New Story", SwingConstants.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
//        add(titleLabel, BorderLayout.NORTH);

        storyTextArea = new JTextArea("Put your stories text here. Each line contains new story.");
//        JScrollPane scrollPane = new JScrollPane(storyTextArea);
//        add(scrollPane, BorderLayout.CENTER);
//
//        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
//        JButton saveAddNewButton = new JButton("Save & Add New");
//        JButton saveCloseButton = new JButton("Save & Close");
//        JButton importButton = new JButton("Import");
//        JButton cancelButton = new JButton("Cancel");
//        buttonPanel.add(saveAddNewButton);
//        buttonPanel.add(saveCloseButton);
//        buttonPanel.add(importButton);
//        buttonPanel.add(cancelButton);
//        add(buttonPanel, BorderLayout.SOUTH);

//        saveAddNewButton.addActionListener(e -> storiesNanny.saveAndAddNew(storyTextArea.getText(), this::reset));
//        saveCloseButton.addActionListener(e -> storiesNanny.saveAndClose(storyTextArea.getText()));
//        cancelButton.addActionListener(e -> storiesNanny.cancel());

        JLabel titleLabel = new JLabel("Import Stories from Taiga", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Taiga Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        add(usernameLabel);
        JTextField usernameField = new JTextField();
        add(usernameField);

        JLabel passwordLabel = new JLabel("Taiga Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        add(passwordLabel);
        JTextField passwordField = new JTextField();
        add(passwordField);

        JLabel projectIdLabel = new JLabel("Taiga Project ID:");
        projectIdLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        add(projectIdLabel);
        JTextField projectIdField = new JTextField();
        add(projectIdField);

        JButton importButton = new JButton("Import");
        add(importButton);
        importButton.addActionListener(e -> storiesNanny.importStories(usernameField.getText(), passwordField.getText(), projectIdField.getText()));
    }

    public void reset(){
        storyTextArea.setText("Put your stories text here. Each line contains new story.");
    }

    public void setStoryText(String text){
        storyTextArea.setText(text);
    }
}
