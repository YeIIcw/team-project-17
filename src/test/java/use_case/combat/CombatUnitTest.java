package use_case.combat;

import entity.Enemy;
import entity.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombatUnitTest {

    static class FakePresenter implements CombatOutputBoundary {
        CombatOutputData lastOutput;

        @Override
        public void present(CombatOutputData outputData) {
            this.lastOutput = outputData;
        }
    }

    private GameState gameState;
    private FakePresenter presenter;
    private CombatInteractor interactor;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        // Create a weaker enemy for testing (25 HP instead of default 50)
        gameState.getCurrentEnemy().setHealth(25); // Set enemy HP to 25 for testing
        presenter = new FakePresenter();
        interactor = new CombatInteractor(presenter, gameState);
    }

    @Test
    void fullCombatFlow_PlayerWins() {
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
        // Enemy started with 25 HP, took 10 damage, should have 15 HP
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
