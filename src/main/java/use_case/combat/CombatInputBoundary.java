package use_case.combat;

public interface CombatInputBoundary {
  void startBattle();
  void choosePlayerAction(CombatInputData inputData);
  void executePlayerAction(CombatInputData inputData);
  void defend(CombatInputData inputData);
}
