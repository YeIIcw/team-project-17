package use_case.leaderboard;

public class LeaderboardOutputData {
  private final String message;
  private final boolean success;

  public LeaderboardOutputData(String message, boolean success) {
    this.message = message;
    this.success = success;
  }

  public String getMessage() {
    return message;
  }
  public boolean isSuccess() {
    return success;
  }
}
