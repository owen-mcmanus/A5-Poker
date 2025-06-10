import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * GUI for managing the general control of the application.
 * Features include opening the add story panel and showing/hiding results.
 *
 * @author MichaelMan
 */
public class T4AUtilitiesPanel extends JPanel {
    public T4AUtilitiesPanel(T4AUtilitiesNanny t4AUtilitiesNanny){
        setLayout(new GridLayout(11, 1));
        if(T4ABlackboard.getInstance().isHost()){
            JButton viewResults = new JButton("View Results");
            JButton nextStory = new JButton("Next Story");
            JButton addStory = new JButton("Add Story");
            add(viewResults);
            add(nextStory);
            add(addStory);
            addStory.addActionListener(e -> t4AUtilitiesNanny.openNewStoryWindow());
            viewResults.addActionListener(e -> t4AUtilitiesNanny.showResults());
            nextStory.addActionListener(e -> t4AUtilitiesNanny.switchToNextStory());
        }

        JButton showLineChart = new JButton("Show Line Chart");
        add(showLineChart);
        showLineChart.addActionListener(e -> t4AUtilitiesNanny.openVoteHistoryChartWindow());

        JLabel playersTitle = new JLabel("User:");
        JLabel username = new JLabel(T4ABlackboard.getInstance().getUser());
        add(playersTitle);
        add(username);
    }
}
