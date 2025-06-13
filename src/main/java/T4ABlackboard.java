import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Location for shared app data
 *
 * @author Uriel Hernandez-Vega
 * @version 1
 */
public class T4ABlackboard extends PropertyChangeSupport {
    private LinkedList<String> storyQueue = new LinkedList<>();
    private LinkedList<String[]> completedStory = new LinkedList<>();
    private  String activeStory = "";
    private  String selectedCard = "";
    private Map<String, List<Number>> voteHistory = new HashMap<>();

    private String user = "";


    private String activeRoom = "";
    private String roomLayout = "Standard";
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

    public LinkedList<String> getStoryQueue(){
        return storyQueue;
    }

    public void setStoryQueue(LinkedList<String> stories){
        storyQueue = stories;
        firePropertyChange("newStory", null, storyQueue);
    }

    public void addCompletedStory(String story, float score){
        completedStory.add(new String[]{story, Float.toString(score)});
        firePropertyChange("completedStory", null, completedStory);
    }

    public void setCompletedStories(LinkedList<String[]> stories){
        completedStory = stories;
        firePropertyChange("completedStory", null, completedStory);
    }

    public LinkedList<String[]> getCompletedStories(){
        return completedStory;
    }

    public void setActiveStory(String story){
        activeStory = story;
        firePropertyChange("activeStory", null, activeStory);
    }

    public String getActiveStory(){
        return activeStory;
    }

    public void setSelected(String value){
        selectedCard = value;
    }

    public String getSelected(){
        return selectedCard;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String name){
        user = name;
    }

    public void addNewRoom(String name, String mode){
        activeRoom = name;
        roomLayout = mode;
    }

    public void joinRoom(String name){
        activeRoom = name;
    }

    public String getRoomId(){
        return activeRoom;
    }

    public void setCardLayout(String layout){
        roomLayout = layout;
        firePropertyChange("layout", null, layout);
    }

    public String getCardLayout(){
        return roomLayout;
    }

    public Map<String, String> getVotes(){
        return votes;
    }

    public void setVotes(Map<String, String> votes){
        this.votes = votes;
    }

    public void addVote(String vote , String name, boolean notify){
        votes.put(name, vote);
        if(notify) firePropertyChange("submitVote", null, vote);
    }

    public Map<String, List<Number>> getVoteHistory() {
        return voteHistory;
    }
    public void setVoteHistory(Map<String, List<Number>> vh) {
        voteHistory = vh;
    }
    public void addVoteHistory(String vote, String name){
        voteHistory.computeIfAbsent(name, k -> new ArrayList<>()).add(Float.parseFloat(vote));
    }

    public void setHost(){
        isHost = true;
    }

    public boolean isHost(){
        return isHost;
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

    public void showResults(){
        firePropertyChange("reveal", null, null);
    }

    public void hideResults(){
        firePropertyChange("endReveal", null, null);
    }


}
