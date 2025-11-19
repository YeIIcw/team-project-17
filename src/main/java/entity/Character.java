package entity;

public class Character {
    private int health;
    private int damage;
    private int healing;

    public Character(int health, int damage, int healing) {
        this.health = health;
        this.damage = damage;
        this.healing = healing;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealing() {
        return healing;
    }

    public void setHealing(int healing) {
        this.healing = healing;
    }
}
