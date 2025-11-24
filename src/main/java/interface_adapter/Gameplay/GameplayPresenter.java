package interface_adapter.Gameplay;

import use_case.gameplay.GameplayOutputBoundary;
import use_case.gameplay.GameplayOutputData;

public class GameplayPresenter implements GameplayOutputBoundary {

    private final GameplayViewModel viewModel;

    public GameplayPresenter(GameplayViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAnswerResult(GameplayOutputData outputData) {
        viewModel.setCorrect(outputData.isCorrect());
        viewModel.setQuestionScore(outputData.getQuestionScore());
        viewModel.setTotalScore(outputData.getTotalScore());
        viewModel.setHasMoreQuestions(outputData.hasMoreQuestions());
        
        if (outputData.isCorrect()) {
            viewModel.setMessage("Correct! +" + outputData.getQuestionScore() + " points");
        } else {
            viewModel.setMessage("Incorrect! The correct answer was: " + 
                outputData.getCurrentQuestion().getChoices().get(
                    outputData.getCurrentQuestion().getCorrectChoiceIndex()));
        }
    }

    @Override
    public void presentQuestion(GameplayOutputData outputData) {
        System.out.println("DEBUG: GameplayPresenter - presentQuestion() called");
        System.out.println("DEBUG: GameplayPresenter - Question: " + 
            (outputData.getCurrentQuestion() != null ? outputData.getCurrentQuestion().getText() : "null"));
        
        viewModel.setCurrentQuestion(outputData.getCurrentQuestion());
        viewModel.setTotalScore(outputData.getTotalScore());
        viewModel.setHasMoreQuestions(outputData.hasMoreQuestions());
        viewModel.setMessage(null); // Clear previous message
        
        System.out.println("DEBUG: GameplayPresenter - ViewModel updated with question");
    }
}

