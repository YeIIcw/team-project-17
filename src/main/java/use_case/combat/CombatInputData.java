package use_case.combat;

public class CombatInputData {
  public static final String ACTION_LIGHT = "LIGHT_ATTACK";
  public static final String ACTION_HEAVY = "HEAVY_ATTACK";
  public static final String ACTION_HEAL = "HEAL";

  public static final String DEFENSE_DODGE = "DODGE";
  public static final String DEFENSE_COUNTER = "COUNTER";

  private final String actionType;
  private final String defenseType;
  private final boolean correct;

  public CombatInputData(String actionType, String defenseType, boolean correct) {
    this.actionType = actionType;
    this.defenseType = defenseType;
    this.correct = correct;
  }

  public String getActionType() {
    return actionType;
  }

  public String getDefenseType() {
    return defenseType;
  }

  public boolean isCorrect() {
    return correct;
  }
}
