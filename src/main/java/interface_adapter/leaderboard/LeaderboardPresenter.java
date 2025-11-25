package interface_adapter.leaderboard;

import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

    @Override
    public void prepareSuccessView(LeaderboardOutputData outputData) {
        System.out.println("Success view: " + outputData.getMessage());
    }

    @Override
    public void prepareFailView(LeaderboardOutputData outputData) {
        System.out.println("Fail view: " + outputData.getMessage());
    }
}
