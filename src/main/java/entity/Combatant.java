package entity;

public abstract class Combatant {
    protected int health;
    protected int damage;

    public Combatant(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void takeDamage(int amount) {
        setHealth(this.health - amount);
    }

    public boolean isDead() {
        return health <= 0;
    }
}
