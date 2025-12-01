package view;

import interface_adapter.LevelUp.LevelUpViewCallback;
import interface_adapter.LevelUp.LevelUpController;
import interface_adapter.LevelUp.LevelUpViewModel;

import javax.swing.*;
import java.awt.*;

public class LevelUpView extends JFrame implements LevelUpViewCallback {

    private final JLabel healthLabel;
    private final JLabel damageLabel;
    private LevelUpController controller;

    public LevelUpView() {
        super("Level Up");
        healthLabel = new JLabel("Health: 0");
        damageLabel = new JLabel("Damage: 0");
        JButton healthButton = new JButton("Increase Health");
        JButton damageButton = new JButton("Increase Damage");

        healthButton.addActionListener(e -> {
            if (controller != null) controller.chooseHealth();
        });

        damageButton.addActionListener(e -> {
            if (controller != null) controller.chooseDamage();
        });

        // Layout
        setLayout(new GridLayout(4, 1));
        add(healthLabel);
        add(damageLabel);
        add(healthButton);
        add(damageButton);

        // Basic window setup
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
    }

    public void setController(LevelUpController controller) {
        this.controller = controller;
    }

    @Override
    public void showLevelUpScreen(LevelUpViewModel levelUpViewModel) {
        healthLabel.setText("Health: " + levelUpViewModel.getHealth());
        damageLabel.setText("Damage: " + levelUpViewModel.getDamage());

        this.setVisible(true);
    }

    @Override
    public void hideLevelUpScreen() {
        this.setVisible(false);
    }
}



