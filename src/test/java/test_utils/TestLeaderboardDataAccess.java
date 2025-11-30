package test_utils;

import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardDataAccessInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestLeaderboardDataAccess implements LeaderboardDataAccessInterface {
    private final List<ScoreEntry> scores = new ArrayList<>();

    @Override
    public void saveScore(ScoreEntry newScore) {
        boolean userExists = false;
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry existing = scores.get(i);
            if (existing.getUsername().equals(newScore.getUsername())) {
                if (newScore.getScore() > existing.getScore()) {
                    scores.set(i, newScore);
                }
                userExists = true;
                break;
            }
        }

        if (!userExists) {
            scores.add(newScore);
        }

        scores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
    }

    @Override
    public List<ScoreEntry> getAllScores() {
        return new ArrayList<>(scores);
    }

    // helper method for tests
    public void clear() {
        scores.clear();
    }

}
