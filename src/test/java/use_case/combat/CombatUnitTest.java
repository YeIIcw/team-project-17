package use_case.combat;

import entity.Character;
import entity.Enemy;
import entity.GameState;
import org.junit.jupiter.api.Test;
import use_case.levelup.LevelUpOutputBoundary;
import use_case.levelup.LevelUpOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatUnitTest {

    static class FakePresenter implements CombatOutputBoundary {
        CombatOutputData lastOutput;

        @Override
        public void present(CombatOutputData outputData) {
            this.lastOutput = outputData;
        }

        @Override
        public void playerReadyToLevel(int health, int damage) {

        }
    }

    private static class FakeLevelUpPresenter implements LevelUpOutputBoundary {

        boolean presentUpdatedStatsCalled = false;
        boolean levelUpCompleteCalled = false;

        LevelUpOutputData data;

        @Override
        public void presentUpdatedStats(LevelUpOutputData outputData) {
            presentUpdatedStatsCalled = true;
            this.data = outputData;
        }

        @Override
        public void levelUpComplete(LevelUpOutputData outputData) {
            levelUpCompleteCalled = true;
            this.data = outputData;
        }
    }

    static class FakeGameState extends GameState {
        private final Character player;
        private Enemy currentEnemy;
        private int xp;
        private int enemyIndex;
        private int enemiesDefeated;

        FakeGameState(Character player, Enemy enemy) {
            super();
            this.player = player;
            this.currentEnemy = enemy;
            this.xp = 0;
            this.enemyIndex = 0;
            this.enemiesDefeated = 0;
        }

        public Character getPlayer() {
            return player;
        }

        public Enemy getCurrentEnemy() {
            return currentEnemy;
        }

        public void addXP(int amount) {
            xp += amount;
        }

        public int getEnemyIndex() {
            return enemyIndex;
        }

        public void incrementEnemiesDefeated() {
            enemiesDefeated++;
        }

        public void nextEnemy() {
            enemyIndex++;
            currentEnemy = new Enemy(30, 5);
        }

        public void resetPlayerHealth() {
        }

        public int getEnemiesDefeated() {
            return enemiesDefeated;
        }

        int getXp() {
            return xp;
        }
    }

    @Test
    void fullCombatFlow_FirstEnemyDies_WithLightAndDodge() {
        Character player = new Character(100, 10, 5);
        Enemy enemy = new Enemy(25, 5);

        FakeGameState gameState = new FakeGameState(player, enemy);
        FakePresenter presenter = new FakePresenter();
        FakeLevelUpPresenter levelUpPresenter = new FakeLevelUpPresenter();
        CombatInteractor interactor = new CombatInteractor(presenter, gameState, levelUpPresenter);

        interactor.startBattle();
        CombatOutputData out = presenter.lastOutput;

        assertEquals("PLAYER_ACTION_CHOICE", out.getNextPhase());
        assertTrue(out.isOngoing());

        while (gameState.getEnemiesDefeated() == 0) {
            interactor.choosePlayerAction(new CombatInputData(CombatInputData.ACTION_LIGHT, null, false));
            out = presenter.lastOutput;
            assertEquals("PLAYER_ACTION_QUESTION", out.getNextPhase());
            assertEquals("EASY", out.getQuestionDifficulty());

            interactor.executePlayerAction(new CombatInputData(null, null, true));
            out = presenter.lastOutput;

            if (!out.isOngoing()) {
                break;
            }

            if ("PLAYER_ACTION_CHOICE".equals(out.getNextPhase()) && gameState.getEnemiesDefeated() > 0) {
                break;
            }

            if ("DEFENSE_CHOICE".equals(out.getNextPhase())) {
                int pendingDamage = out.getPendingEnemyDamage();
                assertTrue(pendingDamage > 0);

                int hpBeforeDefense = out.getPlayerHealth();

                interactor.defend(new CombatInputData(null, CombatInputData.DEFENSE_DODGE, true));
                out = presenter.lastOutput;

                assertEquals("PLAYER_ACTION_CHOICE", out.getNextPhase());
                assertEquals(hpBeforeDefense, out.getPlayerHealth());
                assertEquals(0, out.getDamageToPlayer());
                assertTrue(out.isOngoing());
            }
        }

        assertTrue(gameState.getEnemiesDefeated() >= 1);
        assertTrue(gameState.getXp() > 0);
    }
}
