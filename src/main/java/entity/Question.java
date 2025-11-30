package entity;

import java.util.ArrayList;
import java.util.List;

public class Question {
  private final String type;
  private final String text;
  private final List<String> choices;
  private final int correctChoiceIndex;
  private final int scoreValue;

  public Question(String type, String text, ArrayList<String> choices, int correctChoiceIndex, int scoreValue) {
    this.type = type;
    this.text = text;
    this.choices = new ArrayList<>(choices);
    this.correctChoiceIndex = correctChoiceIndex;
    this.scoreValue = scoreValue;
  }

  public String getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  public List<String> getChoices() {
    return new ArrayList<>(choices);
  }

  public int getCorrectChoiceIndex() {
    return correctChoiceIndex;
  }

  public int getScoreValue() {
    return scoreValue;
  }
}
