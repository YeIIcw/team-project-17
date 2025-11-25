package data_access_object;

import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryLeaderboardDataAccessObject implements LeaderboardDataAccessInterface {
    private final List<ScoreEntry> scores = new ArrayList<>();

    @Override
    public void saveScore(ScoreEntry score) {
        scores.add(score);
    }

    @Override
    public List<ScoreEntry> getAllScores() {
        return new ArrayList<>(scores);
    }
}
