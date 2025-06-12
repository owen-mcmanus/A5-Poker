import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.*;

/**
 * Receives game data from MQTT server
 *
 * @version 1
 * @author  owen-mcmanus
 */
public class T4ASubscriber implements MqttCallback {
    private final T4AUtilitiesNanny nanny;
    private final Logger logger = LoggerFactory.getLogger(T4ASubscriber.class);


    public T4ASubscriber( T4AUtilitiesNanny nanny){
        this.nanny = nanny;
        T4AConnection connection = T4AConnection.getInstance();

        try {
            connection.client.setCallback(this);
            connection.client.subscribe(connection.TOPIC_CURRENT_ROOM_DATA + "/#");
            connection.client.subscribe(connection.TOPIC_SHOW_RESULTS + "/#");
            connection.client.subscribe(connection.TOPIC_HIDE_RESULTS + "/#");
            connection.client.subscribe(connection.TOPIC_SEND_VOTE + "/#");
            logger.info("Subscriber connected");
        } catch (MqttException e) {
            logger.error("Error connecting subscriber: {}", Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.error("Subscriber connection lost");
        JOptionPane.showMessageDialog(
                null,
                "Connection lost.",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        T4AConnection connection = T4AConnection.getInstance();
        if (topic.endsWith("/" + connection.CLIENT_ID)) {
            return;
        }

        if(topic.startsWith(connection.TOPIC_CURRENT_ROOM_DATA)){
            JSONObject obj = new JSONObject(new String(mqttMessage.getPayload()));
            handleMessageQueue(obj.getJSONArray("queue"));
            handleStoryCompleted(obj.getJSONArray("completed"));
            handleVotes(obj.getJSONObject("votes"));
            T4ABlackboard.getInstance().setActiveStory(obj.getString("active"));
            T4ABlackboard.getInstance().setCardLayout(obj.getString("layout"));
        }

        if(T4ABlackboard.getInstance().isHost()){
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

    private void handleMessageQueue(JSONArray queueArray){
        LinkedList<String> queue = new LinkedList<>();
        for (int i = 0; i < queueArray.length(); i++) {
            queue.add(queueArray.getString(i));
        }
        T4ABlackboard.getInstance().setStoryQueue(queue);
    }

    private void handleStoryCompleted(JSONArray completedArray){
        LinkedList<String[]> completedStories = new LinkedList<>();
        for (int i = 0; i < completedArray.length(); i++) {
            completedStories.add(new String[]{completedArray.getJSONArray(i).getString(0), completedArray.getJSONArray(i).getString(1)});
        }
        T4ABlackboard.getInstance().setCompletedStories(completedStories);
    }

    private void handleVotes(JSONObject jsonVotes){
        Map<String, List<Number>> voteMap = new HashMap<>();
        for (String key : jsonVotes.keySet()) {
            JSONArray arr = jsonVotes.getJSONArray(key);
            List<Number> numbers = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                Object v = arr.get(i);
                if (v instanceof Number) {
                    numbers.add((Number) v);
                }
            }
            voteMap.put(key, numbers);
        }
        T4ABlackboard.getInstance().setVoteHistory(voteMap);
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
