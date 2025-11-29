package use_case.login;

import entity.Player;

import java.io.IOException;

public class LoginInteractor implements LoginInputBoundary {

  private Player player;

  public void execute(String username, String password) throws IOException {
    player = new Player(username, password);
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayerPreferences(String category, String difficulty, String type, int numQuestions) {
    player.setCategory(category);
    player.setDifficulty(difficulty);
    player.setType(type);
    player.setNumQuestions(numQuestions);
    System.out.println(player.getCategory() + player.getDifficulty() + player.getType() + player.getNumQuestions());
  }
}
