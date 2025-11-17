package app;

import javax.swing.*;
import view.HomeScreenView;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        appBuilder.addHomeScreenView();
        appBuilder.addHomeScreenUseCase();
        appBuilder.addSignupView();
        appBuilder.addSignupUseCase();
        appBuilder.addLoginView();
        appBuilder.addLoginUseCase();
        appBuilder.addAccountCreatedView();
        appBuilder.addAccountCreatedUseCase();
        appBuilder.addLoggedInView();
        appBuilder.addLoggedInUseCase();
        appBuilder.addPreferencesView();
        appBuilder.addPreferencesUseCase();

        appBuilder.build();
    }
}
