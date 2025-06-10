import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Publisher for hosts to room data to the guests
 *
 * @author owen-mcmanus
 * @version 1
 */
public class T4AHostPublisher implements  Runnable, PropertyChangeListener {
    private final T4AConnection connection;
    private boolean publishReveal, publishEndReveal = false;

    public  T4AHostPublisher(T4AConnection connection){
        this.connection = connection;
    }

    @Override
    public void run() {
        try{
            while(true) {
                T4ABlackboard bb = T4ABlackboard.getInstance();
                JSONObject payload = new JSONObject()
                        .put("queue", bb.getStoryQueue())
                        .put("completed", bb.getCompletedStories())
                        .put("active", bb.getActiveStory())
                        .put("layout", bb.getCardLayout());
                MqttMessage message = new MqttMessage(payload.toString().getBytes());
                message.setQos(2);
                if (connection.client.isConnected()) {
                    connection.client.publish(connection.TOPIC_CURRENT_ROOM_DATA + "/" + connection.CLIENT_ID, message);
                }else{
                    JOptionPane.showMessageDialog(
                            null,
                            "Connection lost.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if(publishEndReveal && connection.client.isConnected()){
                    MqttMessage revealMessage = new MqttMessage("hideResults".getBytes());
                    revealMessage.setQos(2);
                    connection.client.publish(connection.TOPIC_HIDE_RESULTS + "/" + connection.CLIENT_ID, revealMessage);
                    publishEndReveal = false;
                }

                if(publishReveal && connection.client.isConnected()){
                    StringBuilder builder = new StringBuilder();
                    for(Integer i : bb.getResultsValues()){
                        builder.append(i);
                        builder.append(',');
                    }

                    String messageValue = String.join(";", String.join(",", bb.getResultsLabels()), builder.toString());
                    MqttMessage revealMessage = new MqttMessage(messageValue.getBytes());
                    revealMessage.setQos(2);
                    connection.client.publish(connection.TOPIC_SHOW_RESULTS + "/" + connection.CLIENT_ID, revealMessage);
                    publishReveal = false;
                }

                Thread.sleep(100);
            }
        }catch (MqttException | InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "reveal")) publishReveal = true;
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "endReveal")) publishEndReveal = true;
    }
}
