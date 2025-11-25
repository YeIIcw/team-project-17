package use_case.leaderboard;

public class LeaderboardInputData {
    private final String username;
    private final int score;

    public LeaderboardInputData(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
