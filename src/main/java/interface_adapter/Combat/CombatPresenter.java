package interface_adapter.Combat;

import use_case.combat.CombatOutputBoundary;
import use_case.combat.CombatOutputData;

public class CombatPresenter implements CombatOutputBoundary {

    private final CombatViewModel viewModel;

    private Runnable uiUpdateCallback;
    private Runnable gameOverCallback;

    public CombatPresenter(CombatViewModel viewModel) {
        this.viewModel = viewModel;
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
            viewModel.setNextPhase(outputData.getNextPhase());
        }

        if (uiUpdateCallback != null) {
            uiUpdateCallback.run();
        }
    }

    public CombatViewModel getViewModel() {
        return viewModel;
    }
}
