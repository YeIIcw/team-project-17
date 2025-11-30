package use_case.combat;

public class CombatOutputData {

    private final int playerHealth;
    private final int opponentHealth;
    private final boolean ongoing;
    private final boolean playerWon;
    private final boolean playerLost;
    private final int damageToPlayer;
    private final int damageToOpponent;
    private final int healAmount;
    private final String nextPhase;
    private final String questionDifficulty;
    private final String actionType;
    private final String defenseType;
    private final int pendingEnemyDamage;

    public CombatOutputData(int playerHealth,
                            int opponentHealth,
                            boolean ongoing,
                            boolean playerWon,
                            boolean playerLost,
                            int damageToPlayer,
                            int damageToOpponent,
                            int healAmount,
                            String nextPhase,
                            String questionDifficulty,
                            String actionType,
                            String defenseType,
                            int pendingEnemyDamage) {

        this(playerHealth, opponentHealth, ongoing, playerWon, playerLost,
                damageToPlayer, damageToOpponent, healAmount, nextPhase,
                questionDifficulty, actionType, defenseType,
                pendingEnemyDamage, false, 0, 0);
    }

    // FULL constructor (used for GAME OVER)
    public CombatOutputData(int playerHealth,
                            int opponentHealth,
                            boolean ongoing,
                            boolean playerWon,
                            boolean playerLost,
                            int damageToPlayer,
                            int damageToOpponent,
                            int healAmount,
                            String nextPhase,
                            String questionDifficulty,
                            String actionType,
                            String defenseType,
                            int pendingEnemyDamage,
                            boolean gameOver,
                            int finalXP,
                            int finalRound) {

        this.playerHealth = playerHealth;
        this.opponentHealth = opponentHealth;
        this.ongoing = ongoing;
        this.playerWon = playerWon;
        this.playerLost = playerLost;
        this.damageToPlayer = damageToPlayer;
        this.damageToOpponent = damageToOpponent;
        this.healAmount = healAmount;
        this.nextPhase = nextPhase;
        this.questionDifficulty = questionDifficulty;
        this.actionType = actionType;
        this.defenseType = defenseType;
        this.pendingEnemyDamage = pendingEnemyDamage;
    }

    public int getPlayerHealth() { return playerHealth; }
    public int getOpponentHealth() { return opponentHealth; }
    public boolean isOngoing() { return ongoing; }
    public boolean isPlayerWon() { return playerWon; }
    public boolean isPlayerLost() { return playerLost; }
    public int getDamageToPlayer() { return damageToPlayer; }
    public int getDamageToOpponent() { return damageToOpponent; }
    public int getHealAmount() { return healAmount; }
    public String getNextPhase() { return nextPhase; }
    public String getQuestionDifficulty() { return questionDifficulty; }
    public String getActionType() { return actionType; }
    public String getDefenseType() { return defenseType; }
    public int getPendingEnemyDamage() { return pendingEnemyDamage; }
}
