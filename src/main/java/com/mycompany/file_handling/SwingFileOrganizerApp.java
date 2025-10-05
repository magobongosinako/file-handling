package com.mycompany.file_handling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class SwingFileOrganizerApp extends JFrame {

    private FileOrganizer organizer;
    private JTextArea logArea;
    private JButton dryRunButton, actualRunButton, undoButton, showRulesButton, addRuleButton, removeRuleButton, showHistoryButton, getFileInfoButton, clearHistoryButton, exitButton;

    public SwingFileOrganizerApp() {
        // Select base directory
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Base Directory for File Organization");
        int result = chooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }
        File baseDir = chooser.getSelectedFile();
        String baseDirectory = baseDir.getAbsolutePath();
        String logFile = baseDirectory + File.separator + "file_organizer.log";

        // Initialize organizer
        organizer = new FileOrganizer(baseDirectory, logFile);

        // Setup GUI
        setTitle("File Organizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        // Set log area in logger
        organizer.getLogger().setLogArea(logArea);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 2));

        dryRunButton = new JButton("Organize Files (Dry Run)");
        actualRunButton = new JButton("Organize Files (Actual)");
        undoButton = new JButton("Undo Last Operation");
        showRulesButton = new JButton("Show Organization Rules");
        addRuleButton = new JButton("Add Custom Rule");
        removeRuleButton = new JButton("Remove Rule");
        showHistoryButton = new JButton("Show Operation History");
        getFileInfoButton = new JButton("Get File Information");
        clearHistoryButton = new JButton("Clear Operation History");
        exitButton = new JButton("Exit");

        buttonPanel.add(dryRunButton);
        buttonPanel.add(actualRunButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(showRulesButton);
        buttonPanel.add(addRuleButton);
        buttonPanel.add(removeRuleButton);
        buttonPanel.add(showHistoryButton);
        buttonPanel.add(getFileInfoButton);
        buttonPanel.add(clearHistoryButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        dryRunButton.addActionListener(e -> organizer.organizeFiles(true));
        actualRunButton.addActionListener(e -> organizer.organizeFiles(false));
        undoButton.addActionListener(e -> {
            boolean success = organizer.undoLastOperation();
            JOptionPane.showMessageDialog(this, success ? "Undo successful!" : "Undo failed!");
        });
        showRulesButton.addActionListener(e -> organizer.showRules());
        addRuleButton.addActionListener(e -> addCustomRule());
        removeRuleButton.addActionListener(e -> removeRule());
        showHistoryButton.addActionListener(e -> showHistory());
        getFileInfoButton.addActionListener(e -> getFileInfo());
        clearHistoryButton.addActionListener(e -> clearHistory());
        exitButton.addActionListener(e -> {
            organizer.close();
            System.exit(0);
        });

        setVisible(true);
    }

    private void addCustomRule() {
        String fileType = JOptionPane.showInputDialog(this, "Enter file extension (e.g., .json):");
        if (fileType == null || fileType.trim().isEmpty()) return;
        String category = JOptionPane.showInputDialog(this, "Enter category name:");
        if (category == null || category.trim().isEmpty()) return;
        organizer.addCustomRule(fileType, category);
        JOptionPane.showMessageDialog(this, "Custom rule added!");
    }

    private void removeRule() {
        String fileType = JOptionPane.showInputDialog(this, "Enter file extension to remove:");
        if (fileType == null || fileType.trim().isEmpty()) return;
        String category = JOptionPane.showInputDialog(this, "Enter category:");
        if (category == null || category.trim().isEmpty()) return;
        boolean removed = organizer.removeRule(fileType, category);
        JOptionPane.showMessageDialog(this, removed ? "Rule removed!" : "Rule not found!");
    }

    private void showHistory() {
        JOptionPane.showMessageDialog(this, "Operation history feature coming soon!");
    }

    private void getFileInfo() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename:");
        if (filename == null || filename.trim().isEmpty()) return;
        Map<String, Object> info = organizer.getFilesInfo(filename);
        StringBuilder sb = new StringBuilder("File Information:\n");
        for (Map.Entry<String, Object> entry : info.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void clearHistory() {
        organizer.getLogger().clearHistory();
        JOptionPane.showMessageDialog(this, "Operation history cleared!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingFileOrganizerApp());
    }
}
