package use_case.levelup;

import entity.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelUpInteractorTest {

    private Character player;
    private TestPresenter presenter;
    private LevelUpInteractor interactor;

    @BeforeEach
    void setup() {
        player = new Character(100, 100, 20);

        presenter = new TestPresenter();
        interactor = new LevelUpInteractor(player, presenter);
    }


    @Test
    void testHealthIncrease() {
        int before = player.getHealth();

        LevelUpInputData input = new LevelUpInputData("health");
        interactor.levelUp(input); // MAIN CALL

        // Stat increased
        assertTrue(player.getHealth() > before);

        // Correct callback
        assertTrue(presenter.levelUpCompleteCalled);
        assertFalse(presenter.presentUpdatedStatsCalled);

        // Output correctness
        assertEquals(player.getHealth(), presenter.data.getNewHealth());
        assertEquals(player.getDamage(), presenter.data.getNewDamage());
    }


    @Test
    void testDamageIncrease() {
        int before = player.getDamage();

        LevelUpInputData input = new LevelUpInputData("damage");
        interactor.levelUp(input);

        // Stat increased
        assertTrue(player.getDamage() > before);

        // Correct callback
        assertTrue(presenter.levelUpCompleteCalled);
        assertFalse(presenter.presentUpdatedStatsCalled);

        // Output correctness
        assertEquals(player.getHealth(), presenter.data.getNewHealth());
        assertEquals(player.getDamage(), presenter.data.getNewDamage());
    }


    private static class TestPresenter implements LevelUpOutputBoundary {

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
}
