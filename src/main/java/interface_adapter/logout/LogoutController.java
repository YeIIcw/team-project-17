package interface_adapter.Logout;

import interface_adapter.HomeScreen.HomeScreenViewModel;
import interface_adapter.Loggedin.LoggedInViewModel;
import view.HomeScreenView;
import view.LoggedInView;

public class LogoutController {

    public void execute() {
//        LoggedInView loggedInView = new LoggedInView(new LoggedInViewModel());

        HomeScreenView homeScreenView = new HomeScreenView(new HomeScreenViewModel());
        homeScreenView.display();
    }
}
