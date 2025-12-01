package interface_adapter.LevelUp;

import use_case.levelup.LevelUpInputBoundary;
import use_case.levelup.LevelUpInputData;

public class LevelUpController {

    private final LevelUpInputBoundary interactor;

    public LevelUpController(LevelUpInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void chooseHealth() {
        LevelUpInputData inputData = new LevelUpInputData("health");
        interactor.levelUp(inputData);
    }

    public void chooseDamage() {
        LevelUpInputData inputData = new LevelUpInputData("damage");
        interactor.levelUp(inputData);
    }
}
