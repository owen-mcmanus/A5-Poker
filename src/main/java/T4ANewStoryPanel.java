import javax.swing.*;
import java.awt.*;

/**
 * Panel for adding/importing new user sotires to the app
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4ANewStoryPanel extends JPanel {
    public T4ANewStoryPanel(T4ANewStoryNanny storiesNanny){
        setLayout(new GridLayout(8, 1));

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
        JPasswordField passwordField = new JPasswordField();
        add(passwordField);

        JLabel projectIdLabel = new JLabel("Taiga Project ID:");
        projectIdLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        add(projectIdLabel);
        JTextField projectIdField = new JTextField();
        add(projectIdField);

        JButton importButton = new JButton("Import");
        add(importButton);
        importButton.addActionListener(e -> storiesNanny.importStories(usernameField.getText(), new String(passwordField.getPassword()), projectIdField.getText()));
    }
}
