package entity;

public class Battle {
    private Character player;
    private Enemy enemy;
    private boolean playerTurn;
    private boolean ongoing;
    private boolean playerWon;
    private boolean playerLost;

    public Battle(Character player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        playerTurn = true;
        ongoing = true;
        playerWon = false;
        playerLost = false;
    }

    public Character getPlayer() {
        return player;
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public boolean getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    public boolean getPlayerLost() {
        return playerLost;
    }

    public void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
    }
}
