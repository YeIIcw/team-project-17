package use_case.levelup;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LevelUpOutputDataTest {

    @Test
    void testConstructorStoresValues() {
        LevelUpOutputData data = new LevelUpOutputData(120, 35);

        assertEquals(120, data.getNewHealth());
        assertEquals(35, data.getNewDamage());
    }

    @Test
    void testValuesAreImmutable() {
        LevelUpOutputData data = new LevelUpOutputData(150, 40);

        // Values should not change — this is just checking the contract
        assertEquals(150, data.getNewHealth());
        assertEquals(40, data.getNewDamage());
    }

    @Test
    void testBoundaryValues() {
        LevelUpOutputData dataZero = new LevelUpOutputData(0, 0);

        assertEquals(0, dataZero.getNewHealth());
        assertEquals(0, dataZero.getNewDamage());

        LevelUpOutputData dataMax = new LevelUpOutputData(Integer.MAX_VALUE, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, dataMax.getNewHealth());
        assertEquals(Integer.MAX_VALUE, dataMax.getNewDamage());
    }
}
