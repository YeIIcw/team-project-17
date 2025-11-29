package use_case.gameplay;

public interface GameplayInputBoundary {
  void submitAnswer(GameplayInputData inputData);
  void getNextQuestion();
}
