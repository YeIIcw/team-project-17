package interface_adapter.LevelUp;

import java.util.ArrayList;
import java.util.List;

public class LevelUpViewModel {

    private int health;
    private int damage;

    public void setStats(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() { return health; }
    public int getDamage() { return damage; }
}
