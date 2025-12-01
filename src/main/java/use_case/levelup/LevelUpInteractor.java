package use_case.levelup;

import entity.Character;

public class LevelUpInteractor implements LevelUpInputBoundary {
    private final Character player;
    private final LevelUpOutputBoundary presenter;

    private Runnable levelUpFinishedCallback;


    public LevelUpInteractor(Character player, LevelUpOutputBoundary presenter) {
        this.player = player;
        this.presenter = presenter;
    }

    public void setLevelUpFinishedCallback(Runnable callback) {
        this.levelUpFinishedCallback = callback;
    }

    @Override
    public void levelUp(LevelUpInputData inputData) {
        String stat = inputData.getStatToIncrease();

        switch (stat.toLowerCase()) {
            case "health":
                player.increaseHealth();
                break;
            case "damage":
                player.increaseDamage();
                break;
        }

        LevelUpOutputData outputData = new LevelUpOutputData(
                player.getHealth(),
                player.getDamage()
        );

        presenter.levelUpComplete(outputData);

        if (levelUpFinishedCallback != null) {
            levelUpFinishedCallback.run();
        }
    }
}
