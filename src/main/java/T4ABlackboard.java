import java.beans.PropertyChangeSupport;
import java.util.*;


/**
 * Location for shared app data
 *
 * @author Owen McManus
 * @version 1
 */
public class T4ABlackboard extends PropertyChangeSupport {
    private LinkedList<String> storyQueue = new LinkedList<>();
    private LinkedList<String[]> completedStory = new LinkedList<>();
    private  String activeStory = "";
    private  String selectedCard = "";

    private String user = "Default User";


    private String[] activeRoom = {"", ""};
    private Map<String, String> votes = new HashMap<>();
    private boolean isHost = false;

    private String[] resultsLabels = {};
    private Integer[] resultsValues = {};

    public final int ROOM_WINDOW_WIDTH = 500;
    public final int ROOM_WINDOW_HEIGHT = 500;
    public final int MAIN_WINDOW_WIDTH = 1000;
    public final int MAIN_WINDOW_HEIGHT = 800;

    private static T4ABlackboard instance;

    private T4ABlackboard(){
        super(new Object());
    }
    public static T4ABlackboard getInstance(){
        if(instance == null)
            instance = new T4ABlackboard();
        return instance;
    }

    public void addNewStory(String story){
        storyQueue.add(story);
        firePropertyChange("newStory", null, storyQueue);
    }

    public String dequeueNewStory(){
        String story = storyQueue.poll();
        firePropertyChange("newStory", null, storyQueue);
        return story != null ? story : "" ;
    }

    public void addCompletedStory(String story, float score){
        completedStory.add(new String[]{story, Float.toString(score)});
        firePropertyChange("completedStory", null, completedStory);
    }

    public void setActiveStory(String story){
        activeStory = story;
        firePropertyChange("activeStory", null, activeStory);
    }

    public void setSelected(String value){
        selectedCard = value;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String name){
        user = name;
    }

    public void addNewRoom(String name, String mode){
        activeRoom[0] = name;
        activeRoom[1] = mode;
    }

    public void joinRoom(String name){
        activeRoom[0] = name;
        activeRoom[1] = "Standard";
    }

    public String getCardLayout(){
        return activeRoom[1];
    }
    public String getRoomId(){
        return activeRoom[0];
    }

    public String getSelection(){
        return selectedCard;
    }

    public String getActiveStory(){
        return activeStory;
    }

    public LinkedList<String> getStoryQueue(){
        return storyQueue;
    }

    public LinkedList<String[]> getCompletedStories(){
        return completedStory;
    }

    public Map<String, String> getVotes(){
        return votes;
    }

    public void setHost(){
        isHost = true;
    }

    public boolean isHost(){
        return isHost;
    }

    public void setCompletedStories(LinkedList<String[]> stories){
        completedStory = stories;
        firePropertyChange("completedStory", null, completedStory);
    }

    public void setStoryQueue(LinkedList<String> stories){
        storyQueue = stories;
        firePropertyChange("newStory", null, storyQueue);
    }

    public void setVotes(Map<String, String> votes){
        this.votes = votes;
    }

    public void setCardLayout(String layout){
        activeRoom[1] = layout;
        firePropertyChange("layout", null, layout);
    }

    public void showResults(){
        firePropertyChange("reveal", null, null);
    }

    public void hideResults(){
        firePropertyChange("endReveal", null, null);
    }

    public void addVote(String vote , String name, boolean notify){
        votes.put(name, vote);
        if(notify) firePropertyChange("submitVote", null, vote);
    }

    public String[] getResultsLabels() {
        return resultsLabels;
    }

    public void setResultsLabels(String[] resultsLabels) {
        this.resultsLabels = resultsLabels;
    }

    public Integer[] getResultsValues() {
        return resultsValues;
    }

    public void setResultsValues(Integer[] resultsValues) {
        this.resultsValues = resultsValues;
    }
}
