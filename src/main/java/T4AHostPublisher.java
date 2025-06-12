import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private boolean publishReveal, publishEndReveal = false;
    private final Logger logger = LoggerFactory.getLogger(T4AHostPublisher.class);


    @Override
    public void run() {
        try{
            T4AConnection connection = T4AConnection.getInstance();
            while(true) {
                sendRoomMessage();

                if(publishEndReveal && connection.client.isConnected()){
                    sendEndRevealMessage();
                    publishEndReveal = false;
                }

                if(publishReveal && connection.client.isConnected()){
                    sendRevealMessage();
                    publishReveal = false;
                }

                Thread.sleep(100);
            }
        }catch (MqttException | InterruptedException e){
            logger.error("Publisher Error: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    private void sendRoomMessage() throws MqttException{
        T4ABlackboard bb = T4ABlackboard.getInstance();
        T4AConnection connection = T4AConnection.getInstance();
        JSONObject payload = new JSONObject()
                .put("queue", bb.getStoryQueue())
                .put("completed", bb.getCompletedStories())
                .put("active", bb.getActiveStory())
                .put("votes", bb.getVoteHistory())
                .put("layout", bb.getCardLayout());
        MqttMessage message = new MqttMessage(payload.toString().getBytes());
        message.setQos(2);
        if (connection.client.isConnected()) {
            connection.client.publish(connection.TOPIC_CURRENT_ROOM_DATA + "/" + connection.CLIENT_ID, message);
        }else{
            displayConnectionLostError();
        }
    }

    private void sendRevealMessage() throws MqttException{
        T4ABlackboard bb = T4ABlackboard.getInstance();
        T4AConnection connection = T4AConnection.getInstance();
        StringBuilder builder = new StringBuilder();
        for(Integer i : bb.getResultsValues()){
            builder.append(i);
            builder.append(',');
        }

        String messageValue = String.join(";", String.join(",", bb.getResultsLabels()), builder.toString());
        MqttMessage revealMessage = new MqttMessage(messageValue.getBytes());
        revealMessage.setQos(2);
        if (connection.client.isConnected()) {
            connection.client.publish(connection.TOPIC_SHOW_RESULTS + "/" + connection.CLIENT_ID, revealMessage);
            logger.info("Publish show results");
        }else{
            displayConnectionLostError();
        }
    }

    private void sendEndRevealMessage() throws MqttException{
        T4AConnection connection = T4AConnection.getInstance();
        MqttMessage revealMessage = new MqttMessage("hideResults".getBytes());
        revealMessage.setQos(2);
        if (connection.client.isConnected()) {
            connection.client.publish(connection.TOPIC_HIDE_RESULTS + "/" + connection.CLIENT_ID, revealMessage);
            logger.info("Publish hide results");
        }else{
            displayConnectionLostError();
        }
    }

    private void displayConnectionLostError(){
        logger.error("Publisher lost connection");
        JOptionPane.showMessageDialog(
                null,
                "Connection lost.",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "reveal")) publishReveal = true;
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "endReveal")) publishEndReveal = true;
    }
}
