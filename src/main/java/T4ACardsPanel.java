import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Panel that displays the cards used for estimating.
 * Behaviours are executed by CardsPanelNanny
 * @author MichaelMan
 */

public class T4ACardsPanel extends JPanel implements PropertyChangeListener {
	private final Map<String, String[]> CARD_VALUES = new HashMap<>();
	private final ArrayList<JButton> buttons = new ArrayList<>();
	private final Color baseColor = new Color(172, 248, 199);

	private final T4ACardsPanelNanny nanny;

	public T4ACardsPanel() {
		CARD_VALUES.put("Standard", new String[]{"0", "0.5", "1", "2", "3", "5", "8", "20", "40", "80", "200", "500"});
		CARD_VALUES.put("Increasing", new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"});
		CARD_VALUES.put("Fibonacci", new String[]{"0", "1", "2", "3", "5", "8", "13", "21", "34", "55", "89", "144"});

		setLayout(new GridLayout(4, 3, 10, 10));

		nanny = new T4ACardsPanelNanny(this, CARD_VALUES, buttons);

		String[] values = CARD_VALUES.get(T4ABlackboard.getInstance().getCardLayout());
		for (String value : values) {
			JButton card = new JButton(value);
			card.setBackground(baseColor);
			card.setFont(new Font("SansSerif", Font.BOLD, 20));
			card.addActionListener(e -> nanny.handleCardClick(card));
			buttons.add(card);
			add(card);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("layout".equals(evt.getPropertyName())) {
			nanny.updateCardLayout(evt.getNewValue().toString());
		}
	}
}
