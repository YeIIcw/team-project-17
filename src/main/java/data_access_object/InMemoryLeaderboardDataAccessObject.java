package data_access_object;

import entity.ScoreEntry;
import use_case.leaderboard.LeaderboardDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InMemoryLeaderboardDataAccessObject implements LeaderboardDataAccessInterface {
  private final List<ScoreEntry> scores = new ArrayList<>();
  private final String userFilePath = "src/main/java/scratch.txt";

  public InMemoryLeaderboardDataAccessObject() {
    loadUsersFromFile();
  }

  @Override
  public void saveScore(ScoreEntry newScore) {
    boolean userExists = false;
    for (int i = 0; i < scores.size(); i++) {
      ScoreEntry existing = scores.get(i);
      if (existing.getUsername().equals(newScore.getUsername())) {
        if (newScore.getScore() > existing.getScore()) {
          scores.set(i, newScore);
        }
        userExists = true;
        break;
      }
    }

    if (!userExists) {
      scores.add(newScore);
    }

    scores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
    updateFile();

  }

  @Override
  public List<ScoreEntry> getAllScores() {
    return new ArrayList<>(scores);
  }

  private void loadUsersFromFile() {
    File file = new File(userFilePath);
    if (!file.exists())
      return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line = br.readLine(); // Skip header

      while ((line = br.readLine()) != null) {
        String[] fields = line.split(", ");
        if (fields.length >= 3) {
          String username = fields[0].trim();
          int points = Integer.parseInt(fields[2].trim());
          scores.add(new ScoreEntry(username, points));
        }
      }
    } catch (IOException | NumberFormatException e) {
      // silent fail - use default scores
    }
  }

  private void updateFile() {
    try {
      List<String> lines = new ArrayList<>();

      try (BufferedReader br = new BufferedReader(new FileReader(userFilePath))) {
        String line;
        while ((line = br.readLine()) != null) {
          lines.add(line);
        }
      }

      try (PrintWriter writer = new PrintWriter(new FileWriter(userFilePath))) {
        writer.println(lines.get(0)); // Header

        for (int i = 1; i < lines.size(); i++) {
          String line = lines.get(i);
          String[] fields = line.split(", ");
          if (fields.length >= 3) {
            String username = fields[0].trim();
            String password = fields[1].trim();

            int currentScore = 0;
            for (ScoreEntry score : scores) {
              if (score.getUsername().equals(username)) {
                currentScore = score.getScore();
                break;
              }
            }

            writer.println(username + ", " + password + ", " + currentScore);
          }
        }
      }
    } catch (IOException e) {
      // silent fail - scores won't persist this session
    }
  }
}
