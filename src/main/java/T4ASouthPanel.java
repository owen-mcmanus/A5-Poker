
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 *
 * After a vote, the completed story is updated and the story voted on is removed from active story
 *
 * Active stories is now based on user inputed stories
 *
 * Created a button to remove the top active story
 */

public class T4ASouthPanel extends JPanel implements PropertyChangeListener {
	private JTextArea completedStoriesArea;
	private JTextArea activeStoriesArea;
	private JTextArea upcomingStoriesArea;

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

		T4ABlackboard.getInstance().addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		if(Objects.equals(propertyChangeEvent.getPropertyName(), "newStory")){
			Queue<String> storiesQueue = (Queue<String>) propertyChangeEvent.getNewValue();
			StringBuilder builder = new StringBuilder();
			for(String story : storiesQueue){
				builder.append(story);
				builder.append("\n");
			}
			upcomingStoriesArea.setText(builder.toString());
		}
		if(Objects.equals(propertyChangeEvent.getPropertyName(), "completedStory")){
			List<String[]> storiesList = (List<String[]>) propertyChangeEvent.getNewValue();
			StringBuilder builder = new StringBuilder();
			for(String[] story : storiesList){
				builder.append(story[0]);
				builder.append(": ");
				builder.append(story[1]);
				builder.append("\n");
			}
			completedStoriesArea.setText(builder.toString());
		}
		if(Objects.equals(propertyChangeEvent.getPropertyName(), "activeStory")){
			String activeStory = (String) propertyChangeEvent.getNewValue();
			activeStoriesArea.setText(activeStory);
		}

	}
}
