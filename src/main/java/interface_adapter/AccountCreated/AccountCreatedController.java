package interface_adapter.AccountCreated;

import interface_adapter.HomeScreen.HomeScreenViewModel;
import view.AccountCreatedView;
import view.HomeScreenView;

public class AccountCreatedController {

    public void execute() {
        AccountCreatedView accountCreatedView = new AccountCreatedView(new AccountCreatedViewModel());

        HomeScreenView homeScreenView = new HomeScreenView(new HomeScreenViewModel());
        homeScreenView.display();
    }
}
