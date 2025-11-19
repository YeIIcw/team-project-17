package app;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder appBuilder = new AppBuilder();

            appBuilder
                    .addGameState()

                    .addHomeScreenView()
                    .addHomeScreenUseCase()

                    .addSignupView()
                    .addSignupUseCase()

                    .addLoginView()
                    .addLoginUseCase()

                    .addAccountCreatedView()
                    .addAccountCreatedUseCase()

                    .addPreferencesView()
                    .addPreferencesUseCase()

                    .addLoggedInUseCase()
                    .addLoggedInView()

                    .build();
        });
    }
}
