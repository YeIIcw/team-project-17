package interface_adapter.leaderboard;

import entity.ScoreEntry;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardViewModel {
  private List<ScoreEntry> highScores = new ArrayList<>();
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public static final String HIGHSCORES_PROPERTY = "highScores";

  public List<ScoreEntry> getHighScores() {
    return highScores;
  }

  public void setHighScores(List<ScoreEntry> highScores) {
    this.highScores = highScores;
    support.firePropertyChange(HIGHSCORES_PROPERTY, null, this.highScores);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
