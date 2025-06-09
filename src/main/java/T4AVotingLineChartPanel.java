/**
 * Processes data from voting and creates a line chart displaying each voters vote for each story
 *
 * @version 1
 * @author Michael Man
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class T4AVotingLineChartPanel extends JPanel {

    public T4AVotingLineChartPanel(Map<String, List<Number>> voteHistory) {
        System.out.println(voteHistory);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, List<Number>> entry : voteHistory.entrySet()) {
            String user = entry.getKey();
            List<Number> votes = entry.getValue();

            for (int i = 0; i < votes.size(); i++) {
                String storyLabel = "Story " + (i + 1);
                dataset.addValue(votes.get(i), user, storyLabel);
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Voting History by User",
                "Story",
                "Vote",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }
}
