import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import java.util.*;

/**
 * Receives game data from MQTT server
 *
 * @version 1
 * @author  owen-mcmanus
 */
public class T4ASubscriber implements MqttCallback {
    T4AConnection connection;
    T4AUtilitiesNanny nanny;
    public T4ASubscriber(T4AConnection connection, T4AUtilitiesNanny nanny){
        this.connection = connection;
        this.nanny = nanny;

        try {
            connection.client.setCallback(this);
            connection.client.subscribe(connection.TOPIC_CURRENT_ROOM_DATA + "/#");
            connection.client.subscribe(connection.TOPIC_SHOW_RESULTS + "/#");
            connection.client.subscribe(connection.TOPIC_HIDE_RESULTS + "/#");
            connection.client.subscribe(connection.TOPIC_SEND_VOTE + "/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        JOptionPane.showMessageDialog(
                null,
                "Connection lost.",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        if (topic.endsWith("/" + connection.CLIENT_ID)) {
            return;
        }

        if(topic.startsWith(connection.TOPIC_CURRENT_ROOM_DATA)){
            String[] message  = new String(mqttMessage.getPayload()).split(";");
            handleMessageQueue(message[0]);
            handleStoryCompleted(message[1]);
            handleActiveStory(message[2]);
            handleLayout(message[3]);
        }

        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        if(blackboard.isHost()){
            if(topic.startsWith(connection.TOPIC_SEND_VOTE)){
                handleSendVote(new String(mqttMessage.getPayload()));
            }
        }else{
            if(topic.startsWith(connection.TOPIC_SHOW_RESULTS)){
                handleShowResults(new String(mqttMessage.getPayload()));
            }
            if(topic.startsWith(connection.TOPIC_HIDE_RESULTS)){
                nanny.switchToCardsGUI();
            }
        }
    }

    private void handleMessageQueue(String rawData){
        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        String[] data = rawData.split(",");
        LinkedList<String> storyQueue = new LinkedList<>();
        Collections.addAll(storyQueue, data);
        blackboard.setStoryQueue(storyQueue);
    }

    private void handleStoryCompleted(String rawData){
        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        String[] data = rawData.split(",");
        LinkedList<String[]> completedStories = new LinkedList<>();
        if(data.length > 1) {
            for (int i = 0; i < data.length; i += 2) {
                completedStories.add(new String[]{data[i], data[i + 1]});
            }
        }
        blackboard.setCompletedStories(completedStories);
    }

    private void handleActiveStory(String rawData){
        T4ABlackboard.getInstance().setActiveStory(rawData);
    }

    private void handleLayout(String rawData){
        T4ABlackboard.getInstance().setCardLayout(rawData);
    }

    private void handleSendVote(String rawData){
        String[] data = rawData.split(",");
        T4ABlackboard.getInstance().addVote(data[0], data[1], false);
    }

    private void handleShowResults(String rawData){
        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        String[] data = rawData.split(";");
        List<String> labels = List.of(data[0].split(","));
        blackboard.setResultsLabels(labels.toArray(new String[0]));

        Integer[] values = Arrays.stream(data[1].split(","))
                .map(String::trim)
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
        blackboard.setResultsValues(values);

        nanny.switchToResultsGUI();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
