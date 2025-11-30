package view;

import interface_adapter.Combat.CombatController;
import interface_adapter.Combat.CombatViewModel;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class CombatView extends JPanel {

  private final CombatController controller;
  private final CombatViewModel viewModel;

  private Runnable onQuestionRequested;

  private JFrame frame;

  private JLabel playerHealthLabel;
  private JLabel opponentHealthLabel;
  private JLabel playerDamageLabel;
  private JLabel playerHealLabel;

  private JLabel statusLabel;
  private JLabel phaseLabel;
  private JLabel difficultyLabel;

  private JButton lightAttackButton;
  private JButton heavyAttackButton;
  private JButton healButton;

  private JButton dodgeButton;
  private JButton counterButton;

  public CombatView(CombatController controller, CombatViewModel viewModel) {
    this.controller = controller;
    this.viewModel = viewModel;

    this.frame = new JFrame("Combat");

    initComponents();
    layoutComponents();
    wireActionButtons();

    frame.add(this);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
  }

  public void display() {
    refreshFromViewModel();
    frame.setVisible(true);
  }

  public void setVisibleFalse() {
    frame.setVisible(false);
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(600, 400));

    playerHealthLabel = new JLabel();
    opponentHealthLabel = new JLabel();
    playerDamageLabel = new JLabel();
    playerHealLabel = new JLabel();

    Font statFont = new Font("SansSerif", Font.BOLD, 14);
    playerHealthLabel.setFont(statFont);
    opponentHealthLabel.setFont(statFont);
    playerDamageLabel.setFont(statFont);
    playerHealLabel.setFont(statFont);

    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    statusLabel.setForeground(new Color(30, 30, 30));

    phaseLabel = new JLabel("", JLabel.CENTER);
    phaseLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

    difficultyLabel = new JLabel("", JLabel.CENTER);
    difficultyLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    difficultyLabel.setForeground(new Color(80, 80, 80));

    lightAttackButton = new JButton("Light Attack");
    heavyAttackButton = new JButton("Heavy Attack");
    healButton = new JButton("Heal");

    dodgeButton = new JButton("Dodge");
    counterButton = new JButton("Counter");
  }

  private void layoutComponents() {
    JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 5));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    statsPanel.add(playerHealthLabel);
    statsPanel.add(opponentHealthLabel);
    statsPanel.add(playerDamageLabel);
    statsPanel.add(playerHealLabel);

    JPanel centerPanel = new JPanel(new GridLayout(3, 1));
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    centerPanel.add(statusLabel);
    centerPanel.add(phaseLabel);
    centerPanel.add(difficultyLabel);

    JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 0));
    actionPanel.setBorder(BorderFactory.createTitledBorder("Choose Action"));
    actionPanel.add(lightAttackButton);
    actionPanel.add(heavyAttackButton);
    actionPanel.add(healButton);

    JPanel defensePanel = new JPanel(new GridLayout(1, 2, 10, 0));
    defensePanel.setBorder(BorderFactory.createTitledBorder("Defense"));
    defensePanel.add(dodgeButton);
    defensePanel.add(counterButton);

    JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 5));
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    bottomPanel.add(actionPanel);
    bottomPanel.add(defensePanel);

    add(statsPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  private void wireActionButtons() {
    // Attacks: same as before
    lightAttackButton.addActionListener(e -> controller.chooseLightAttack());
    heavyAttackButton.addActionListener(e -> controller.chooseHeavyAttack());
    healButton.addActionListener(e -> controller.chooseHeal());

    // DEFENSE: mark pending defense + open trivia question
    dodgeButton.addActionListener(e -> {
      controller.markPendingDodge();
      if (onQuestionRequested != null) {
        onQuestionRequested.run();
      }
    });

    counterButton.addActionListener(e -> {
      controller.markPendingCounter();
      if (onQuestionRequested != null) {
        onQuestionRequested.run();
      }
    });
  }

  // still available if something else wants to override listeners
  public void setDodgeActionListener(ActionListener listener) {
    for (ActionListener l : dodgeButton.getActionListeners()) {
      dodgeButton.removeActionListener(l);
    }
    dodgeButton.addActionListener(listener);
  }

  public void setCounterActionListener(ActionListener listener) {
    for (ActionListener l : counterButton.getActionListeners()) {
      counterButton.removeActionListener(l);
    }
    counterButton.addActionListener(listener);
  }

  public void refreshFromViewModel() {
    int playerHp = viewModel.getPlayerHealth();
    int opponentHp = viewModel.getOpponentHealth();
    String phase = viewModel.getNextPhase();

    if ("GAME_OVER".equals(phase)) {
      setVisibleFalse();
      return;
    }

    int dmgToEnemyRaw = viewModel.getDamageToOpponent();
    int dmgToPlayer = viewModel.getDamageToPlayer();
    int heal = viewModel.getHealAmount();

    playerHealthLabel.setText("Player HP: " + playerHp);
    opponentHealthLabel.setText("Enemy HP: " + opponentHp);
    playerDamageLabel.setText("Damage: " + Math.max(0, dmgToEnemyRaw) + " dealt last turn");
    playerHealLabel.setText("Heal: " + heal + " last turn");

    phaseLabel.setText("Phase: " + phase);

    String diff = viewModel.getQuestionDifficulty();
    if (diff != null) {
      difficultyLabel.setText("Question difficulty: " + diff);
    } else {
      difficultyLabel.setText("");
    }

    String message = "";

    if (!viewModel.isOngoing()) {
      if (viewModel.isPlayerWon()) {
        message = "You won the battle!";
      } else if (viewModel.isPlayerLost()) {
        message = "You died.";
      }
    } else {
      if ("PLAYER_ACTION_QUESTION".equals(phase)) {
        message = "Answer the question to perform your action.";
        if (onQuestionRequested != null) {
          onQuestionRequested.run();
        }
      } else if ("DEFENSE_CHOICE".equals(phase)) {
        message = "Enemy is attacking for " + viewModel.getPendingEnemyDamage() + " damage. Choose a defense.";
      } else if ("DEFENSE_QUESTION".equals(phase)) {
        message = "Answer the question to resolve your defense.";
      } else if (dmgToEnemyRaw == -1) {
        message = "Heavy attack missed!";
      } else if (dmgToEnemyRaw == -2) {
        message = "Counter failed! You took " + dmgToPlayer + " damage.";
      } else if (dmgToEnemyRaw > 0) {
        message = "Dealt " + dmgToEnemyRaw + " damage.";
      } else if (heal > 0) {
        message = "Healed " + heal + " HP.";
      } else if (dmgToPlayer > 0) {
        message = "You took " + dmgToPlayer + " damage.";
      }
    }

    statusLabel.setText(message);

    boolean enableActions = "PLAYER_ACTION_CHOICE".equals(phase) && viewModel.isOngoing();
    lightAttackButton.setEnabled(enableActions);
    heavyAttackButton.setEnabled(enableActions);
    healButton.setEnabled(enableActions);

    boolean enableDefense = "DEFENSE_CHOICE".equals(phase) && viewModel.isOngoing();
    dodgeButton.setEnabled(enableDefense);
    counterButton.setEnabled(enableDefense);
  }

  public void setOnQuestionRequested(Runnable r) {
    this.onQuestionRequested = r;
  }
}
