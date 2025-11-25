package interface_adapter.leaderboard;

import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInputData;

public class LeaderboardController {
    private final LeaderboardInputBoundary leaderboardUseCaseInteractor;

    public LeaderboardController(LeaderboardInputBoundary leaderboardUseCaseInteractor) {
        this.leaderboardUseCaseInteractor = leaderboardUseCaseInteractor;
    }

    public void execute(String username, int score) {
        LeaderboardInputData inputData = new LeaderboardInputData(username, score);
        leaderboardUseCaseInteractor.saveScore(inputData);
    }

    public void showLeaderboard() {
        leaderboardUseCaseInteractor.showLeaderboard();
    }
}
