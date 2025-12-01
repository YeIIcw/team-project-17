package use_case.gameplay;

import app.AppBuilder;
import entity.GameState;
import entity.Question;
import view.GameOverView;

public class GameplayInteractor implements GameplayInputBoundary {

    private final GameState gameState;
    private final GameplayOutputBoundary presenter;
    private final AppBuilder appBuilder;

    public GameplayInteractor(GameState gameState, GameplayOutputBoundary presenter, AppBuilder appBuilder) {
        this.gameState = gameState;
        this.presenter = presenter;
        this.appBuilder = appBuilder;
    }

    @Override
    public void submitAnswer(GameplayInputData inputData) {
        Question currentQuestion = gameState.getCurrentQuestion();
        int selectedIndex = inputData.getSelectedChoiceIndex();
        int correctIndex = currentQuestion.getCorrectChoiceIndex();
        
        boolean isCorrect = selectedIndex == correctIndex;
        int questionScore = 0;
        
        if (isCorrect) {
            questionScore = currentQuestion.getScoreValue();
            gameState.addScore(questionScore);
        }
        
        // Move to next question after answering
        gameState.moveToNextQuestion();
        
        GameplayOutputData outputData = new GameplayOutputData(
            isCorrect,
            questionScore,
            currentQuestion,
            gameState.getScore(),
            gameState.hasMoreQuestions()
        );
        
        presenter.presentAnswerResult(outputData);
    }

    @Override
    public void getNextQuestion() {
        System.out.println("DEBUG: GameplayInteractor - getNextQuestion() called");
        System.out.println("DEBUG: GameplayInteractor - hasMoreQuestions: " + gameState.hasMoreQuestions());
        System.out.println("DEBUG: GameplayInteractor - questions list size: " + 
            (gameState.getQuestions() != null ? gameState.getQuestions().size() : "null"));
        System.out.println("DEBUG: GameplayInteractor - currentQuestionIndex: " + gameState.getCurrentQuestionIndex());

        if (!gameState.hasMoreQuestions()) {
            System.out.println("DEBUG: GameplayInteractor - No more questions available");
            // No more questions - could show game over screen
            GameOverView gameOverView = new GameOverView(appBuilder, gameState.getScore(), gameState.getEnemiesDefeated());
            gameOverView.display();
            return;
        }

        Question currentQuestion = gameState.getCurrentQuestion();
        System.out.println("DEBUG: GameplayInteractor - Current question: " + 
            (currentQuestion != null ? currentQuestion.getText() : "null"));
        
        GameplayOutputData outputData = new GameplayOutputData(
            false, // Not an answer submission
            0,     // No score yet
            currentQuestion,
            gameState.getScore(),
            gameState.hasMoreQuestions()
        );
        
        System.out.println("DEBUG: GameplayInteractor - Calling presenter.presentQuestion()");
        presenter.presentQuestion(outputData);
    }
}
