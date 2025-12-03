package interface_adapter.LevelUp;

public interface LevelUpViewCallback {
    /**
     * Displays the level-up screen using the provided view model.
     *
     * @param levelUpViewModel the view model containing the character's updated stats
     */
    void showLevelUpScreen(LevelUpViewModel levelUpViewModel);

    /**
     * Hides the level-up screen.
     */
    void hideLevelUpScreen();

}
