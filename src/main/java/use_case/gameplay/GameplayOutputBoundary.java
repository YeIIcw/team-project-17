package use_case.gameplay;

public interface GameplayOutputBoundary {
  void presentAnswerResult(GameplayOutputData outputData);
  void presentQuestion(GameplayOutputData outputData);
}
