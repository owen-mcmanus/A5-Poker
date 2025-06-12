import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Establishes a connection to an MQTT server for the publisher and subscriber
 *
 * @version 1
 * @author  owen-mcmanus
 */
public class T4AConnection {
    private final String BROKER = "tcp://broker.hivemq.com:1883";
    private final String TOPIC_HEADER = "CSC307T4A";

    public final String TOPIC_CURRENT_ROOM_DATA;
    public final String TOPIC_SHOW_RESULTS;
    public final String TOPIC_HIDE_RESULTS;
    public final String TOPIC_SEND_VOTE;

    public final String CLIENT_ID;
    public final String ROOM_ID;
    public MqttClient client;

    private static T4AConnection instance;

    private T4AConnection(){
        this.CLIENT_ID = T4ABlackboard.getInstance().getUser();
        this.ROOM_ID = T4ABlackboard.getInstance().getRoomId();

        this.TOPIC_CURRENT_ROOM_DATA = String.join("/", TOPIC_HEADER, ROOM_ID, "CURRENT_ROOM_DATA");
        this.TOPIC_SHOW_RESULTS = String.join("/", TOPIC_HEADER, ROOM_ID, "SHOW_RESULTS");
        this.TOPIC_HIDE_RESULTS = String.join("/", TOPIC_HEADER, ROOM_ID, "HIDE_RESULTS");
        this.TOPIC_SEND_VOTE = String.join("/", TOPIC_HEADER, ROOM_ID, "SEND_VOTE");
    }

    public boolean connect(){
        Logger logger = LoggerFactory.getLogger(T4AConnection.class);
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            client.connect();
            logger.info("Connected to BROKER: " + BROKER);
            return true;
        } catch (MqttException e) {
            logger.error("Could not connect to BROKER: {}", Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static T4AConnection getInstance(){
        if(instance == null){
            instance = new T4AConnection();
        }
        return instance;
    }

}
