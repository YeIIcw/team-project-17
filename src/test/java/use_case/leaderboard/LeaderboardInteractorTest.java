package use_case.leaderboard;

import data_access_object.InMemoryLeaderboardDataAccessObject;
import entity.ScoreEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardInteractorTest {
    @Test
    void successTest() {
        LeaderboardInputData inputData = new LeaderboardInputData("Paul", 1500);
        LeaderboardDataAccessInterface dataAccess = new InMemoryLeaderboardDataAccessObject();

        LeaderboardOutputBoundary successPresenter = new LeaderboardOutputBoundary() {
            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                // success
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());

                // score saved
                assertEquals(1, dataAccess.getAllScores().size());
                ScoreEntry savedScore = dataAccess.getAllScores().get(0);
                assertEquals("Paul", savedScore.getUsername());
                assertEquals(1500, savedScore.getScore());
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Use case failure is unexpected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, successPresenter);
        interactor.saveScore(inputData);
    }

    @Test
    void multipleScoresTest() {
        LeaderboardDataAccessInterface dataAccess = new InMemoryLeaderboardDataAccessObject();

        LeaderboardOutputBoundary successPresenter = new LeaderboardOutputBoundary() {
            private int successCount = 0;

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                successCount++;
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());

                // check if second score saved
                if (successCount == 2) {
                    assertEquals(2, dataAccess.getAllScores().size());

                    // check both scores exist
                    boolean foundAlice = dataAccess.getAllScores().stream()
                            .anyMatch(score -> "Paul".equals(score.getUsername()) && score.getScore() == 1500);
                    boolean foundBob = dataAccess.getAllScores().stream()
                            .anyMatch(score -> "Bob".equals(score.getUsername()) && score.getScore() == 2000);

                    assertTrue(foundAlice, "Paul's score should be saved");
                    assertTrue(foundBob, "Bob's score should be saved");
                }
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Use case failure is unexpected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, successPresenter);

        // save scores
        interactor.saveScore(new LeaderboardInputData("Paul", 1500));
        interactor.saveScore(new LeaderboardInputData("Bob", 2000));
    }
}
