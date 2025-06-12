import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Stories organized in tabs.
 * Delegates logic to T4ASouthPanelNanny.
 *
 * @author MichaelMan
 */
public class T4ASouthPanel extends JPanel implements PropertyChangeListener {
	private final JTextArea completedStoriesArea;
	private final JTextArea activeStoriesArea;
	private final JTextArea upcomingStoriesArea;
	private final T4ASouthPanelNanny nanny;

	public T4ASouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());

		JTabbedPane storyTabs = new JTabbedPane();

		activeStoriesArea = new JTextArea();
		activeStoriesArea.setEditable(false);

		completedStoriesArea = new JTextArea();
		completedStoriesArea.setEditable(false);

		upcomingStoriesArea = new JTextArea();
		upcomingStoriesArea.setEditable(false);

		storyTabs.addTab("Active Stories", new JScrollPane(activeStoriesArea));
		storyTabs.addTab("Completed Stories", new JScrollPane(completedStoriesArea));
		storyTabs.addTab("Upcoming Stories", new JScrollPane(upcomingStoriesArea));

		add(storyTabs, BorderLayout.CENTER);

		nanny = new T4ASouthPanelNanny(activeStoriesArea, completedStoriesArea, upcomingStoriesArea);

		T4ABlackboard.getInstance().addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		nanny.handlePropertyChange(evt);
	}
}
