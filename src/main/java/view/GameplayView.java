package view;

import entity.Question;
import interface_adapter.Gameplay.GameplayController;
import interface_adapter.Gameplay.GameplayViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameplayView {

    private final GameplayViewModel gameplayViewModel;
    private final GameplayController gameplayController;

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
        displayQuestion();
    }

    private void setupUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Question panel - contains both score and question
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createTitledBorder("Question"));
        
        // Question label
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionLabel.setPreferredSize(new Dimension(500, 100)); // Ensure it has size
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        // Choices panel
        choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
        choicesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Result panel
        JPanel resultPanel = new JPanel();
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resultPanel.add(resultLabel);

        // Score panel - put it in the question panel
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel scorePanel = new JPanel();
        scorePanel.add(scoreLabel);
        questionPanel.add(scorePanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> {
            if (gameplayViewModel.hasMoreQuestions()) {
                answerSubmitted = false;
                nextButton.setEnabled(false);
                resultLabel.setText("");
                displayQuestion();
            }
        });

        finishButton.addActionListener(e -> {
            frame.dispose();
            // Could navigate to results screen here
        });

        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);

        // Add components to frame
        frame.add(questionPanel, BorderLayout.NORTH);
        frame.add(choicesPanel, BorderLayout.CENTER);
        frame.add(resultPanel, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
    }

    private void displayQuestion() {
        System.out.println("DEBUG: GameplayView - displayQuestion() called");
        
        // Always get the question from the controller/interactor
        // This ensures we're getting it from GameState
        gameplayController.getNextQuestion();
        Question currentQuestion = gameplayViewModel.getCurrentQuestion();

        System.out.println("DEBUG: GameplayView - Current question from ViewModel: " + 
            (currentQuestion != null ? currentQuestion.getText() : "null"));

        if (currentQuestion == null) {
            System.out.println("DEBUG: GameplayView - No question available, showing error message");
            questionLabel.setText("No questions available.");
            return;
        }
        
        System.out.println("DEBUG: GameplayView - Displaying question: " + currentQuestion.getText());

        // Display question text (decode HTML entities if needed)
        String questionText = currentQuestion.getText()
            .replace("&quot;", "\"")
            .replace("&#039;", "'")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">");
        
        questionLabel.setText("<html><div style='text-align: center; width: 500px; padding: 10px;'>" + 
                             questionText + "</div></html>");
        questionLabel.setVisible(true);
        questionLabel.setOpaque(true);
        questionLabel.setBackground(frame.getBackground());
        
        System.out.println("DEBUG: GameplayView - Question label text set to: " + questionText);
        System.out.println("DEBUG: GameplayView - Question label visible: " + questionLabel.isVisible());
        System.out.println("DEBUG: GameplayView - Question label text: " + questionLabel.getText());

        // Clear previous choices
        choicesPanel.removeAll();
        List<String> choices = currentQuestion.getChoices();
        System.out.println("DEBUG: GameplayView - Question has " + choices.size() + " choices");
        choiceButtons = new JButton[choices.size()];

        // Create choice buttons
        for (int i = 0; i < choices.size(); i++) {
            final int choiceIndex = i;
            JButton choiceButton = new JButton(choices.get(i));
            choiceButton.setPreferredSize(new Dimension(500, 50));
            choiceButton.setFont(new Font("Arial", Font.PLAIN, 12));
            choiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            choiceButton.addActionListener(e -> {
                if (!answerSubmitted) {
                    answerSubmitted = true;
                    // Disable all buttons
                    for (JButton btn : choiceButtons) {
                        btn.setEnabled(false);
                    }
                    // Submit answer
                    gameplayController.submitAnswer(choiceIndex);
                    // Show result
                    showResult();
                }
            });
            
            choiceButtons[i] = choiceButton;
            choicesPanel.add(choiceButton);
            choicesPanel.add(Box.createVerticalStrut(10));
        }

        // Update score display
        scoreLabel.setText("Total Score: " + gameplayViewModel.getTotalScore());

        frame.revalidate();
        frame.repaint();
    }

    private void showResult() {
        boolean isCorrect = gameplayViewModel.isCorrect();
        int questionScore = gameplayViewModel.getQuestionScore();
        String message = gameplayViewModel.getMessage();

        resultLabel.setText(message);
        
        if (isCorrect) {
            resultLabel.setForeground(Color.GREEN);
        } else {
            resultLabel.setForeground(Color.RED);
        }

        // Highlight correct answer
        Question currentQuestion = gameplayViewModel.getCurrentQuestion();
        int correctIndex = currentQuestion.getCorrectChoiceIndex();
        choiceButtons[correctIndex].setBackground(Color.GREEN);
        choiceButtons[correctIndex].setOpaque(true);

        // Update total score
        scoreLabel.setText("Total Score: " + gameplayViewModel.getTotalScore());

        // Enable next button if there are more questions
        if (gameplayViewModel.hasMoreQuestions()) {
            nextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
            resultLabel.setText(message + " Game Over!");
        }
    }

    public void display() {
        // Reload question when view is displayed (in case questions were loaded after view creation)
        System.out.println("DEBUG: GameplayView - display() called, reloading question");
        displayQuestion();
        frame.setVisible(true);
    }
}

