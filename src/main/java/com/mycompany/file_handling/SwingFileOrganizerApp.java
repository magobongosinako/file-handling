package com.mycompany.file_handling;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.ExecutionException;


public class SwingFileOrganizerApp extends JFrame {
       private FileOrganizer organizer;
    private FileLogger logger;
    
    private JTextField sourceDirectoryField;
    private JTextArea logArea;
    private JList<OrganizationRule> rulesList;
    private DefaultListModel<OrganizationRule> rulesListModel;
    private JLabel statusLabel;
    private JButton organizeButton;
    private JButton dryRunButton;
    private JButton undoButton;
    
    // Dark theme colors (neutral grey)
    private Color DARK_BG = new Color(28, 28, 30);
    private Color DARK_CARD = new Color(44, 44, 46);
    private Color DARK_CARD_HOVER = new Color(58, 58, 60);
    private Color DARK_TEXT_PRIMARY = new Color(242, 242, 247);
    private Color DARK_TEXT_SECONDARY = new Color(174, 174, 178);
    private Color DARK_BORDER = new Color(72, 72, 74);
    
    // Accent colors (neutral for both themes)
    private Color ACCENT_SECONDARY = new Color(142, 142, 147);
    private Color SUCCESS_COLOR = new Color(52, 199, 89);
    private Color WARNING_COLOR = new Color(255, 159, 10);
    private Color DANGER_COLOR = new Color(255, 69, 58);

    public SwingFileOrganizerApp(FileOrganizer organizer, FileLogger logger) {
        this.organizer = organizer;
        this.logger = logger;
        
        rulesListModel = new DefaultListModel<>();
        initializeRulesList();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("File Organizer");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
        getContentPane().setBackground(DARK_BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void initializeRulesList() {
        // Load all default rules from organizer
        for (OrganizationRule rule : organizer.getRulesCategory("")) {
            rulesListModel.addElement(rule);
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, DARK_BORDER),
            new EmptyBorder(25, 35, 25, 35)
        ));
        
        JPanel leftPanel = new JPanel(new BorderLayout(0, 8));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("File Organizer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(DARK_TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Smart file management system with custom automation rules");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_TEXT_SECONDARY);
        
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        
        mainPanel.add(createSourceDirectoryCard(), BorderLayout.NORTH);
        mainPanel.add(createContentSplit(), BorderLayout.CENTER);
        
