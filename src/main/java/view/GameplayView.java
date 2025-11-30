package view;

import entity.Question;
import interface_adapter.Gameplay.GameplayController;
import interface_adapter.Gameplay.GameplayViewModel;
import interface_adapter.Combat.CombatController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameplayView {

  private final GameplayViewModel gameplayViewModel;
  private final GameplayController gameplayController;

  private CombatController combatController;
  private boolean answeringForCombat = false;

  private final JFrame frame = new JFrame("Trivia Game");
  private final JLabel questionLabel = new JLabel("Loading question...");
  private final JPanel choicesPanel = new JPanel();
  private final JLabel resultLabel = new JLabel();
  private final JLabel scoreLabel = new JLabel("Total Score: 0");
  private final JButton nextButton = new JButton("Next Question");
  private final JButton finishButton = new JButton("Finish Game");

  private JButton[] choiceButtons;
  private boolean answerSubmitted = false;

  public GameplayView(GameplayViewModel gameplayViewModel, GameplayController gameplayController) {
    this.gameplayViewModel = gameplayViewModel;
    this.gameplayController = gameplayController;
    setupUI();
  }

  public void setCombatController(CombatController combatController) {
    this.combatController = combatController;
  }

  public void askCombatQuestion(String difficulty) {
    answeringForCombat = true;
    frame.setTitle("Combat Question - " + difficulty);
    answerSubmitted = false;
    displayQuestion();
    frame.setVisible(true);
  }

  private void setupUI() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    JPanel questionPanel = new JPanel(new BorderLayout());
    questionPanel.setBorder(BorderFactory.createTitledBorder("Question"));

    questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
    questionLabel.setVerticalAlignment(SwingConstants.CENTER);
    questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    questionLabel.setPreferredSize(new Dimension(500, 100));
    questionPanel.add(questionLabel, BorderLayout.CENTER);

    choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
    choicesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    JPanel resultPanel = new JPanel();
    resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    resultPanel.add(resultLabel);

    scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JPanel scorePanel = new JPanel();
    scorePanel.add(scoreLabel);
    questionPanel.add(scorePanel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    nextButton.setEnabled(false);
    nextButton.setVisible(false);
    finishButton.setVisible(false);

    frame.add(questionPanel, BorderLayout.NORTH);
    frame.add(choicesPanel, BorderLayout.CENTER);
    frame.add(resultPanel, BorderLayout.SOUTH);
    frame.add(buttonPanel, BorderLayout.PAGE_END);

    frame.setSize(750, 500);
    frame.setLocationRelativeTo(null);
  }

  private void displayQuestion() {
    gameplayController.getNextQuestion();
    Question currentQuestion = gameplayViewModel.getCurrentQuestion();

    if (currentQuestion == null) {
      questionLabel.setText("No questions available.");
      return;
    }

    String questionText = currentQuestion.getText().replace("&quot;", "\"").replace("&#039;", "'").replace("&amp;", "&")
        .replace("&lt;", "<").replace("&gt;", ">");

    questionLabel.setText(
        "<html><div style='text-align: center; width: 500px; padding: 10px;'>" + questionText + "</div></html>");

    choicesPanel.removeAll();
    List<String> choices = currentQuestion.getChoices();
    choiceButtons = new JButton[choices.size()];

    for (int i = 0; i < choices.size(); i++) {
      final int choiceIndex = i;
      JButton choiceButton = new JButton(choices.get(i));
      choiceButton.setPreferredSize(new Dimension(500, 50));
      choiceButton.setFont(new Font("Arial", Font.PLAIN, 12));
      choiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);

      choiceButton.addActionListener(e -> {
        if (!answerSubmitted) {
          answerSubmitted = true;

          Question q = gameplayViewModel.getCurrentQuestion();
          boolean correct = (choiceIndex == q.getCorrectChoiceIndex());

          if (combatController != null && answeringForCombat) {
            gameplayController.submitAnswer(choiceIndex);
            combatController.answerFromQuestion(correct);
            answeringForCombat = false;
            frame.setVisible(false);
            return;
          }

          for (JButton btn : choiceButtons) {
            btn.setEnabled(false);
          }
          gameplayController.submitAnswer(choiceIndex);
          showResult();
        }
      });

      choiceButtons[i] = choiceButton;
      choicesPanel.add(choiceButton);
      choicesPanel.add(Box.createVerticalStrut(10));
    }

    scoreLabel.setText("Total Score: " + gameplayViewModel.getTotalScore());

    frame.revalidate();
    frame.repaint();
  }

  private void showResult() {
    boolean isCorrect = gameplayViewModel.isCorrect();
    int questionScore = gameplayViewModel.getQuestionScore();
    String message = gameplayViewModel.getMessage();

    resultLabel.setText(message);
    resultLabel.setForeground(isCorrect ? Color.GREEN : Color.RED);

    Question currentQuestion = gameplayViewModel.getCurrentQuestion();
    int correctIndex = currentQuestion.getCorrectChoiceIndex();
    choiceButtons[correctIndex].setBackground(Color.GREEN);
    choiceButtons[correctIndex].setOpaque(true);

    scoreLabel.setText("Total Score: " + gameplayViewModel.getTotalScore());

    if (!gameplayViewModel.hasMoreQuestions()) {
      resultLabel.setText(message + " Game Over!");
    }
  }

  public void display() {
    displayQuestion();
    frame.setVisible(true);
  }
}
