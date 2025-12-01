package use_case.leaderboard;


import entity.ScoreEntry;
import org.junit.jupiter.api.Test;
import test_utils.TestLeaderboardDataAccess;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardInteractorTest {
    @Test
    void successTest() {
        LeaderboardInputData inputData = new LeaderboardInputData("Paul", 1500);
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();

        LeaderboardOutputBoundary successPresenter = new LeaderboardOutputBoundary() {

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                // success
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Unexpected use case failure.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }

        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, successPresenter);
        interactor.saveScore(inputData);

        // check if score was saved
        assertEquals(1, dataAccess.getAllScores().size());
        ScoreEntry savedScore = dataAccess.getAllScores().get(0);
        assertEquals("Paul", savedScore.getUsername());
        assertEquals(1500, savedScore.getScore());

    }

    @Test
    void multipleScoresTest() {
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();

        LeaderboardOutputBoundary successPresenter = new LeaderboardOutputBoundary() {

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Unexpected use case failure.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }

        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, successPresenter);

        // save scores
        interactor.saveScore(new LeaderboardInputData("Paul", 1500));
        interactor.saveScore(new LeaderboardInputData("Bob", 2000));

        // check both scores saved and correctly sorted
        List<ScoreEntry> scores = dataAccess.getAllScores();
        assertEquals(2, scores.size());

        // sorted highest first
        assertEquals("Bob", scores.get(0).getUsername());
        assertEquals(2000, scores.get(0).getScore());
        assertEquals("Paul", scores.get(1).getUsername());
        assertEquals(1500, scores.get(1).getScore());
    }

    @Test
    void showLeaderboardTest() {
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();

        // add test scores
        dataAccess.saveScore(new ScoreEntry("Mario", 1000));
        dataAccess.saveScore(new ScoreEntry("Toad", 2000));
        dataAccess.saveScore(new ScoreEntry("Luigi", 1500));

        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                fail("Success view not expected.");
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Fail view not expected.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                // get top 3 scores sorted highest first
                assertEquals(3, highScores.size());
                assertEquals("Toad", highScores.get(0).getUsername());
                assertEquals(2000, highScores.get(0).getScore());
                assertEquals("Luigi", highScores.get(1).getUsername());
                assertEquals(1500, highScores.get(1).getScore());
                assertEquals("Mario", highScores.get(2).getUsername());
                assertEquals(1000, highScores.get(2).getScore());
            }

        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, presenter);
        interactor.showLeaderboard();
    }

    @Test
    void updateHigherScoreTest() {
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();

        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            private int numCalls = 0;

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                numCalls++;
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());

                if (numCalls == 2) {
                    // only have one score (higher) after second save
                    List<ScoreEntry> scores = dataAccess.getAllScores();
                    assertEquals(1, scores.size());
                    assertEquals("Paul", scores.get(0).getUsername());
                    assertEquals(2000, scores.get(0).getScore());
                }
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Unexpected use case failure.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, presenter);

        // save initial score (lower)
        interactor.saveScore(new LeaderboardInputData("Paul", 1500));
        // save higher score for same user
        interactor.saveScore(new LeaderboardInputData("Paul", 2000));
    }

    @Test
    void keepHigherScoreTest() {
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();

        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {

            private int numCalls = 0;

            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                numCalls++;
                assertEquals("Score saved", outputData.getMessage());
                assertTrue(outputData.isSuccess());

                if (numCalls == 2) {
                    // only have one score (higher) after second save
                    List<ScoreEntry> scores = dataAccess.getAllScores();
                    assertEquals(1, scores.size());
                    assertEquals("Paul", scores.get(0).getUsername());
                    assertEquals(2000, scores.get(0).getScore());
                }
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Unexpected use case failure.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, presenter);

        // save higher score first
        interactor.saveScore(new LeaderboardInputData("Paul", 2000));
        // save lower score for same user
        interactor.saveScore(new LeaderboardInputData("Paul", 1500));
    }

    @Test
    void saveScoreExceptionTest() {
        LeaderboardInputData inputData = new LeaderboardInputData("Paul", 1500);

        // create data access that throws an exception
        LeaderboardDataAccessInterface failingDataAccess = new LeaderboardDataAccessInterface() {
            @Override
            public void saveScore(ScoreEntry scoreEntry) {
                throw new RuntimeException("Database connection failed");
            }

            @Override
            public List<ScoreEntry> getAllScores() {
                return List.of();
            }
        };

        LeaderboardOutputBoundary failPresenter = new LeaderboardOutputBoundary() {
            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                fail("Success view not expected.");
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                assertEquals("Failed to save score: Database connection failed", outputData.getMessage());
                assertFalse(outputData.isSuccess());
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(failingDataAccess, failPresenter);
        interactor.saveScore(inputData);
    }

    @Test
    void showLeaderboardExceptionTest() {
        // create a data access that throws an exception during getAllScores()
        LeaderboardDataAccessInterface failingDataAccess = new LeaderboardDataAccessInterface() {
            @Override
            public void saveScore(ScoreEntry scoreEntry) {
                // not used
            }

            @Override
            public List<ScoreEntry> getAllScores() {
                throw new RuntimeException("Failed to load scores");
            }
        };

        LeaderboardOutputBoundary failPresenter = new LeaderboardOutputBoundary() {
            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                fail("Success view not expected.");
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                assertEquals("Failed to load leaderboard", outputData.getMessage());
                assertFalse(outputData.isSuccess());
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                fail("Leaderboard view not expected.");
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(failingDataAccess, failPresenter);
        interactor.showLeaderboard();
    }

    @Test
    void showLeaderboardEmptyTest() {
        LeaderboardDataAccessInterface dataAccess = new TestLeaderboardDataAccess();
        // no scores added

        LeaderboardOutputBoundary presenter = new LeaderboardOutputBoundary() {
            @Override
            public void prepareSuccessView(LeaderboardOutputData outputData) {
                fail("Success view not expected.");
            }

            @Override
            public void prepareFailView(LeaderboardOutputData outputData) {
                fail("Fail view not expected.");
            }

            @Override
            public void prepareLeaderboardView(List<ScoreEntry> highScores) {
                assertTrue(highScores.isEmpty());
            }
        };

        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, presenter);
        interactor.showLeaderboard();
    }

}
