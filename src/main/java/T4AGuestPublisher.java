import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Objects;

/**
 * Publisher for guests to send their vote to the host
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4AGuestPublisher implements  Runnable, PropertyChangeListener {
    private boolean publishVote = false;
    private final Logger logger = LoggerFactory.getLogger(T4AGuestPublisher.class);

    @Override
    public void run() {
        try{
            while(true) {
                T4AConnection connection = T4AConnection.getInstance();
                if(publishVote && connection.client.isConnected()){
                    T4ABlackboard bb = T4ABlackboard.getInstance();
                    String message = bb.getSelected() + "," + bb.getUser();
                    MqttMessage voteMessage = new MqttMessage(message.getBytes());
                    voteMessage.setQos(2);
                    connection.client.publish(connection.TOPIC_SEND_VOTE + "/" + connection.CLIENT_ID, voteMessage);
                    publishVote = false;
                    logger.info("Vote sent - name: {}, value:{}", bb.getUser(), bb.getSelected());
                }
                Thread.sleep(300);
            }
        }catch (MqttException | InterruptedException e){
            logger.error("Could not publish vote: {}", Arrays.toString(e.getStackTrace()));
            JOptionPane.showMessageDialog(
                    null,
                    "Connection lost.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "submitVote")) publishVote = true;
    }
}
