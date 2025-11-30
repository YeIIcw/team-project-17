package use_case.combat;

import entity.Battle;
import entity.Character;
import entity.Combatant;
import entity.Enemy;
import entity.GameState;

import java.util.Random;

public class CombatInteractor implements CombatInputBoundary {

  private final CombatOutputBoundary presenter;
  private final GameState gameState;

  private Battle battle;
  private final Random random = new Random();

  private String pendingActionType;
  private int pendingEnemyDamage;

  public CombatInteractor(CombatOutputBoundary presenter, GameState gameState) {
    this.presenter = presenter;
    this.gameState = gameState;
    this.battle = null;
    this.pendingActionType = null;
    this.pendingEnemyDamage = 0;
  }

  @Override
  public void startBattle() {
    Character player = gameState.getPlayer();
    Enemy enemy = gameState.getCurrentEnemy();

    battle = new Battle(player, enemy);
    pendingActionType = null;
    pendingEnemyDamage = 0;

    CombatOutputData out = new CombatOutputData(player.getHealth(), enemy.getHealth(), true, false, false, 0, 0, 0,
        "PLAYER_ACTION_CHOICE", null, null, null, 0);
    presenter.present(out);
  }

  @Override
  public void choosePlayerAction(CombatInputData inputData) {
    if (battle == null || !battle.isOngoing()) {
      return;
    }

    pendingActionType = inputData.getActionType();
    String difficulty = getPlayerActionDifficulty(pendingActionType);

    CombatOutputData out = new CombatOutputData(battle.getPlayer().getHealth(), battle.getOpponent().getHealth(),
        battle.isOngoing(), battle.isPlayerWon(), battle.isPlayerLost(), 0, 0, 0, "PLAYER_ACTION_QUESTION", difficulty,
        pendingActionType, null, 0);
    presenter.present(out);
  }

  @Override
  public void executePlayerAction(CombatInputData inputData) {
    if (battle == null || !battle.isOngoing()) {
      return;
    }

    Combatant player = battle.getPlayer();
    Combatant enemy = battle.getOpponent();

    boolean correct = inputData.isCorrect();
    int damageToPlayer = 0;
    int damageToOpponent = 0;
    int healAmount = 0;

    gameState.addXP(5);

    if (correct && pendingActionType != null) {

      if (pendingActionType.equals(CombatInputData.ACTION_LIGHT)) {
        int before = enemy.getHealth();
        enemy.takeDamage(player.getDamage());
        damageToOpponent = before - enemy.getHealth();

      } else if (pendingActionType.equals(CombatInputData.ACTION_HEAVY)) {

        if (random.nextDouble() <= 0.75) {
          int heavyDamage = (player.getDamage() * 3) / 2;
          int before = enemy.getHealth();
          enemy.takeDamage(heavyDamage);
          damageToOpponent = before - enemy.getHealth();
        } else {
          damageToOpponent = -1; // heavy attack miss işareti
        }

      } else if (pendingActionType.equals(CombatInputData.ACTION_HEAL)) {
        if (player instanceof Character) {
          Character c = (Character) player;
          int before = c.getHealth();
          c.heal();
          healAmount = c.getHealth() - before;
        }
      }
    }

    updateBattleState();

    if (!battle.isOngoing()) {
      handleBattleEnd(damageToPlayer, damageToOpponent, healAmount, pendingActionType, null);
      return;
    }

    int baseDamage = enemy.getDamage();
    double mult = 1.0 + random.nextDouble(); // 1.0–2.0 arası
    pendingEnemyDamage = (int) Math.round(baseDamage * mult);

    CombatOutputData out = new CombatOutputData(player.getHealth(), enemy.getHealth(), battle.isOngoing(),
        battle.isPlayerWon(), battle.isPlayerLost(), damageToPlayer, damageToOpponent, healAmount, "DEFENSE_CHOICE",
        null, pendingActionType, null, pendingEnemyDamage);
    presenter.present(out);
  }

  @Override
  public void defend(CombatInputData inputData) {
    if (battle == null || !battle.isOngoing()) {
      return;
    }

    String defenseType = inputData.getDefenseType();
    boolean correct = inputData.isCorrect();

    Combatant player = battle.getPlayer();
    Combatant enemy = battle.getOpponent();

    int damageToPlayer = 0;
    int damageToOpponent = 0;
    int healAmount = 0;

    gameState.addXP(5);

    if (correct) {

      if (CombatInputData.DEFENSE_COUNTER.equals(defenseType)) {
        double roll = random.nextDouble();

        if (roll <= 0.70) {
          int before = enemy.getHealth();
          enemy.takeDamage(player.getDamage());
          damageToOpponent = before - enemy.getHealth();
        } else {
          int before = player.getHealth();
          player.takeDamage(pendingEnemyDamage);
          damageToPlayer = before - player.getHealth();
          damageToOpponent = -2;
        }
      }

    } else {
      int before = player.getHealth();
      player.takeDamage(pendingEnemyDamage);
      damageToPlayer = before - player.getHealth();
    }

    pendingEnemyDamage = 0;
    updateBattleState();

    if (!battle.isOngoing()) {
      handleBattleEnd(damageToPlayer, damageToOpponent, healAmount, pendingActionType, defenseType);
      return;
    }

    CombatOutputData out = new CombatOutputData(player.getHealth(), enemy.getHealth(), battle.isOngoing(),
        battle.isPlayerWon(), battle.isPlayerLost(), damageToPlayer, damageToOpponent, healAmount,
        "PLAYER_ACTION_CHOICE", null, pendingActionType, defenseType, 0);
    presenter.present(out);
  }

  private void updateBattleState() {
    if (battle.getOpponent().isDead()) {
      battle.setOngoing(false);
      battle.setPlayerWon(true);
    } else if (battle.getPlayer().isDead()) {
      battle.setOngoing(false);
      battle.setPlayerLost(true);
    }
  }

  private void handleBattleEnd(int dmgToPlayer, int dmgToOpponent, int healing, String actionType, String defenseType) {

    if (battle.isPlayerWon()) {
      int xpGain = 20 + gameState.getEnemyIndex() * 10;
      gameState.addXP(xpGain);
      gameState.incrementEnemiesDefeated();
      gameState.nextEnemy();
      gameState.resetPlayerHealth();

      Enemy nextEnemy = gameState.getCurrentEnemy();
      battle = new Battle(gameState.getPlayer(), nextEnemy);
      pendingEnemyDamage = 0;
      pendingActionType = null;

      CombatOutputData out = new CombatOutputData(battle.getPlayer().getHealth(), battle.getOpponent().getHealth(),
          true, false, false, dmgToPlayer, dmgToOpponent, healing, "PLAYER_ACTION_CHOICE", null, null, defenseType, 0);
      presenter.present(out);
      return;
    }

    if (battle.isPlayerLost()) {
      CombatOutputData out = new CombatOutputData(battle.getPlayer().getHealth(), battle.getOpponent().getHealth(),
          false, false, true, dmgToPlayer, dmgToOpponent, healing, "GAME_OVER", null, actionType, defenseType, 0);
      presenter.present(out);
      return;
    }
  }

  private String getPlayerActionDifficulty(String actionType) {
    if (actionType == null)
      return "EASY";
    if (actionType.equals(CombatInputData.ACTION_LIGHT))
      return "EASY";
    if (actionType.equals(CombatInputData.ACTION_HEAVY))
      return "MEDIUM";
    if (actionType.equals(CombatInputData.ACTION_HEAL))
      return "EASY";
    return "EASY";
  }
}
