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

    // REMOVED: Number of questions label + dropdown
    // private final JLabel numQuestionLabel = new JLabel("Number of Questions:");

    String[] categories = {"General Knowledge", "Entertainment: Books", "Entertainment: Film", "Entertainment: Music",
            "Entertainment: Musicals & Theatres", "Entertainment: Television", "Entertainment: Video Games",
            "Entertainment: Board Games", "Science & Nature", "Science: Computers", "Science: Mathematics",
            "Mythology", "Sports", "Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
            "Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};

    String[] difficulties = {"Easy", "Medium", "Hard"};
    String[] types = {"Multiple Choice", "True/False"};

    private final JComboBox<String> categoriesDropdown = new JComboBox<>(categories);
    private final JComboBox<String> difficultyDropdown = new JComboBox<>(difficulties);
    private final JComboBox<String> typesDropdown = new JComboBox<>(types);

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

        // REMOVED question panel
        // JPanel question = new JPanel();
        // question.add(numQuestionLabel);
        // question.add(numQuestionDropdown);

        JPanel done = new JPanel();
        done.add(doneButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(category);
        mainPanel.add(difficulty);
        mainPanel.add(type);

        // REMOVED: mainPanel.add(question);

        mainPanel.add(done);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();

        doneButton.addActionListener(e -> {
            System.out.println("DEBUG: PreferencesView - Done button clicked");

            String categoryChoice   = (String) categoriesDropdown.getSelectedItem();
            String difficultyChoice = (String) difficultyDropdown.getSelectedItem();
            String typeChoice       = (String) typesDropdown.getSelectedItem();

            System.out.println("DEBUG: PreferencesView - Selected preferences:");
            System.out.println("  Category: " + categoryChoice);
            System.out.println("  Difficulty: " + difficultyChoice);
            System.out.println("  Type: " + typeChoice);
            System.out.println("  Number of Questions: (IGNORED → 50 MAX)");

            // Update ViewModel
            preferencesViewModel.setCategory(categoryChoice);
            preferencesViewModel.setDifficulty(difficultyChoice);
            preferencesViewModel.setType(typeChoice);
            preferencesViewModel.setNumQuestions(50); // always max

            if (preferencesController != null) {
                System.out.println("DEBUG: PreferencesView - Calling PreferencesController.execute()");
                preferencesController.execute(
                        categoryChoice,
                        difficultyChoice,
                        typeChoice,
                        50 // always max
                );
                System.out.println("DEBUG: PreferencesView - PreferencesController.execute() completed");
            } else {
                System.out.println("ERROR: PreferencesView - preferencesController is null!");
            }

            System.out.println("DEBUG: PreferencesView - Waiting for API response...");
        });
    }

    public void setPreferencesController(PreferencesController controller) {
        this.preferencesController = controller;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void display() {
        frame.setVisible(true);
    }
}
