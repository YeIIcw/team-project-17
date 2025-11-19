package use_case.combat;

import java.util.Random;

import entity.Battle;
import entity.Character;
import entity.Combatant;
import entity.Enemy;

public class CombatInteractor implements CombatInputBoundary {
    private final CombatOutputBoundary presenter;
    private final Combatant player;
    private final Combatant opponent;
    private final Random random;

    private Battle battle;
    private String pendingActionType;
    private int pendingEnemyDamage;

    public CombatInteractor(CombatOutputBoundary presenter, Combatant player, Combatant opponent) {
        this.presenter = presenter;
        this.player = player;
        this.opponent = opponent;
        this.random = new Random();
    }

    @Override
    public void startBattle() {
        battle = new Battle(player, opponent);
        pendingActionType = null;
        pendingEnemyDamage = 0;

        CombatOutputData outputData = new CombatOutputData(
                player.getHealth(),
                opponent.getHealth(),
                true,
                false,
                false,
                0,
                0,
                0,
                "PLAYER_ACTION_CHOICE",
                null,
                null,
                null,
                0
        );
        presenter.present(outputData);
    }

    @Override
    public void choosePlayerAction(CombatInputData inputData) {
        if (battle == null || !battle.isOngoing()) {
            return;
        }

        pendingActionType = inputData.getActionType();
        String difficulty = getPlayerActionDifficulty(pendingActionType);

        CombatOutputData outputData = new CombatOutputData(
                player.getHealth(),
                opponent.getHealth(),
                battle.isOngoing(),
                battle.isPlayerWon(),
                battle.isPlayerLost(),
                0,
                0,
                0,
                "PLAYER_ACTION_QUESTION",
                difficulty,
                pendingActionType,
                null,
                0
        );
        presenter.present(outputData);
    }

    @Override
    public void executePlayerAction(CombatInputData inputData) {
        if (battle == null || !battle.isOngoing()) {
            return;
        }

        boolean correct = inputData.isCorrect();

        int damageToPlayer = 0;
        int damageToOpponent = 0;
        int healAmount = 0;

        if (correct && pendingActionType != null) {
            if (CombatInputData.ACTION_LIGHT.equals(pendingActionType)) {
                int before = opponent.getHealth();
                opponent.takeDamage(player.getDamage());
                damageToOpponent = before - opponent.getHealth();
            } else if (CombatInputData.ACTION_HEAVY.equals(pendingActionType)) {
                int before = opponent.getHealth();
                int heavyDamage = (player.getDamage() * 3) / 2;
                opponent.takeDamage(heavyDamage);
                damageToOpponent = before - opponent.getHealth();
            } else if (CombatInputData.ACTION_HEAL.equals(pendingActionType)) {
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
            CombatOutputData outputData = new CombatOutputData(
                    player.getHealth(),
                    opponent.getHealth(),
                    false,
                    battle.isPlayerWon(),
                    battle.isPlayerLost(),
                    damageToPlayer,
                    damageToOpponent,
                    healAmount,
                    "FINISHED",
                    null,
                    pendingActionType,
                    null,
                    0
            );
            presenter.present(outputData);
            return;
        }

        int baseDamage = opponent.getDamage();
        int multiplier = random.nextInt(2) + 1;
        pendingEnemyDamage = baseDamage * multiplier;

        CombatOutputData outputData = new CombatOutputData(
                player.getHealth(),
                opponent.getHealth(),
                battle.isOngoing(),
                battle.isPlayerWon(),
                battle.isPlayerLost(),
                damageToPlayer,
                damageToOpponent,
                healAmount,
                "DEFENSE_CHOICE",
                null,
                pendingActionType,
                null,
                pendingEnemyDamage
        );
        presenter.present(outputData);
    }

    @Override
    public void defend(CombatInputData inputData) {
        if (battle == null || !battle.isOngoing()) {
            return;
        }

        String defenseType = inputData.getDefenseType();
        boolean correct = inputData.isCorrect();

        int damageToPlayer = 0;
        int damageToOpponent = 0;
        int healAmount = 0;

        if (correct) {
            if (CombatInputData.DEFENSE_COUNTER.equals(defenseType)) {
                int before = opponent.getHealth();
                opponent.takeDamage(player.getDamage());
                damageToOpponent = before - opponent.getHealth();
            }
        } else {
            int before = player.getHealth();
            player.takeDamage(pendingEnemyDamage);
            damageToPlayer = before - player.getHealth();
        }

        pendingEnemyDamage = 0;

        updateBattleState();

        if (!battle.isOngoing()) {
            CombatOutputData outputData = new CombatOutputData(
                    player.getHealth(),
                    opponent.getHealth(),
                    false,
                    battle.isPlayerWon(),
                    battle.isPlayerLost(),
                    damageToPlayer,
                    damageToOpponent,
                    healAmount,
                    "FINISHED",
                    null,
                    pendingActionType,
                    defenseType,
                    0
            );
            presenter.present(outputData);
            return;
        }

        CombatOutputData outputData = new CombatOutputData(
                player.getHealth(),
                opponent.getHealth(),
                battle.isOngoing(),
                battle.isPlayerWon(),
                battle.isPlayerLost(),
                damageToPlayer,
                damageToOpponent,
                healAmount,
                "PLAYER_ACTION_CHOICE",
                null,
                pendingActionType,
                defenseType,
                0
        );
        presenter.present(outputData);
    }

    private void updateBattleState() {
        if (opponent.isDead()) {
            battle.setOngoing(false);
            battle.setPlayerWon(true);
            battle.setPlayerLost(false);
        } else if (player.isDead()) {
            battle.setOngoing(false);
            battle.setPlayerWon(false);
            battle.setPlayerLost(true);
        }
    }

    private String getPlayerActionDifficulty(String actionType) {
        if (CombatInputData.ACTION_LIGHT.equals(actionType)) {
            return "EASY";
        } else if (CombatInputData.ACTION_HEAVY.equals(actionType)) {
            return "MEDIUM";
        } else if (CombatInputData.ACTION_HEAL.equals(actionType)) {
            return "EASY";
        }
        return "EASY";
    }
}
