package entity;

public class Character extends Combatant {
  private int healing;

  public Character(int health, int damage, int healing) {
    super(health, damage);
    this.healing = healing;
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
}
