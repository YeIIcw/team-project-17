package use_case.leaderboard;

public interface LeaderboardOutputBoundary {
    void prepareSuccessView(LeaderboardOutputData outputData);
    void prepareFailView(LeaderboardOutputData outputData);
}
