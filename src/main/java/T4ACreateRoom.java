import javax.swing.*;
import java.awt.*;


/**
 * CreateRoom is the frame that will pop up when you want to create a room.
 * User can enter a room name and select a mode
 * Main interface will be opened
 * 
 * @author Uriel Hernandez-Vega
 * @version 1
 */
public class T4ACreateRoom extends JFrame{
    
    public T4ACreateRoom(){
        setLayout(null);
        setTitle("Create a Room");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        JLabel room = new JLabel("Create a Room");
        room.setFont(new Font("Courier",Font.BOLD,30));
        room.setBounds(125,0,300,50);
        add(room);

        JLabel name= new JLabel ("Room Name:");
        name.setFont(new Font("Courier", Font.BOLD, 30));
        name.setBounds(5,100, 300,50);
        add(name);

        JTextField enterName = new JTextField();
        enterName.setBounds(200,110,200,40);
        enterName.setFont(new Font("Courier", Font.PLAIN, 15));
        add(enterName);
    
        JLabel mode = new JLabel("Select Mode:");
        mode.setFont(new Font("Courier", Font.BOLD, 30));
        mode.setBounds(5, 200, 300, 50);
        add(mode);
        String[] modes = {
            "Standard", "Increasing", "Fibonacci"
        };
        JComboBox<String> modesMenu = new JComboBox<>(modes);
        modesMenu.setBounds(200,215,200,30);
        add(modesMenu);

        JButton create = new JButton("Create");
        create.setBounds(200,400, 100,50);
        add(create);

        create.addActionListener(RoomNanny.switchMainframe(enterName, modesMenu, this));
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(T4ABlackboard.getInstance().ROOM_WINDOW_WIDTH, T4ABlackboard.getInstance().ROOM_WINDOW_HEIGHT);
    }
}
