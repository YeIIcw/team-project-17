package interface_adapter.Combat;

public class CombatViewModel {
  private int playerHealth;
  private int opponentHealth;
  private boolean ongoing;
  private boolean playerWon;
  private boolean playerLost;
  private int damageToPlayer;
  private int damageToOpponent;
  private int healAmount;
  private String nextPhase;
  private String questionDifficulty;
  private String actionType;
  private String defenseType;
  private int pendingEnemyDamage;

  public int getPlayerHealth() {
    return playerHealth;
  }

  public void setPlayerHealth(int playerHealth) {
    this.playerHealth = playerHealth;
  }

  public int getOpponentHealth() {
    return opponentHealth;
  }

  public void setOpponentHealth(int opponentHealth) {
    this.opponentHealth = opponentHealth;
  }

  public boolean isOngoing() {
    return ongoing;
  }

  public void setOngoing(boolean ongoing) {
    this.ongoing = ongoing;
  }

  public boolean isPlayerWon() {
    return playerWon;
  }

  public void setPlayerWon(boolean playerWon) {
    this.playerWon = playerWon;
  }

  public boolean isPlayerLost() {
    return playerLost;
  }

  public void setPlayerLost(boolean playerLost) {
    this.playerLost = playerLost;
  }

  public int getDamageToPlayer() {
    return damageToPlayer;
  }

  public void setDamageToPlayer(int damageToPlayer) {
    this.damageToPlayer = damageToPlayer;
  }

  public int getDamageToOpponent() {
    return damageToOpponent;
  }

  public void setDamageToOpponent(int damageToOpponent) {
    this.damageToOpponent = damageToOpponent;
  }

  public int getHealAmount() {
    return healAmount;
  }

  public void setHealAmount(int healAmount) {
    this.healAmount = healAmount;
  }

  public String getNextPhase() {
    return nextPhase;
  }

  public void setNextPhase(String nextPhase) {
    this.nextPhase = nextPhase;
  }

  public String getQuestionDifficulty() {
    return questionDifficulty;
  }

  public void setQuestionDifficulty(String questionDifficulty) {
    this.questionDifficulty = questionDifficulty;
  }

  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public String getDefenseType() {
    return defenseType;
  }

  public void setDefenseType(String defenseType) {
    this.defenseType = defenseType;
  }

  public int getPendingEnemyDamage() {
    return pendingEnemyDamage;
  }

  public void setPendingEnemyDamage(int pendingEnemyDamage) {
    this.pendingEnemyDamage = pendingEnemyDamage;
  }
}
