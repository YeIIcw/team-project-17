package entity;

import java.util.List;

public class GameState {

//    private final Character character;
//    private final Map gameMap;
    private Position position;

    private List<Question> questions;
    private int currentQuestionIndex;

    private int score;

    private int xp = 0;
    private int enemyIndex = 1;
    private String currentUsername = "Guest";

    private Character player;
    private Enemy currentEnemy;

    private int enemiesDefeated;


//    private CombatState currentCombat;         // null when not in combat
//
//    private GamePhase phase;                   // EXPLORATION, QUESTION, COMBAT, GAME_OVER

    public GameState() {
//        this.character = character;
//        this.gameMap = gameMap;
        this.position = new Position(0, 0);
        this.currentQuestionIndex = 0;
        this.score = 0;
        // initialize player and first enemy
        this.player = new Character(100, 10, 5);    // base values
        this.currentEnemy = new Enemy(50, 5);       // first enemy base stats

    }

    // NOTE: already set in PreferencesInteractor by making API calls; thus not in the constructor
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions(){
        return questions;
    }
    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public boolean hasMoreQuestions() {
        return questions != null && currentQuestionIndex < questions.size();
    }

    public void moveToNextQuestion() {
        if (hasMoreQuestions()) {
            currentQuestionIndex++;
        }
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void reset() {
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

//    public boolean isGameOver() {
//        return phase == GamePhase.GAME_OVER
//                || character.getHealth() <= 0
//                || !hasMoreQuestions();
//    }

    // getters / setters / methods to move player, start combat, etc.
    public Character getPlayer() {
        return player;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public void addXP(int amount) {
        xp += amount;
    }

    public int getXP() {
        return xp;
    }

    public void setXP(int xp) {this.xp = xp;}

    public int nextXP() { return 20;}

    public void resetPlayerHealth() {
        player.setHealth(100);
    }

    public void nextEnemy() {
        enemyIndex++;
        int hp = 50 + enemyIndex * 30;
        int dmg = 5 + enemyIndex * 3;
        currentEnemy = new Enemy(hp, dmg);
    }

    public int getEnemyIndex() {
        return enemyIndex;
    }

    public void incrementEnemiesDefeated() {
        enemiesDefeated++;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

}
