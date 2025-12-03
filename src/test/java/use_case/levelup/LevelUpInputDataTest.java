package use_case.levelup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelUpInputDataTest {

    @Test
    void testConstructorStoresValue() {
        LevelUpInputData input = new LevelUpInputData("damage");
        assertEquals("damage", input.getStatToIncrease());
    }

    @Test
    void testStoresHealthValue() {
        LevelUpInputData input = new LevelUpInputData("health");
        assertEquals("health", input.getStatToIncrease());
    }

    @Test
    void testHandlesEmptyString() {
        LevelUpInputData input = new LevelUpInputData("");
        assertEquals("", input.getStatToIncrease());
    }

    @Test
    void testHandlesNullValue() {
        LevelUpInputData input = new LevelUpInputData(null);
        assertNull(input.getStatToIncrease());
    }

    @Test
    void testStoresMixedCaseValue() {
        LevelUpInputData input = new LevelUpInputData("HeAlTh");
        assertEquals("HeAlTh", input.getStatToIncrease());
    }
}
