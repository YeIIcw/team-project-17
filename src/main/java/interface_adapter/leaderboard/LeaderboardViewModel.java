package interface_adapter.leaderboard;

import entity.ScoreEntry;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardViewModel {
    private List<ScoreEntry> highScores = new ArrayList<>();

    public List<ScoreEntry> getHighScores() {
        return highScores;
    }

    public void setHighScores(List<ScoreEntry> highScores) {
        this.highScores = highScores;
    }
}
