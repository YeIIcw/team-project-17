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

// The GameplayView is being created before any questions are fetched from the API, causing freezes
// I created them in AppBuilder.addPreferencesUseCase() instead
//                    .addGameplayView()
//                    .addGameplayUseCase()

                    .build();
        });
    }
}
