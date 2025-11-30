package interface_adapter.Preferences;

import use_case.preferences.PreferencesOutputBoundary;
import use_case.preferences.PreferencesOutputData;

public class PreferencesPresenter implements PreferencesOutputBoundary {

  private final PreferencesViewModel viewModel;
  private Runnable onSuccessCallback;

  public PreferencesPresenter(PreferencesViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public void setOnSuccessCallback(Runnable callback) {
    this.onSuccessCallback = callback;
  }

  @Override
  public void present(PreferencesOutputData outputData) {
    System.out.println("DEBUG: PreferencesPresenter - present() called");
    System.out.println("  Success: " + outputData.isSuccess());
    System.out.println("  Message: " + outputData.getMessage());

    viewModel.setCategory(outputData.getCategory());
    viewModel.setDifficulty(outputData.getDifficulty());
    viewModel.setType(outputData.getType());
    viewModel.setNumQuestions(outputData.getNumQuestions());
    viewModel.setSuccess(outputData.isSuccess());
    viewModel.setMessage(outputData.getMessage());

    // Navigate to gameplay view on success
    if (outputData.isSuccess()) {
      System.out.println("DEBUG: PreferencesPresenter - Questions fetched successfully");
      if (onSuccessCallback != null) {
        System.out.println("DEBUG: PreferencesPresenter - Calling success callback");
        onSuccessCallback.run();
        System.out.println("DEBUG: PreferencesPresenter - Success callback completed");
      } else {
        System.out.println("ERROR: PreferencesPresenter - onSuccessCallback is null!");
      }
    } else {
      System.out.println("ERROR: PreferencesPresenter - Failed to fetch questions: " + outputData.getMessage());
    }
  }
}
