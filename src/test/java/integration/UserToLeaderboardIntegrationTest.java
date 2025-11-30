package integration;

import data_access_object.InMemoryLeaderboardDataAccessObject;
import entity.GameState;
import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardInputData;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserToLeaderboardIntegrationTest {

    // mock presenter for testing
    static class MockLeaderboardPresenter implements LeaderboardOutputBoundary {
        public String lastMessage;
        public boolean lastSuccess;
        public java.util.List<ScoreEntry> lastScores;

        @Override
        public void prepareSuccessView(use_case.leaderboard.LeaderboardOutputData outputData) {
            this.lastMessage = outputData.getMessage();
            this.lastSuccess = outputData.isSuccess();
        }

        @Override
        public void prepareFailView(use_case.leaderboard.LeaderboardOutputData outputData) {
            this.lastMessage = outputData.getMessage();
            this.lastSuccess = outputData.isSuccess();
        }

        @Override
        public void prepareLeaderboardView(java.util.List<ScoreEntry> scores) {
            this.lastScores = scores;
        }
    }

    @Test
    public void testUsernameFlowFromLoginToLeaderboard() {
        System.out.println("Testing Username Flow Integration");

        // login
        GameState gameState = new GameState();
        gameState.setCurrentUsername("TestPlayer1");

        // setup leaderboard
        InMemoryLeaderboardDataAccessObject dataAccess = new InMemoryLeaderboardDataAccessObject();
        MockLeaderboardPresenter presenter = new MockLeaderboardPresenter();
        LeaderboardInteractor leaderboardInteractor = new LeaderboardInteractor(dataAccess, presenter);

        // game completion and score saving
        int finalScore = 1500;
        String usernameFromGameState = gameState.getCurrentUsername();

        System.out.println("Saving score for: " + usernameFromGameState);

        // save score to leaderboard
        LeaderboardInputData inputData = new LeaderboardInputData(usernameFromGameState, finalScore);
        leaderboardInteractor.saveScore(inputData);

        // verify score saved with correct username
        leaderboardInteractor.showLeaderboard();

        assertNotNull(presenter.lastScores, "Leaderboard should have scores");
        assertTrue(presenter.lastScores.size() > 0, "Should have at least one score");

        ScoreEntry savedScore = presenter.lastScores.get(0);
        assertEquals("TestPlayer1", savedScore.getUsername(),
                "Username should flow from login to leaderboard");
        assertEquals(1500, savedScore.getScore(),
                "Score should be saved correctly");

        System.out.println("Username correctly flowed from login to leaderboard: " + savedScore.getUsername());
        System.out.println("Score correctly saved: " + savedScore.getScore());
    }
}


