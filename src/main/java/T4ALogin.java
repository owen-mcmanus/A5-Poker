import javax.swing.*;
import java.awt.*;

/**
 * First frame that opens, allows user to enter name
 * 
 * @author Uriel Hernandez-Vega
 * @version 1
 */
public class T4ALogin extends JFrame{
    public static void main(String[] args) {
        JFrame joinRoom = new T4ALogin();
        joinRoom.pack();
        joinRoom.setLocationRelativeTo(null);
        joinRoom.setVisible(true);
    }

    public T4ALogin(){
        setTitle("Login");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel join = new JLabel("PlanItPoker");
        join.setFont(new Font("Courier",Font.BOLD,30));
        join.setBounds(130,0,300,50);
        add(join);


        JLabel name = new JLabel("Name:");
        name.setFont(new Font("Courier", Font.BOLD, 30));
        name.setBounds(50, 100, 300, 50);
        add(name);

        JTextField enterName = new JTextField();
        enterName.setBounds(150,110,200,40);
        add(enterName);

        JButton createButton = new JButton("Create New");
        createButton.setBounds(180, 175,120,50);
        add(createButton);

        JButton joinButton = new JButton("Join");
        joinButton.setBounds(180, 260,100,50);
        add(joinButton);

        createButton.addActionListener(RoomNanny.createRoom(enterName, this));
        joinButton.addActionListener(RoomNanny.joinRoom(enterName, this));

    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(T4ABlackboard.getInstance().ROOM_WINDOW_WIDTH, T4ABlackboard.getInstance().ROOM_WINDOW_HEIGHT);
    }

}
    
