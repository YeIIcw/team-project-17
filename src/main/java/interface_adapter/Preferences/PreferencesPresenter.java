package interface_adapter.Preferences;

import use_case.preferences.PreferencesOutputBoundary;
import use_case.preferences.PreferencesOutputData;

public class PreferencesPresenter implements PreferencesOutputBoundary {

    private final PreferencesViewModel viewModel;

    public PreferencesPresenter(PreferencesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(PreferencesOutputData outputData) {
        viewModel.setCategory(outputData.getCategory());
        viewModel.setDifficulty(outputData.getDifficulty());
        viewModel.setType(outputData.getType());
        viewModel.setNumQuestions(outputData.getNumQuestions());
        viewModel.setSuccess(outputData.isSuccess());
        viewModel.setMessage(outputData.getMessage());
    }
}
