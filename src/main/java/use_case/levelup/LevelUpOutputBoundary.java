package use_case.levelup;

import entity.Character;

public interface LevelUpOutputBoundary {
    void presentUpdatedStats(LevelUpOutputData outputData);
    void levelUpComplete(LevelUpOutputData outputData);
}
