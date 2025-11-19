package view;

import interface_adapter.Preferences.PreferencesController;
import interface_adapter.Preferences.PreferencesViewModel;

import javax.swing.*;

public class PreferencesView {

    private final PreferencesViewModel preferencesViewModel;
    private PreferencesController preferencesController;

    private final JFrame frame = new JFrame();

    private final JLabel categoryLabel = new JLabel("Category:");
    private final JLabel difficultyLabel = new JLabel("Difficulty");
    private final JLabel typeLabel = new JLabel("Type:");
    private final JLabel numQuestionLabel = new JLabel("Number of Questions:");

    String[] categories = {"General Knowledge", "Entertainment: Books", "Entertainment: Film", "Entertainment: Music",
            "Entertainment: Musicals & Theatres", "Entertainment: Television", "Entertainment: Video Games",
            "Entertainment: Board Games", "Science & Nature", "Science: Computers", "Science: Mathematics, ",
            "Mythology", "Sports", "Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
            "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};
    String[] difficulties = {"Easy", "Medium", "Hard"};
    String[] types = {"Multiple Choice", "True/False"};
    String[] numQuestions = {"10", "20", "30", "40", "50"};

    private final JComboBox<String> categoriesDropdown = new JComboBox<>(categories);
    private final JComboBox<String> difficultyDropdown = new JComboBox<>(difficulties);
    private final JComboBox<String> typesDropdown = new JComboBox<>(types);
    private final JComboBox<String> numQuestionDropdown = new JComboBox<>(numQuestions);

    private final JButton doneButton = new JButton("Done");

    public PreferencesView(PreferencesViewModel preferencesViewModel) {
        this.preferencesViewModel = preferencesViewModel;

        JPanel category = new JPanel();
        category.add(categoryLabel);
        category.add(categoriesDropdown);

        JPanel difficulty = new JPanel();
        difficulty.add(difficultyLabel);
        difficulty.add(difficultyDropdown);

        JPanel type = new JPanel();
        type.add(typeLabel);
        type.add(typesDropdown);

        JPanel question = new JPanel();
        question.add(numQuestionLabel);
        question.add(numQuestionDropdown);

        JPanel done = new JPanel();
        done.add(doneButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(category);
        mainPanel.add(difficulty);
        mainPanel.add(type);
        mainPanel.add(question);
        mainPanel.add(done);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();

        doneButton.addActionListener(e -> {
            String categoryChoice   = (String) categoriesDropdown.getSelectedItem();
            String difficultyChoice = (String) difficultyDropdown.getSelectedItem();
            String typeChoice       = (String) typesDropdown.getSelectedItem();
            int numQuestionChoice   = Integer.parseInt((String) numQuestionDropdown.getSelectedItem());

            // ViewModel can be updated here
            preferencesViewModel.setCategory(categoryChoice);
            preferencesViewModel.setDifficulty(difficultyChoice);
            preferencesViewModel.setType(typeChoice);
            preferencesViewModel.setNumQuestions(numQuestionChoice);

            // Use injected controller
            if (preferencesController != null) {
                preferencesController.execute(
                        categoryChoice,
                        difficultyChoice,
                        typeChoice,
                        numQuestionChoice
                );
            }

            // optional: inspect ViewModel for success / error
            if (!preferencesViewModel.isSuccess() && preferencesViewModel.getMessage() != null) {
                JOptionPane.showMessageDialog(frame, preferencesViewModel.getMessage());
            } else {
                frame.dispose(); // move to next screen
            }
        });
    }

    public void setPreferencesController(PreferencesController controller) {
        this.preferencesController = controller;
    }

    public void display() {
        frame.setVisible(true);
    }
}
