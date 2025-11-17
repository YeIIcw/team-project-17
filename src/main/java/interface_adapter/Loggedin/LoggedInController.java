package interface_adapter.Loggedin;


import interface_adapter.Preferences.PreferencesViewModel;
import view.PreferencesView;

public class LoggedInController {

    public void goToPreferences() {
        PreferencesView preferencesView = new PreferencesView(new PreferencesViewModel());

        preferencesView.display();
    }

    public void goToStats() {

    }
}
