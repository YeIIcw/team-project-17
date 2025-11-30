package use_case.preferences;

public class PreferencesOutputData {
  private final String category;
  private final String difficulty;
  private final String type;
  private final int numQuestions;
  private final boolean success;
  private final String message;

  public PreferencesOutputData(String category, String difficulty, String type, int numQuestions, boolean success,
      String message) {
    this.category = category;
    this.difficulty = difficulty;
    this.type = type;
    this.numQuestions = numQuestions;
    this.success = success;
    this.message = message;
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
  public boolean isSuccess() {
    return success;
  }
  public String getMessage() {
    return message;
  }
}
