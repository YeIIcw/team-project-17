package interface_adapter.Loggedin;


import interface_adapter.Preferences.PreferencesViewModel;
import view.PreferencesView;

public class LoggedInController {

    private final PreferencesView preferencesView;

    public LoggedInController(PreferencesView preferencesView) {
        this.preferencesView = preferencesView;
    }

    public void goToPreferences() {
        preferencesView.display();
    }

    public void goToStats() {

    }
}
