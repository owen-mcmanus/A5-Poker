import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains handlers for managing the general control of the application.
 * Features include opening the add story panel and showing/hiding results.
 *
 * @author MichaelMan
 */
public class T4AUtilitiesNanny {
    T4MainInterface window;
    T4AUtilitiesNanny(T4MainInterface window){
       this.window = window;
    }

    public void openNewStoryWindow(){
        T4ANewStoryWindow newStoryWindow = new T4ANewStoryWindow();
        newStoryWindow.showWindow();
    }

    public void showResults(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        bb.addCompletedStory(bb.getActiveStory(), calculateResults());
        bb.showResults();
        switchToResultsGUI();
    }

    public void switchToNextStory(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        bb.setActiveStory(bb.dequeueNewStory());
        bb.setSelected("");
        bb.setVotes(new HashMap<>());
        bb.hideResults();
        switchToCardsGUI();
    }

    public void switchToResultsGUI() {
        T4ABlackboard bb = T4ABlackboard.getInstance();
        T4APieChartPanel resultsPanel = new T4APieChartPanel(bb.getResultsLabels(), bb.getResultsValues());
        Component center = ((BorderLayout) window.getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (center != null)
            window.remove(center);

        window.add(resultsPanel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }

    public void switchToCardsGUI() {
        T4ACardsPanel cardsPanel = new T4ACardsPanel();
        Component center = ((BorderLayout) window.getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (center != null)
            window.remove(center);

        window.add(cardsPanel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }

    private float calculateResults(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        Map<String,String> votes = bb.getVotes();
        float sum = 0;

        Map<String, Integer> voteCount = new HashMap<>();
        for(String name : votes.keySet()){
            String vote = votes.get(name);

            sum += Float.parseFloat(vote);

            if(voteCount.containsKey(vote)){
                voteCount.put(vote, voteCount.get(vote) + 1);
            }else{
                voteCount.put(vote, 1);
            }
        }

        bb.setResultsLabels(voteCount.keySet().toArray(new String[0]));
        bb.setResultsValues(voteCount.values().toArray(new Integer[0]));

        return sum / votes.size();
    }
}
