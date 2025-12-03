package use_case.levelup;

public interface LevelUpInputBoundary {
    /**
     * Performs a level-up action for a character based on the provided input data.
     *
     * @param inputData the data specifying which stat to increase (e.g., health or damage)
     */
    void levelUp(LevelUpInputData inputData);
}
