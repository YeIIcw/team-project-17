package interface_adapter.Combat;

import use_case.combat.CombatInputBoundary;
import use_case.combat.CombatInputData;

public class CombatController {
  private final CombatInputBoundary interactor;

  // NEW FIELD
  private String pendingDefense = null;

  public CombatController(CombatInputBoundary interactor) {
    this.interactor = interactor;
  }

  public void startBattle() {
    interactor.startBattle();
  }

  public void chooseLightAttack() {
    CombatInputData inputData = new CombatInputData(CombatInputData.ACTION_LIGHT, null, false);
    interactor.choosePlayerAction(inputData);
  }

  public void chooseHeavyAttack() {
    CombatInputData inputData = new CombatInputData(CombatInputData.ACTION_HEAVY, null, false);
    interactor.choosePlayerAction(inputData);
  }

  public void chooseHeal() {
    CombatInputData inputData = new CombatInputData(CombatInputData.ACTION_HEAL, null, false);
    interactor.choosePlayerAction(inputData);
  }

  // ===========================
  // DIRECT EXECUTION
  // ===========================
  public void executePlayerAction(boolean correct) {
    CombatInputData inputData = new CombatInputData(null, null, correct);
    interactor.executePlayerAction(inputData);
  }

  public void defendWithDodge(boolean correct) {
    CombatInputData inputData = new CombatInputData(null, CombatInputData.DEFENSE_DODGE, correct);
    interactor.defend(inputData);
  }

  public void defendWithCounter(boolean correct) {
    CombatInputData inputData = new CombatInputData(null, CombatInputData.DEFENSE_COUNTER, correct);
    interactor.defend(inputData);
  }

  // ===========================
  // NEW COMBAT/QUESTION BRIDGE
  // ===========================
  public void markPendingDodge() {
    pendingDefense = CombatInputData.DEFENSE_DODGE;
  }

  public void markPendingCounter() {
    pendingDefense = CombatInputData.DEFENSE_COUNTER;
  }

  public void answerFromQuestion(boolean correct) {
    if (pendingDefense != null) {
      if (pendingDefense.equals(CombatInputData.DEFENSE_DODGE)) {
        defendWithDodge(correct);
      } else {
        defendWithCounter(correct);
      }
      pendingDefense = null;
    } else {
      executePlayerAction(correct);
    }
  }
}
