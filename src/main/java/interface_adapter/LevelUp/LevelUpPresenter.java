package interface_adapter.LevelUp;

import use_case.levelup.LevelUpOutputBoundary;
import use_case.levelup.LevelUpOutputData;


public class LevelUpPresenter implements LevelUpOutputBoundary {
    private final LevelUpViewModel viewModel;
    private LevelUpViewCallback callback; // the view registers itself
    public Runnable onLevelUpFinished;

    public LevelUpPresenter(LevelUpViewModel vm) {
        this.viewModel = vm;
    }

    public void setViewCallback(LevelUpViewCallback callback) {
        this.callback = callback;
    }

    public void presentUpdatedStats(LevelUpOutputData data) {
        viewModel.setStats(data.getNewHealth(), data.getNewDamage());
        if (callback != null) {
            callback.showLevelUpScreen(viewModel);
        }
    }

    @Override
    public void levelUpComplete(LevelUpOutputData data) {
        // Update view model
        viewModel.setStats(data.getNewHealth(), data.getNewDamage());

        // Hide UI
        if (callback != null) {
            callback.hideLevelUpScreen();
        }

        // Notify Combat that leveling is done
        if (onLevelUpFinished != null) {
            onLevelUpFinished.run();
        }
    }
}
