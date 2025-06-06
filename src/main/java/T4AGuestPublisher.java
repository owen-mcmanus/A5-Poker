import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Publisher for guests to send their vote to the host
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4AGuestPublisher implements  Runnable, PropertyChangeListener {
    private final T4AConnection connection;
    private boolean publishVote = false;

    public  T4AGuestPublisher(T4AConnection connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        try{
            while(true) {
                if(publishVote && connection.client.isConnected()){
                    T4ABlackboard bb = T4ABlackboard.getInstance();
                    String message = bb.getSelected() + "," + bb.getUser();
                    MqttMessage voteMessage = new MqttMessage(message.getBytes());
                    voteMessage.setQos(2);
                    connection.client.publish(connection.TOPIC_SEND_VOTE + "/" + connection.CLIENT_ID, voteMessage);
                    publishVote = false;
                }
                Thread.sleep(300);
            }
        }catch (MqttException | InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "submitVote")) publishVote = true;
    }
}
