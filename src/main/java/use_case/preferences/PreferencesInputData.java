package use_case.preferences;

public class PreferencesInputData {
  private final String category;
  private final String difficulty;
  private final String type;
  private final int numQuestions;

  public PreferencesInputData(String category, String difficulty, String type, int numQuestions) {
    this.category = category;
    this.difficulty = difficulty;
    this.type = type;
    this.numQuestions = numQuestions;
  }

  public String getCategory() {
    return category;
  }
  public String getDifficulty() {
    return difficulty;
  }
  public String getType() {
    return type;
  }
  public int getNumQuestions() {
    return numQuestions;
  }
}
