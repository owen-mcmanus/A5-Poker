import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                String value = String.join(";", makeStoryQueueMessage(), makeStoryCompletedMessage(), makeActiveStoryMessage(), makeLayoutMessage());
                MqttMessage message = new MqttMessage(value.getBytes());
                message.setQos(2);

                if (connection.client.isConnected()) {
                    connection.client.publish(connection.TOPIC_CURRENT_ROOM_DATA + "/" + connection.CLIENT_ID, message);
                }

                if(publishEndReveal && connection.client.isConnected()){
                    MqttMessage revealMessage = new MqttMessage("hideResults".getBytes());
                    revealMessage.setQos(2);
                    connection.client.publish(connection.TOPIC_HIDE_RESULTS + "/" + connection.CLIENT_ID, revealMessage);
                    publishEndReveal = false;
                }

                if(publishReveal && connection.client.isConnected()){
                    T4ABlackboard bb = T4ABlackboard.getInstance();

                    // conjoin Integer[] into comma separated String
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

    private String makeStoryQueueMessage(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        LinkedList<String> sq = bb.getStoryQueue();
        StringBuilder builder = new StringBuilder();
        for(String story : sq){
            builder.append(story);
            builder.append(",");
        }
        return  builder.toString();
    }

    private String makeStoryCompletedMessage(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        LinkedList<String[]> completedStories = bb.getCompletedStories();
        StringBuilder builder = new StringBuilder();
        for(String[] story : completedStories){
            builder.append(story[0]);
            builder.append(",");
            builder.append(story[1]);
            builder.append(",");
        }
        return  builder.toString();
    }

    private String makeActiveStoryMessage(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        return  bb.getActiveStory();
    }

    private String makeLayoutMessage(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        return bb.getCardLayout();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "reveal")) publishReveal = true;
        if(Objects.equals(propertyChangeEvent.getPropertyName(), "endReveal")) publishEndReveal = true;
    }
}
