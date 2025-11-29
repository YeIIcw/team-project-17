package use_case.gameplay;

public class GameplayInputData {
  private final int selectedChoiceIndex;

  public GameplayInputData(int selectedChoiceIndex) {
    this.selectedChoiceIndex = selectedChoiceIndex;
  }

  public int getSelectedChoiceIndex() {
    return selectedChoiceIndex;
  }
}
