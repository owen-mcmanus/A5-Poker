import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * RoomNanny provides ActionListeners for creating and joining rooms
 * Handles user and room names
 * Will switch to createroom or joinroom frames depending on login frame option
 * Handles action from createRoom and joinroom classes. Will switch to mainInterface
 *
 * @Author: Uriel Hernandez-Vega
 * @Version: 1.0
 */

public class T4RoomNanny {

    public static ActionListener createRoomFromLogin(JTextField name, JFrame frame) {
        return e -> {
            String uname = name.getText().trim();
            if (uname.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T4ABlackboard.getInstance().setUser(uname);

            SwingUtilities.invokeLater(() -> {
                JFrame createRoom = new T4ACreateRoom();
                createRoom.pack();
                createRoom.setLocationRelativeTo(null);
                createRoom.setVisible(true);
                frame.dispose();
            });
        };
    }

    public static ActionListener joinRoomFromLogin(JTextField name, JFrame frame) {
        return e -> {
            String uname = name.getText().trim();
            if (uname.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T4ABlackboard.getInstance().setUser(uname);

            SwingUtilities.invokeLater(() -> {
                JFrame joinRoom = new T4AJoinRoom();
                joinRoom.pack();
                joinRoom.setLocationRelativeTo(null);
                joinRoom.setVisible(true);
                frame.dispose();
            });
        };
    }

    public static ActionListener createRoomAction(JTextField name, JComboBox<String> modesMenu, JFrame frame) {
        return e -> {
            String roomName = name.getText().trim();
            if (roomName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a room name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T4ABlackboard.getInstance().addNewRoom(roomName, (String) modesMenu.getSelectedItem());
            T4ABlackboard.getInstance().setHost();

            SwingUtilities.invokeLater(() -> {
                T4MainInterface m = new T4MainInterface();
                m.pack();
                m.setVisible(true);
                frame.dispose();
            });
        };
    }

    public static ActionListener joinRoomAction(JTextField enterName, JFrame frame) {
        return e -> {
            String roomName = enterName.getText().trim();
            if (roomName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a room name.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            T4ABlackboard.getInstance().joinRoom(roomName);

            SwingUtilities.invokeLater(() -> {
                T4MainInterface m = new T4MainInterface();
                m.pack();
                m.setVisible(true);
                frame.dispose();
            });
        };
    }
}
