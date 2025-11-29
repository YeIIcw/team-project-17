package app;

import javax.swing.SwingUtilities;

public class Main {

  // Global reference so GameOverView can restart the game
  public static AppBuilder builder;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      Main.builder = new AppBuilder();

      Main.builder.addGameState()

          .addHomeScreenView().addHomeScreenUseCase()

          .addSignupView().addSignupUseCase()

          .addLoginView().addLoginUseCase()

          .addAccountCreatedView().addAccountCreatedUseCase()

          .addPreferencesView().addPreferencesUseCase()

          .addLoggedInUseCase().addLoggedInView()

          .addGameplayView().addGameplayUseCase()

          .addLeaderboardUseCase()

          .addCombatUseCase()

          .build();
    });
  }
}
