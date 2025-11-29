package entity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Player {

  private final String username;
  private final String password;
  private int points;
  private String category;
  private String difficulty;
  private String type;
  private int numQuestions;

  public Player(String username, String password) {
    this.username = username;
    this.password = password;
    // changed initial points/HP so that the player can play at least 1-2 questions
    // before they die
    this.points = 500;
  }

  public boolean checkCredentials() throws IOException {
    FileReader fileReader = new FileReader(new File("src/main/java/scratch.txt"));
    String file = fileReader.toString();
    String[] lines = file.split("\n");

    for (String line : lines) {
      if (line.contains(username) && line.contains(password)) {
        return true;
      }
    }

    return false;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getPoints() {
    return points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public void subtractPoints(int points) {
    this.points -= points;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getNumQuestions() {
    return numQuestions;
  }

  public void setNumQuestions(int numQuestions) {
    this.numQuestions = numQuestions;
  }
}
