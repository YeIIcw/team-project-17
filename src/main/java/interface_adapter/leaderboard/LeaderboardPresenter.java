package interface_adapter.leaderboard;

import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

import java.util.List;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    @Override
    public void prepareSuccessView(LeaderboardOutputData outputData) {
        System.out.println("Success view: " + outputData.getMessage());
    }

    @Override
    public void prepareFailView(LeaderboardOutputData outputData) {
        System.out.println("Fail view: " + outputData.getMessage());
    }

    @Override
    public void prepareLeaderboardView(List<ScoreEntry> highScores) {
        System.out.println("----------LEADERBOARD----------");
        if (highScores.isEmpty()) {
            System.out.println("No scores yet");
        } else {
            for (int i = 0; i < highScores.size(); i++) {
                ScoreEntry entry = highScores.get(i);
                System.out.println((i + 1) + ". " + entry.getUsername() + " - " + entry.getScore() + " points");
            }
        }
        System.out.println("-------------------------------");
    }
}
