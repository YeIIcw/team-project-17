package use_case.levelup;

public class LevelUpOutputData {
    private final int newHealth;
    private final int newDamage;

    public LevelUpOutputData(int newHealth, int newDamage) {
        this.newHealth = newHealth;
        this.newDamage = newDamage;
    }

    public int getNewHealth() {
        return newHealth;
    }

    public int getNewDamage() {
        return newDamage;
    }

}
