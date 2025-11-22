package interface_adapter.Combat;

import use_case.combat.CombatOutputBoundary;
import use_case.combat.CombatOutputData;

public class CombatPresenter implements CombatOutputBoundary {
    private final CombatViewModel viewModel;

    public CombatPresenter(CombatViewModel viewModel) {
        this.viewModel = viewModel;
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
        viewModel.setNextPhase(outputData.getNextPhase());
        viewModel.setQuestionDifficulty(outputData.getQuestionDifficulty());
        viewModel.setActionType(outputData.getActionType());
        viewModel.setDefenseType(outputData.getDefenseType());
        viewModel.setPendingEnemyDamage(outputData.getPendingEnemyDamage());
    }

    public CombatViewModel getViewModel() {
        return viewModel;
    }
}
