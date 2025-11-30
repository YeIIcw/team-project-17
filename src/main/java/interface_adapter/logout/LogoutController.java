package interface_adapter.logout;

import interface_adapter.HomeScreen.HomeScreenViewModel;
import view.HomeScreenView;

public class LogoutController {

  public void execute() {
    // LoggedInView loggedInView = new LoggedInView(new LoggedInViewModel());

    HomeScreenView homeScreenView = new HomeScreenView(new HomeScreenViewModel());
    homeScreenView.display();
  }
}
