package interface_adapter.levelup;

import interface_adapter.LevelUp.LevelUpController;
import use_case.levelup.LevelUpInputBoundary;
import use_case.levelup.LevelUpInputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelUpControllerTest {

    private LevelUpController controller;
    private TestInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new TestInteractor();
        controller = new LevelUpController(interactor);
    }

    // --------- Manual test double ---------
    static class TestInteractor implements LevelUpInputBoundary {
        boolean levelUpCalled = false;
        LevelUpInputData lastInput = null;

        @Override
        public void levelUp(LevelUpInputData inputData) {
            levelUpCalled = true;
            lastInput = inputData;
        }
    }

    // --------- Tests ---------

    @Test
    void testChooseHealthCallsInteractor() {
        controller.chooseHealth();

        assertTrue(interactor.levelUpCalled, "Interactor should have been called");
        assertNotNull(interactor.lastInput, "InputData should not be null");
        assertEquals("health", interactor.lastInput.getStatToIncrease());
    }

    @Test
    void testChooseDamageCallsInteractor() {
        controller.chooseDamage();

        assertTrue(interactor.levelUpCalled, "Interactor should have been called");
        assertNotNull(interactor.lastInput, "InputData should not be null");
        assertEquals("damage", interactor.lastInput.getStatToIncrease());
    }
}
