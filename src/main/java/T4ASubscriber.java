import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
            connection.client.subscribe(connection.TOPIC_ACTIVE_STORY + "/#");
            connection.client.subscribe(connection.TOPIC_COMPLETED_STORY + "/#");
            connection.client.subscribe(connection.TOPIC_VOTES + "/#");
            connection.client.subscribe(connection.TOPIC_SHOW_RESULTS + "/#");
            connection.client.subscribe(connection.TOPIC_STORY_QUEUE + "/#");
            connection.client.subscribe(connection.TOPIC_SEND_VOTE + "/#");
            connection.client.subscribe(connection.TOPIC_LAYOUT + "/#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        if (topic.endsWith("/" + connection.CLIENT_ID)) {
            return;
        }

        if(topic.startsWith(connection.TOPIC_STORY_QUEUE)){
            String[] message  = new String(mqttMessage.getPayload()).split(";");
            handleMessageQueue(message[0]);
            handleStoryCompleted(message[1]);
            handleActiveStory(message[2]);
            handleLayout(message[3]);
        }

        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        if(blackboard.isHost()){
            if(topic.startsWith(connection.TOPIC_SEND_VOTE)){
                String[] rawData = new String(mqttMessage.getPayload()).split(",");
                blackboard.addVote(rawData[0], rawData[1], false);
            }
        }else{
            if(topic.startsWith(connection.TOPIC_SHOW_RESULTS)){
                String[] rawData = new String(mqttMessage.getPayload()).split(";");
                List<String> labels = List.of(rawData[0].split(","));
                blackboard.setResultsLabels(labels.toArray(new String[0]));

                Integer[] values = Arrays.stream(rawData[1].split(","))
                        .map(String::trim)
                        .map(Integer::valueOf)
                        .toArray(Integer[]::new);
                blackboard.setResultsValues(values);
                nanny.switchToResultsGUI();

            }
            if(topic.startsWith(connection.TOPIC_ACTIVE_STORY)){
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
        for(int i = 0; i < data.length; i++){
            String[] story = {"", ""};
            if(i%2 == 0){
                story[0] = data[i];
            }else{
                story[1] = data[i];
            }
            completedStories.add(story);
        }
        blackboard.setCompletedStories(completedStories);
    }

    private void handleActiveStory(String rawData){
        T4ABlackboard.getInstance().setActiveStory(rawData);
    }

    private void handleVotes(String rawData){
        T4ABlackboard blackboard = T4ABlackboard.getInstance();
        String[] data = rawData.split(",");
        Map<String, String> votes = new HashMap<>();
        for(int i = 0; i < data.length; i++){
            String[] vote = {"", ""};
            if(i%2 == 0){
                vote[0] = data[i];
            }else{
                vote[1] = data[i];
            }
            votes.put(vote[0], vote[1]);
        }
        blackboard.setVotes(votes);
    }

    private void handleLayout(String rawData){
        T4ABlackboard.getInstance().setCardLayout(rawData);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
