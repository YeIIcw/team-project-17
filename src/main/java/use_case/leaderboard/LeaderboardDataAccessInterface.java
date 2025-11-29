package use_case.leaderboard;

import entity.ScoreEntry;

import java.util.List;

public interface LeaderboardDataAccessInterface {
  void saveScore(ScoreEntry score);
  List<ScoreEntry> getAllScores();

}
