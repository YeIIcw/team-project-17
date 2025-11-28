package use_case;

import entity.Character;
import entity.Combatant;
import entity.Enemy;
import org.junit.jupiter.api.Test;
import use_case.combat.CombatInputData;
import use_case.combat.CombatInteractor;
import use_case.combat.CombatOutputBoundary;
import use_case.combat.CombatOutputData;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatInteractorTest {

    static class FakePresenter implements CombatOutputBoundary {
        CombatOutputData lastOutput;

        @Override
        public void present(CombatOutputData outputData) {
            this.lastOutput = outputData;
        }
    }

    @Test
    void fullCombatFlow_PlayerWins() {
        Combatant player = new Character(100, 10, 5);
        Combatant enemy = new Enemy(25, 5);

        FakePresenter presenter = new FakePresenter();
        CombatInteractor interactor = new CombatInteractor(presenter, player, enemy);

        interactor.startBattle();
        CombatOutputData out = presenter.lastOutput;

        assertEquals("PLAYER_ACTION_CHOICE", out.getNextPhase());
        assertTrue(out.isOngoing());

        interactor.choosePlayerAction(new CombatInputData(CombatInputData.ACTION_LIGHT, null, false));
        out = presenter.lastOutput;
        assertEquals("PLAYER_ACTION_QUESTION", out.getNextPhase());
        assertEquals("EASY", out.getQuestionDifficulty());

        interactor.executePlayerAction(new CombatInputData(null, null, true));
        out = presenter.lastOutput;
        assertEquals(15, out.getOpponentHealth());
        assertEquals("DEFENSE_CHOICE", out.getNextPhase());

        int pending = out.getPendingEnemyDamage();
        assertTrue(pending > 0);

        interactor.defend(new CombatInputData(null, CombatInputData.DEFENSE_DODGE, true));
        out = presenter.lastOutput;
        assertEquals(100, out.getPlayerHealth());

        interactor.choosePlayerAction(new CombatInputData(CombatInputData.ACTION_HEAVY, null, false));
        interactor.executePlayerAction(new CombatInputData(null, null, true));
        out = presenter.lastOutput;

        assertFalse(out.isOngoing());
        assertTrue(out.isPlayerWon());
    }
}
