package entity;

public class Character extends Combatant {
    private int healing;
    private int timesLeveled;

    public Character(int health, int damage, int healing) {
        super(health, damage);
        this.damage = 30;
        this.healing = healing;
        this.timesLeveled = 0;
    }

    public int getHealing() {
        return healing;
    }

    public void setHealing(int healing) {
        this.healing = healing;
    }

    public void heal() {
        this.health += healing;
    }

    public void levelUp() { this.timesLeveled ++; }

    public int getTimesLeveld() {return this.timesLeveled;}

    public void increaseDamage() {this.damage = (int) (this.damage * 1.1);}

    public void increaseHealth() {this.health = (int) (this.health * 1.05);}
}
