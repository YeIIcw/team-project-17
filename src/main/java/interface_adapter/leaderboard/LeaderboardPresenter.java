package interface_adapter.leaderboard;

import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.leaderboard.LeaderboardOutputData;

import java.util.List;

public class LeaderboardPresenter implements LeaderboardOutputBoundary {

  private final LeaderboardViewModel viewModel;

  public LeaderboardPresenter(LeaderboardViewModel viewModel) {
    this.viewModel = viewModel;
  }

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
    viewModel.setHighScores(highScores);
  }
}
