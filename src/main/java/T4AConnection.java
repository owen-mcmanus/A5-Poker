import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Establishes a connection to an MQTT server for the publisher and subscriber
 *
 * @version 1
 * @author  owen-mcmanus
 */
public class T4AConnection {
    private final String BROKER = "tcp://broker.hivemq.com:1883";
    private final String TOPIC_HEADER = "CSC307T4A";

    public final String TOPIC_STORY_QUEUE;
    public final String TOPIC_ACTIVE_STORY;
    public final String TOPIC_COMPLETED_STORY;
    public final String TOPIC_VOTES;
    public final String TOPIC_SHOW_RESULTS;
    public final String TOPIC_HIDE_RESULTS;
    public final String TOPIC_LAYOUT;
    public final String TOPIC_SEND_VOTE;

    public final String CLIENT_ID;
    public final String ROOM_ID;
    public MqttClient client;

    public T4AConnection(){
        this.CLIENT_ID = T4ABlackboard.getInstance().getUser();
        this.ROOM_ID = T4ABlackboard.getInstance().getRoomId();

        this.TOPIC_STORY_QUEUE = String.join("/", TOPIC_HEADER, ROOM_ID, "STORY_QUEUE");
        this.TOPIC_ACTIVE_STORY = String.join("/", TOPIC_HEADER, ROOM_ID, "ACTIVE_STORY");
        this.TOPIC_COMPLETED_STORY = String.join("/", TOPIC_HEADER, ROOM_ID, "COMPLETED_STORY");
        this.TOPIC_VOTES = String.join("/", TOPIC_HEADER, ROOM_ID, "VOTES");
        this.TOPIC_SHOW_RESULTS = String.join("/", TOPIC_HEADER, ROOM_ID, "SHOW_RESULTS");
        this.TOPIC_HIDE_RESULTS = String.join("/", TOPIC_HEADER, ROOM_ID, "HIDE_RESULTS");
        this.TOPIC_LAYOUT = String.join("/", TOPIC_HEADER, ROOM_ID, "LAYOUT");
        this.TOPIC_SEND_VOTE = String.join("/", TOPIC_HEADER, ROOM_ID, "SEND_VOTE");

        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            client.connect();

            System.out.println("Connected to BROKER: " + BROKER);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
