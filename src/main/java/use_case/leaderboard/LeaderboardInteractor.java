package use_case.leaderboard;

import entity.ScoreEntry;

import java.util.Comparator;
import java.util.List;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
  private final LeaderboardDataAccessInterface dataAccess;
  private final LeaderboardOutputBoundary outputBoundary;

  public LeaderboardInteractor(LeaderboardDataAccessInterface dataAccess, LeaderboardOutputBoundary outputBoundary) {
    this.dataAccess = dataAccess;
    this.outputBoundary = outputBoundary;
  }

  @Override
  public void saveScore(LeaderboardInputData inputData) {
    try {
      ScoreEntry newScore = new ScoreEntry(inputData.getUsername(), inputData.getScore());
      dataAccess.saveScore(newScore);

      LeaderboardOutputData outputData = new LeaderboardOutputData("Score saved", true);
      outputBoundary.prepareSuccessView(outputData);
    } catch (Exception e) {
      LeaderboardOutputData outputData = new LeaderboardOutputData("Failed to save score: " + e.getMessage(), false);
      outputBoundary.prepareFailView(outputData);
    }
  }

  @Override
  public void showLeaderboard() {
    try {
      List<ScoreEntry> allScores = dataAccess.getAllScores();
      List<ScoreEntry> sortedScores = allScores.stream()
          .sorted(Comparator.comparingInt(ScoreEntry::getScore).reversed()).limit(3).toList();

      outputBoundary.prepareLeaderboardView(sortedScores);
    } catch (Exception e) {
      LeaderboardOutputData outputData = new LeaderboardOutputData("Failed to load leaderboard", false);
      outputBoundary.prepareFailView(outputData);
    }
  }
}
