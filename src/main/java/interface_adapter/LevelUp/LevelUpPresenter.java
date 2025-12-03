package interface_adapter.LevelUp;

import use_case.levelup.LevelUpOutputBoundary;
import use_case.levelup.LevelUpOutputData;

public class LevelUpPresenter implements LevelUpOutputBoundary {
    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings", "checkstyle:TrailingComment"})
    public Runnable onLevelUpFinished; // public for test purposes
    private final LevelUpViewModel viewModel;
    private LevelUpViewCallback callback;

    public LevelUpPresenter(LevelUpViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setViewCallback(LevelUpViewCallback viewCallback) {
        this.callback = viewCallback;
    }

    /**
     * Updates the view model with the latest character stats and displays the level-up screen.
     *
     * <p></p>This method updates the health and damage values in the view model using the provided
     * LevelUpOutputData. If a view callback is registered, it will trigger the view to
     * show the level-up screen.
     *
     * @param data the output data containing the character's new health and damage
     */
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
