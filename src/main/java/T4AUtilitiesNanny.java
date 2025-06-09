import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;

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
        if(Objects.equals(bb.getActiveStory(), "")){
            JOptionPane.showMessageDialog(
                    window,
                    "No active story. Press next story.",
                    "Input Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(bb.getVotes().isEmpty()){
            JOptionPane.showMessageDialog(
                    window,
                    "No votes for active story.",
                    "Input Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        bb.addCompletedStory(bb.getActiveStory(), calculateResults());
        bb.showResults();
        switchToResultsGUI();
    }

    public void switchToNextStory(){
        T4ABlackboard bb = T4ABlackboard.getInstance();
        bb.setActiveStory(bb.dequeueNewStory());
        if(Objects.equals(bb.getActiveStory(), "")){
            JOptionPane.showMessageDialog(
                    window,
                    "Active story empty. Add new stories.",
                    "Input Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        bb.setSelected("");
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

    public void openVoteHistoryChartWindow() {
        T4ABlackboard bb = T4ABlackboard.getInstance();
        Map<String, List<Number>> voteHistory = bb.getVoteHistory();

        T4AVotingLineChartPanel chartPanel = new T4AVotingLineChartPanel(voteHistory);

        // new window to hold the chart
        JFrame chartFrame = new JFrame("Voting History Line Chart");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(800, 600);
        chartFrame.setLocationRelativeTo(null); // center on screen
        chartFrame.add(chartPanel);
        chartFrame.setVisible(true);
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

        bb.setVotes(new HashMap<>());

        return sum / votes.size();
    }
}
