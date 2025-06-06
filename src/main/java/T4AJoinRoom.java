import javax.swing.*;
import java.awt.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Allows a user to join an existing room by typing in the name
 *
 * @author Uriel Hernandez-Vega
 * @version 2
 */
public class T4AJoinRoom extends JFrame {
    Logger logger = LoggerFactory.getLogger(T4AJoinRoom.class);

    public T4AJoinRoom(){
        logger.info("JOIN ROOM WINDOW OPEN");
        setTitle("Join Room");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel join = new JLabel("Enter room name");
        join.setFont(new Font("Courier",Font.BOLD,30));
        join.setBounds(125,0,300,50);
        add(join);

        JLabel name = new JLabel("Name:");
        name.setFont(new Font("Courier", Font.BOLD, 30));
        name.setBounds(50, 100, 300, 50);
        add(name);

        JTextField enterName = new JTextField();
        enterName.setBounds(150,110,200,40);
        add(enterName);

        JButton joinButton = new JButton("Join");
        joinButton.setBounds(180, 175,100,50);
        add(joinButton);

        joinButton.addActionListener(T4RoomNanny.joinRoomAction(enterName, this));
        logger.info("JOIN ROOM WINDOW CLOSING");

    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(T4ABlackboard.getInstance().ROOM_WINDOW_WIDTH, T4ABlackboard.getInstance().ROOM_WINDOW_HEIGHT);
    }

}