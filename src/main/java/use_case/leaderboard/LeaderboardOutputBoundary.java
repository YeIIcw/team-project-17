package use_case.leaderboard;

public interface LeaderboardOutputBoundary {
    void prepareSuccessView(String successMessage);
    void prepareFailView(String error);
}
