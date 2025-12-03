package interface_adapter.LevelUp;

import use_case.levelup.LevelUpInputBoundary;
import use_case.levelup.LevelUpInputData;

public class LevelUpController {

    private final LevelUpInputBoundary interactor;

    public LevelUpController(LevelUpInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Handles the player choosing to increase their health during a level-up.
     */
    public void chooseHealth() {
        final LevelUpInputData inputData = new LevelUpInputData("health");
        interactor.levelUp(inputData);
    }

    /**
     * Handles the player choosing to increase their damage during a level-up.
     */
    public void chooseDamage() {
        final LevelUpInputData inputData = new LevelUpInputData("damage");
        interactor.levelUp(inputData);
    }
}
