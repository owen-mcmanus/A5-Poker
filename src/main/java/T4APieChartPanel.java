
import javax.swing.*;
import java.awt.*;

/**
 * Created a piechart to show the result after the vote
 * it displays the title of the story getting voted on
 *
 * @author MichaelMan
 */

class T4APieChartPanel extends JPanel {
    private String[] labels;
    private Integer[] values;

    public T4APieChartPanel(String[] labels, Integer[] values) {
        this.labels = labels;
        this.values = values;


        setLayout(new BorderLayout());
        T4ASouthPanel t4ASouthPanel = new T4ASouthPanel();

        JLabel storyLabel = new JLabel(T4ABlackboard.getInstance().getActiveStory(), SwingConstants.CENTER);
        storyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        storyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(storyLabel, BorderLayout.NORTH);
        add(new PieCanvas(), BorderLayout.CENTER);
    }


    private class PieCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (values == null || values.length == 0) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(new Font("Arial", Font.BOLD, 14));

            Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN};

            int totalVotes = 0;
            int totalScore = 0;
            for (int i = 0; i < values.length; i++) {
                totalVotes += values[i];
                try {
                    totalScore += Integer.parseInt(labels[i]) * values[i];
                } catch (NumberFormatException e) {
                    // Skip votes
                }
            }

            int centerX = getWidth() / 3; // Shifted left
            int centerY = getHeight() / 2;
            int radius = 150;

            int startAngle = 0;
            double cumulativeAngle = 0;
            int sumValues = 0;
            for (int v : values) {
                sumValues += v;
            }

            // Draw pie slices
            for (int i = 0; i < values.length; i++) {
                g2d.setColor(colors[i % colors.length]);

                double rawAngle = values[i] * 360.0 / sumValues;
                int arcAngle;
                if (i == values.length - 1) {
                    arcAngle = 360 - (int) Math.round(cumulativeAngle);
                } else {
                    arcAngle = (int) Math.round(rawAngle);
                    cumulativeAngle += arcAngle;
                }

                g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, startAngle, arcAngle);

                startAngle += arcAngle;
            }

            // Draw inner white circle
            int innerRadius = 120;
            g2d.setColor(Color.WHITE);
            g2d.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);

            // Draw players and average inside
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));

            String playersText = totalVotes + " Player voted";
            String averageText = "Avg: " + (totalVotes == 0 ? "0" : String.format("%.1f", (double) totalScore / totalVotes));

            FontMetrics fm = g2d.getFontMetrics();
            int textHeight = fm.getHeight();

            g2d.drawString(playersText, centerX - fm.stringWidth(playersText) / 2, centerY - 5);
            g2d.drawString(averageText, centerX - fm.stringWidth(averageText) / 2, centerY + textHeight);

            // Draw bullet points on the right
            int legendStartX = getWidth() * 2 / 3; // Right third of screen
            int legendStartY = getHeight() / 8; // Change the legend vertically to the top

            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            for (int i = 0; i < values.length; i++) {
                // Draw small circle (bullet)
                g2d.setColor(colors[i % colors.length]);
                g2d.fillOval(legendStartX, legendStartY + i * 50, 10, 10); // spacing bigger

                // Draw label
                g2d.setColor(Color.BLACK);
                String label = labels[i];
                g2d.drawString(label, legendStartX + 20, legendStartY + i * 50 + 10);

                // Draw percentage and votes (second line)
                double percentage = (totalVotes == 0) ? 0 : (values[i] * 100.0 / totalVotes);
                int votes = values[i];
                String details = String.format("%.1f%% (%d votes)", percentage, votes);

                g2d.setFont(new Font("Arial", Font.PLAIN, 12)); // smaller font for second line
                g2d.drawString(details, legendStartX + 20, legendStartY + i * 50 + 30);

                // Restore font for next label
                g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            }
        }
    }
}


