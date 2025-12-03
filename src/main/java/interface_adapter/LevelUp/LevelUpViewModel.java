package interface_adapter.LevelUp;

public class LevelUpViewModel {

    private int health;
    private int damage;

    /**
     * Updates the character's health and damage stats in the view model.
     *
     * @param newHealth the new health value to set
     * @param newDamage the new damage value to set
     */
    public void setStats(int newHealth, int newDamage) {
        this.health = newHealth;
        this.damage = newDamage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
}
