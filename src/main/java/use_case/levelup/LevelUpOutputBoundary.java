package use_case.levelup;

public interface LevelUpOutputBoundary {
    /**
     * Updates the view model with the latest character stats and displays the level-up screen.
     *
     * @param outputData the output data containing the character's new health and damage
     */
    void presentUpdatedStats(LevelUpOutputData outputData);

    /**
     * Finalizes the level-up process by updating the view model, hiding the level-up screen,
     * and notifying any registered callbacks that the level-up is complete.
     *
     * @param outputData the output data containing the character's new health and damage
     */
    void levelUpComplete(LevelUpOutputData outputData);
}
