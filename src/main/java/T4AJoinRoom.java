import javax.swing.*;
import java.awt.*;

/*
 * @author owen-mcmanus
 */
public class T4AJoinRoom extends JFrame {

    public T4AJoinRoom(){
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

        joinButton.addActionListener(e->{
            String roomName = enterName.getText();
            if(!roomName.isEmpty()){
                T4ABlackboard.getInstance().joinRoom(roomName);

                T4MainInterface m = new T4MainInterface();
                m.pack();
                m.setVisible(true);
                dispose();
            }
        });
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(T4ABlackboard.getInstance().ROOM_WINDOW_WIDTH, T4ABlackboard.getInstance().ROOM_WINDOW_HEIGHT);
    }

}