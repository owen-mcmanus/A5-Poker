import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages card logic for T4ACardsPanel
 *
 * @author MichaelMan
 * @version 1
 */
public class T4ACardsPanelNanny {
    private final JPanel panel;
    private final Map<String, String[]> cardValues;
    private final ArrayList<JButton> buttons;
    Logger logger = LoggerFactory.getLogger(T4ACardsPanelNanny.class);


    private final Color baseColor = new Color(172, 248, 199);
    private final Color selectedColor = new Color(248, 172, 199);
    private JButton selectedButton = null;

    public T4ACardsPanelNanny(JPanel panel, Map<String, String[]> cardValues, ArrayList<JButton> buttons) {
        this.panel = panel;
        this.cardValues = cardValues;
        this.buttons = buttons;
    }

    public void handleCardClick(JButton card) {
        try {
            T4ABlackboard bb = T4ABlackboard.getInstance();


            if (Objects.equals(bb.getActiveStory(), "")) {
                JOptionPane.showMessageDialog(
                        panel,
                        "No active story. Add new stories and press next story.",
                        "Input Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (selectedButton != null) {
                selectedButton.setBackground(baseColor);
            }

            card.setBackground(selectedColor);
            selectedButton = card;

            bb.setSelected(card.getText());
            bb.addVote(card.getText(), bb.getUser(), true);
        } catch (Exception e) {
            logger.error("Error handling card click for value: {}", String.valueOf(e));
        }
    }

    public void updateCardLayout(String layoutType) {
        try {
            String[] newValues = cardValues.get(layoutType);
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setText(newValues[i]);
            }
            panel.repaint();
            panel.revalidate();
            logger.info("Card layout updated to: {}", layoutType);
        } catch (Exception e) {
            logger.error("Failed to update card layout: {}", String.valueOf(e));
        }
    }
}