        return mainPanel;
    }

    private JPanel createSourceDirectoryCard() {
        ModernCard card = new ModernCard();
        card.setLayout(new BorderLayout(15, 15));
        
        JLabel label = new JLabel("Source Directory");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(DARK_TEXT_PRIMARY);
        
        JPanel fieldPanel = new JPanel(new BorderLayout(15, 0));
        fieldPanel.setOpaque(false);
        
        sourceDirectoryField = new ModernTextField(organizer.getSourceDirectory());
        
        JButton browseButton = createNeutralButton("Browse");
        browseButton.addActionListener(e -> browseDirectory());
        
        fieldPanel.add(sourceDirectoryField, BorderLayout.CENTER);
        fieldPanel.add(browseButton, BorderLayout.EAST);
        
        card.add(label, BorderLayout.NORTH);
        card.add(fieldPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JSplitPane createContentSplit() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            createRulesCard(), createLogCard());
        splitPane.setDividerLocation(450);
        splitPane.setBorder(null);
        splitPane.setBackground(DARK_BG);
        splitPane.setDividerSize(2);
        splitPane.setOpaque(false);
        
        return splitPane;
    }

    private JPanel createRulesCard() {
        ModernCard card = new ModernCard();
        card.setLayout(new BorderLayout(0, 18));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Organization Rules");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_TEXT_PRIMARY);
        
        JLabel countLabel = new JLabel(rulesListModel.size() + " rules");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(DARK_TEXT_SECONDARY);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(countLabel, BorderLayout.EAST);
        
        rulesList = new JList<>(rulesListModel);
        rulesList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rulesList.setBackground(DARK_BG);
        rulesList.setForeground(DARK_TEXT_PRIMARY);
        rulesList.setBorder(new EmptyBorder(10, 10, 10, 10));
        rulesList.setSelectionBackground(DARK_CARD_HOVER);
        rulesList.setSelectionForeground(DARK_TEXT_PRIMARY);
        rulesList.setCellRenderer(new ModernListCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(rulesList);
        scrollPane.setBorder(new RoundedBorder(DARK_BORDER, 10));
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARK_BG);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton addButton = createAccentButton("+ Add Rule", SUCCESS_COLOR);
        JButton removeButton = createAccentButton("- Remove", DANGER_COLOR);
        JButton showAllButton = createSmallButton("Show All");
        
        addButton.addActionListener(e -> addRule());
        removeButton.addActionListener(e -> removeRule());
        showAllButton.addActionListener(e -> showAllRules());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(showAllButton);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }

    private JPanel createLogCard() {
        ModernCard card = new ModernCard();
        card.setLayout(new BorderLayout(0, 18));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Activity Log");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_TEXT_PRIMARY);
        
        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonGroup.setOpaque(false);
        
        JButton historyButton = createSmallButton("History");
        JButton clearButton = createSmallButton("Clear");
        
        historyButton.addActionListener(e -> showOperationHistory());
        clearButton.addActionListener(e -> clearLog());
        
        buttonGroup.add(historyButton);
        buttonGroup.add(clearButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonGroup, BorderLayout.EAST);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        logArea.setBackground(new Color(20, 20, 22));
        logArea.setForeground(DARK_TEXT_PRIMARY);
        logArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(new RoundedBorder(DARK_BORDER, 10));
        scrollPane.setBackground(logArea.getBackground());
        scrollPane.getViewport().setBackground(logArea.getBackground());
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createFooter() {
    JPanel footer = new JPanel(new BorderLayout(20, 0));
    footer.setBackground(DARK_CARD);
    footer.setBorder(BorderFactory.createCompoundBorder(
        new MatteBorder(1, 0, 0, 0, DARK_BORDER),
        new EmptyBorder(20, 35, 20, 35)
    ));
    
    statusLabel = new JLabel("Ready");
    statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    statusLabel.setForeground(DARK_TEXT_SECONDARY);
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    buttonPanel.setOpaque(false);
    
    dryRunButton = createAccentButton("Dry Run", ACCENT_SECONDARY);
    dryRunButton.addActionListener(e -> organizeFiles(true));
    
    organizeButton = createAccentButton("Organize", SUCCESS_COLOR);
    organizeButton.addActionListener(e -> organizeFiles(false));
    
    undoButton = createAccentButton("Undo Last Operation", WARNING_COLOR);  // Renamed
    undoButton.addActionListener(e -> undoLastOperation());
    
    JButton undoAllButton = createAccentButton("Undo All Operations", DANGER_COLOR);  // New button
    undoAllButton.addActionListener(e -> undoAllOperations());
    
    buttonPanel.add(dryRunButton);
    buttonPanel.add(organizeButton);
    buttonPanel.add(undoButton);
    buttonPanel.add(undoAllButton);  // Added next to Undo Last Operation
    
    footer.add(statusLabel, BorderLayout.WEST);
    footer.add(buttonPanel, BorderLayout.EAST);
    
    // ... (existing buttons)

/*JButton fileInfoButton = createNeutralButton("File Info");  // New button; use createSmallButton or similar if preferred
fileInfoButton.addActionListener(e -> showFileInfoDialog());

buttonPanel.add(fileInfoButton);  */
    
    return footer;
}
   /* private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout(20, 0));
        footer.setBackground(DARK_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, DARK_BORDER),
            new EmptyBorder(20, 35, 20, 35)
        ));
        
        statusLabel = new JLabel("Ready to organize");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(DARK_TEXT_PRIMARY);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);
        
        undoButton = createAccentButton("Undo", WARNING_COLOR);
        dryRunButton = createNeutralButton("Dry Run");
        organizeButton = createAccentButton("Organize", SUCCESS_COLOR);
        
        undoButton.addActionListener(e -> undoLastOperation());
        dryRunButton.addActionListener(e -> organizeFiles(true));
        organizeButton.addActionListener(e -> organizeFiles(false));
        
        buttonPanel.add(undoButton);
        buttonPanel.add(dryRunButton);
        buttonPanel.add(organizeButton);
        
        footer.add(statusLabel, BorderLayout.WEST);
        footer.add(buttonPanel, BorderLayout.EAST);
        
        return footer;
    }*/

    private JButton createNeutralButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(DARK_TEXT_PRIMARY);
        button.setBackground(DARK_CARD_HOVER);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_SECONDARY);
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARK_CARD_HOVER);
                button.setForeground(DARK_TEXT_PRIMARY);
            }
        });
        
        return button;
    }

    private JButton createAccentButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color btnColor = color;
                if (getModel().isPressed()) {
                    btnColor = color.darker();
                } else if (getModel().isRollover()) {
                    btnColor = color.brighter();
                }
                
                g2.setColor(btnColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(DARK_TEXT_SECONDARY);
        button.setBackground(DARK_CARD_HOVER);
        button.setBorder(new EmptyBorder(6, 14, 6, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(DARK_TEXT_PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(DARK_TEXT_SECONDARY);
            }
        });
        
        return button;
    }

    private void browseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(organizer.getSourceDirectory()));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            sourceDirectoryField.setText(path);
            organizer.setSourceDirectory(path);
            updateLog("Source directory changed to: " + path);
        }
    }

   
        private void addRule() {
        String ext = JOptionPane.showInputDialog(this, "Enter file extension (e.g., .txt, .json):");
        if (ext == null || ext.isEmpty()) return;
        
        String extFolder = JOptionPane.showInputDialog(this, "Enter target folder:");
        if (extFolder == null || extFolder.isEmpty()) return;
        
        OrganizationRule rule = OrganizationRule.byExtension(ext, extFolder);
        organizer.addRule(rule);
        rulesListModel.addElement(rule);
        updateLog("Rule added: " + rule.getRuleName());
        updateRuleCount();
    
        
        
        /*
        String[] ruleTypes = {"By Extension", "By Name Contains", "By Size"};
        String choice = (String) JOptionPane.showInputDialog(this, 
            "Select rule type:", "Add Organization Rule", 
            JOptionPane.QUESTION_MESSAGE, null, ruleTypes, ruleTypes[0]);

        if (choice == null) return;

        OrganizationRule rule = null;

        switch (choice) {
            case "By Extension" -> {
                String ext = JOptionPane.showInputDialog(this, "Enter file extension (e.g., .txt, .json):");
                String extFolder = JOptionPane.showInputDialog(this, "Enter target folder:");
                if (ext != null && extFolder != null && !ext.isEmpty() && !extFolder.isEmpty()) {
                    rule = OrganizationRule.byExtension(ext, extFolder);
                }
            }
            case "By Name Contains" -> {
                String keyword = JOptionPane.showInputDialog(this, "Enter keyword:");
                String keywordFolder = JOptionPane.showInputDialog(this, "Enter target folder:");
                if (keyword != null && keywordFolder != null && !keyword.isEmpty() && !keywordFolder.isEmpty()) {
                    rule = OrganizationRule.byNameContains(keyword, keywordFolder);
                }
            }
            case "By Size" -> {
                String minStr = JOptionPane.showInputDialog(this, "Enter minimum size (bytes):");
                String maxStr = JOptionPane.showInputDialog(this, "Enter maximum size (bytes):");
                String sizeFolder = JOptionPane.showInputDialog(this, "Enter target folder:");
                if (minStr != null && maxStr != null && sizeFolder != null) {
                    try {
                        rule = OrganizationRule.bySize(Long.parseLong(minStr),
                                Long.parseLong(maxStr), sizeFolder);
                    } catch (NumberFormatException e) {
                        updateLog("Invalid size input: " + e.getMessage());
                    }
                }
            }
        }

        if (rule != null) {
            organizer.addRule(rule);
            rulesListModel.addElement(rule);
            updateLog("Rule added: " + rule.getRuleName());
            updateRuleCount();
        }
        */
    }

    private void removeRule() {
        OrganizationRule selectedRule = rulesList.getSelectedValue();
        if (selectedRule != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this rule?\n" + selectedRule.getRuleName(),
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                organizer.removeRule(selectedRule);
                rulesListModel.removeElement(selectedRule);
                updateLog("✗ Rule removed: " + selectedRule.getRuleName());
                updateRuleCount();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a rule to remove.");
        }
    }

    private void showAllRules() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL ORGANIZATION RULES ===\n\n");
        for (int i = 0; i < rulesListModel.size(); i++) {
            sb.append((i + 1)).append(". ").append(rulesListModel.get(i).toString()).append("\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "All Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    private void organizeFiles(boolean dryRun) {
        String mode = dryRun ? "Dry Run" : "Organizing";
        statusLabel.setText("" + mode + " in progress...");
        organizeButton.setEnabled(false);
        dryRunButton.setEnabled(false);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                organizer.organizeFiles(dryRun);
                return null;
            }

            @Override
            protected void done() {
                refreshLog();
                statusLabel.setText(String.format(" %s complete: %d files processed, %d organized", 
                    mode, organizer.getFilesProcessed(), organizer.getFilesOrganized()));
                organizeButton.setEnabled(true);
                dryRunButton.setEnabled(true);
            }
        };
        
        worker.execute();
    }

    private void undoLastOperation() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to undo the last operation?",
            "Confirm Undo",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            statusLabel.setText("Undoing operation...");
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return organizer.undoLastOperation();
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            statusLabel.setText("Operation undone successfully");
                            updateLog("Last operation undone");
                            refreshLog();
                        } else {
                            statusLabel.setText("Undo failed - no operations to undo");
                            updateLog("Undo failed - no operations to undo");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        statusLabel.setText("Undo failed");
                        updateLog("Undo error: " + e.getMessage());
                    }
                }
            };
            
            worker.execute();
        }
    }
    
    private void undoAllOperations() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to undo ALL operations? This cannot be reversed.",
        "Confirm Bulk Undo",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        statusLabel.setText("Undoing all operations...");
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return organizer.undoAllOperations();
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        statusLabel.setText("All operations undone successfully");
                        updateLog("Undid all operations");
                        refreshLog();
                    } else {
                        statusLabel.setText("Bulk undo partially failed - check log");
                        updateLog("Bulk undo completed with some failures");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    statusLabel.setText("Bulk undo failed");
                    updateLog("Bulk undo error: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
}

    private void showOperationHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== OPERATION HISTORY ===\n\n");
        
        // Get history from organizer
        for (String entry : logger.readLogsFromFile()) {
            sb.append(entry).append("\n");
        }
        
        if (sb.length() <= 30) {
            sb.append("No operations recorded yet.");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 450));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Operation History", JOptionPane.INFORMATION_MESSAGE);
    }
    
   
    private void updateRuleCount() {
        // Update the rule count label in the rules card header
        Component[] components = ((JPanel)((JSplitPane)((JPanel)getContentPane()
            .getComponent(1)).getComponent(1)).getLeftComponent()).getComponents();
        if (components.length > 0 && components[0] instanceof JPanel) {
            JPanel headerPanel = (JPanel) components[0];
            if (headerPanel.getComponentCount() > 1 && headerPanel.getComponent(1) instanceof JLabel) {
                JLabel countLabel = (JLabel) headerPanel.getComponent(1);
                countLabel.setText(rulesListModel.size() + " rules");
            }
        }
    }

    private void updateLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void refreshLog() {
        logArea.setText("");
        for (String entry : logger.readLogsFromFile()) {
            logArea.append(entry + "\n");
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void clearLog() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear the log?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            logger.clearLogs();
            logArea.setText("");
            updateLog("Log cleared");
            statusLabel.setText("Log cleared");
        }
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            refreshLog();
        });
    }

    // Custom Components

    class ModernCard extends JPanel {
        public ModernCard() {
            setBackground(DARK_CARD);
            setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(DARK_BORDER, 12),
                new EmptyBorder(20, 20, 20, 20)
            ));
        }
    }

    class ModernTextField extends JTextField {
        public ModernTextField(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBackground(DARK_BG);
            setForeground(DARK_TEXT_PRIMARY);
            setCaretColor(DARK_TEXT_PRIMARY);
            setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(DARK_BORDER, 8),
                new EmptyBorder(10, 15, 10, 15)
            ));
        }
    }

    class RoundedBorder extends AbstractBorder {
        private Color color;
        private int radius;
        
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }

    class ModernListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            
            label.setBorder(new EmptyBorder(8, 10, 8, 10));
            
            if (isSelected) {
                label.setBackground(DARK_CARD_HOVER);
                label.setForeground(DARK_TEXT_PRIMARY);
            } else {
                label.setBackground(DARK_BG);
                label.setForeground(DARK_TEXT_PRIMARY);
            }
            
            return label;
        }
    }
}
