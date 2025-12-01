package interface_adapter.Combat;

import interface_adapter.LevelUp.LevelUpViewModel;
import use_case.combat.CombatOutputBoundary;
import use_case.combat.CombatOutputData;

import java.awt.*;

public class CombatPresenter implements CombatOutputBoundary {

    private final CombatViewModel viewModel;
    private final LevelUpViewModel levelUpViewModel;

    private Runnable uiUpdateCallback;
    private Runnable gameOverCallback;
    private Runnable levelUpCallback;

    public CombatPresenter(CombatViewModel viewModel, LevelUpViewModel levelUpViewModel) {
        this.viewModel = viewModel;
        this.levelUpViewModel = levelUpViewModel;
    }

    public void setUiUpdateCallback(Runnable uiUpdateCallback) {
        this.uiUpdateCallback = uiUpdateCallback;
    }

    public void setGameOverCallback(Runnable gameOverCallback) {
        this.gameOverCallback = gameOverCallback;
    }

    @Override
    public void present(CombatOutputData outputData) {
        viewModel.setPlayerHealth(outputData.getPlayerHealth());
        viewModel.setOpponentHealth(outputData.getOpponentHealth());
        viewModel.setOngoing(outputData.isOngoing());
        viewModel.setPlayerWon(outputData.isPlayerWon());
        viewModel.setPlayerLost(outputData.isPlayerLost());
        viewModel.setDamageToPlayer(outputData.getDamageToPlayer());
        viewModel.setDamageToOpponent(outputData.getDamageToOpponent());
        viewModel.setHealAmount(outputData.getHealAmount());
        viewModel.setActionType(outputData.getActionType());
        viewModel.setDefenseType(outputData.getDefenseType());
        viewModel.setPendingEnemyDamage(outputData.getPendingEnemyDamage());

        if (outputData.isPlayerLost()) {
            viewModel.setNextPhase("GAME_OVER");
            if (gameOverCallback != null) {
                gameOverCallback.run();
            }
        } else {
            if (outputData.getLvledUp()) {
                viewModel.setNextPhase("LEVEL_UP");
                if (levelUpCallback != null) {
                    levelUpCallback.run();
                }
                return;
            }
            viewModel.setNextPhase(outputData.getNextPhase());
        }

        if (uiUpdateCallback != null) {
            uiUpdateCallback.run();
        }
    }

    @Override
    public void playerReadyToLevel(int health, int damage) {
        // Update LevelUpViewModel
        levelUpViewModel.setStats(health, damage);

        // Trigger the callback to show the view
        if (levelUpCallback != null) {
            levelUpCallback.run();
        }
    }

    public void setLevelUpCallback(Runnable levelUpCallback) {
        this.levelUpCallback = levelUpCallback;
    }


    public CombatViewModel getViewModel() {
        return viewModel;
    }
}
