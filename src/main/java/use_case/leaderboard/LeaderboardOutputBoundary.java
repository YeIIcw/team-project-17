package use_case.leaderboard;

import entity.ScoreEntry;

import java.util.List;

public interface LeaderboardOutputBoundary {
    void prepareSuccessView(LeaderboardOutputData outputData);
    void prepareFailView(LeaderboardOutputData outputData);
    void prepareLeaderboardView(List<ScoreEntry> highScores);

}
