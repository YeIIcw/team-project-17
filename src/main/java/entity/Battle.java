package entity;

public class Battle {
    private Combatant player;
    private Combatant opponent;
    private boolean playerTurn;
    private boolean ongoing;
    private boolean playerWon;
    private boolean playerLost;

    public Battle(Combatant player, Combatant opponent) {
        this.player = player;
        this.opponent = opponent;
        this.playerTurn = true;
        this.ongoing = true;
        this.playerWon = false;
        this.playerLost = false;
    }

    public Combatant getPlayer() {
        return player;
    }

    public void setPlayer(Combatant player) {
        this.player = player;
    }

    public Combatant getOpponent() {
        return opponent;
    }

    public void setOpponent(Combatant opponent) {
        this.opponent = opponent;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
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
}
