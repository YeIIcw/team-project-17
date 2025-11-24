package interface_adapter.Gameplay;

import use_case.gameplay.GameplayInputBoundary;
import use_case.gameplay.GameplayInputData;

public class GameplayController {

    private final GameplayInputBoundary interactor;

    public GameplayController(GameplayInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void submitAnswer(int selectedChoiceIndex) {
        GameplayInputData inputData = new GameplayInputData(selectedChoiceIndex);
        interactor.submitAnswer(inputData);
    }

    public void getNextQuestion() {
        interactor.getNextQuestion();
    }
}

