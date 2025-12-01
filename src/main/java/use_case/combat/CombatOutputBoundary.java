package use_case.combat;

import interface_adapter.LevelUp.LevelUpViewModel;

public interface CombatOutputBoundary {
    void present(CombatOutputData outputData);
    void playerReadyToLevel(int health, int damage);
}
