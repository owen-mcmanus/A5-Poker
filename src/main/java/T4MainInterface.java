import javax.swing.*;
import java.awt.*;

/**
 * This class is for the mainframe where frames will be shown together
 * Just setting main space with quick mockup of other 3 frames
 * 
 * @author Uriel Hernandez-vega
 * @version 1
 * 
 */
public class T4MainInterface extends JFrame {

    public T4MainInterface(){
      setLayout(new BorderLayout());
      setTitle("Main Interface");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setResizable(false);

      T4ACardsPanel cardsPanel = new T4ACardsPanel();
      add(cardsPanel, BorderLayout.CENTER);
      T4ABlackboard.getInstance().addPropertyChangeListener(cardsPanel);

      T4ASouthPanel t4ASouthPanel = new T4ASouthPanel();
      add(t4ASouthPanel, BorderLayout.SOUTH);
      T4AUtilitiesNanny utilitiesNanny = new T4AUtilitiesNanny(this);
      JPanel utilitiesPanel = new T4AUtilitiesPanel(utilitiesNanny);
      add(utilitiesPanel, BorderLayout.EAST);

      T4AConnection connection = new T4AConnection();
      if(T4ABlackboard.getInstance().isHost()){
        T4AHostPublisher publisher = new T4AHostPublisher(connection);
        T4ABlackboard.getInstance().addPropertyChangeListener(publisher);
        Thread t = new Thread(publisher);
        t.start();
      }else{
        T4AGuestPublisher publisher = new T4AGuestPublisher(connection);
        T4ABlackboard.getInstance().addPropertyChangeListener(publisher);
        Thread t = new Thread(publisher);
        t.start();
      }
      T4ASubscriber sub = new T4ASubscriber(connection, utilitiesNanny);
    }

  @Override
  public Dimension getPreferredSize(){
    return new Dimension(T4ABlackboard.getInstance().MAIN_WINDOW_WIDTH, T4ABlackboard.getInstance().MAIN_WINDOW_HEIGHT);
  }

}
