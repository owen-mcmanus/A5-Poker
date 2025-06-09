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

    public void fakeData() {
        // wasnt sure if you wanted a new room
        //addNewRoom("Test", "Standard");
        addNewStory("STORY 1: ADDING NAMES");
        addNewStory("STORY 2: ADDING CARDS");
        addNewStory("STORY 3: END");

        String[] users = {"Alice", "Bob", "Carl", "Dave", "Erick"};
        String[] possibleVotes = {"1", "2", "3", "5", "8"};
        Random rand = new Random();

        while (!getStoryQueue().isEmpty()) {
            String story = dequeueNewStory();
            setActiveStory(story);
            setVotes(new HashMap<>());

            for (String user : users) {
                String vote = possibleVotes[rand.nextInt(possibleVotes.length)];
                addVote(vote, user, true);
            }

            Collection<String> allVotes = getVotes().values();
            float total = 0;
            for (String vote : allVotes) {
                total += Float.parseFloat(vote);
            }
            float avg = allVotes.isEmpty() ? 0 : total / allVotes.size();

            addCompletedStory(story, avg);

            Map<String, Integer> tally = new HashMap<>();
            for (String vote : allVotes) {
                tally.put(vote, tally.getOrDefault(vote, 0) + 1);
            }

            List<String> sortedLabels = new ArrayList<>(tally.keySet());
            Collections.sort(sortedLabels);
            String[] labels = sortedLabels.toArray(new String[0]);
            Integer[] values = new Integer[labels.length];
            for (int i = 0; i < labels.length; i++) {
                values[i] = tally.get(labels[i]);
            }
            setResultsLabels(labels);
            setResultsValues(values);

            showResults();
        }
    }
}
