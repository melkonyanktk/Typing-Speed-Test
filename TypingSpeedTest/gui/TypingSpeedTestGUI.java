package TypingSpeedTest.gui;

import TypingSpeedTest.core.TestSession;
import TypingSpeedTest.TestMode;
import TypingSpeedTest.core.TimeLimitedMode;
import TypingSpeedTest.core.WordLimitedMode;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TypingSpeedTestGUI extends JFrame {
    private JTextArea textArea;
    private JTextArea inputArea;
    private JButton startButton;
    private JLabel timerLabel;
    private JLabel statsLabel;
    private JLabel timeUpLabel;
    private TestSession session;
    private TestMode mode;
    private Timer swingTimer;
    private int secondsRemaining;
    private int correctWordsCount = 0;
    private int totalWordsTyped = 0;
    private long startTime;
    private String lastProcessedText = "";

    public TypingSpeedTestGUI() {
        initializeGUI();
        setTitle("Typing Speed Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGUI() {
        setLayout(new BorderLayout());

        // Mode selection panel
        JPanel modePanel = new JPanel(new GridLayout(2, 3, 5, 5));
        modePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ButtonGroup modeGroup = new ButtonGroup();

        JRadioButton time15 = new JRadioButton("15 seconds");
        JRadioButton time30 = new JRadioButton("30 seconds");
        JRadioButton time60 = new JRadioButton("60 seconds");
        JRadioButton words50 = new JRadioButton("50 words");
        JRadioButton words100 = new JRadioButton("100 words");

        modeGroup.add(time15);
        modeGroup.add(time30);
        modeGroup.add(time60);
        modeGroup.add(words50);
        modeGroup.add(words100);

        modePanel.add(time15);
        modePanel.add(time30);
        modePanel.add(time60);
        modePanel.add(words50);
        modePanel.add(words100);
        time15.setSelected(true);

        startButton = new JButton("Start Test");
        startButton.addActionListener(e -> {
            if (time15.isSelected()) mode = new TimeLimitedMode(15);
            else if (time30.isSelected()) mode = new TimeLimitedMode(30);
            else if (time60.isSelected()) mode = new TimeLimitedMode(60);
            else if (words50.isSelected()) mode = new WordLimitedMode(50);
            else if (words100.isSelected()) mode = new WordLimitedMode(100);

            startTest();
        });

        // Target text display
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        JScrollPane targetScroll = new JScrollPane(textArea);
        targetScroll.setPreferredSize(new Dimension(800, 300));

        // User input area
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        inputArea.setEnabled(false);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setPreferredSize(new Dimension(800, 300));

        // Add KeyListener to process input on space or enter
        inputArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processInput();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // Time's up banner
        timeUpLabel = new JLabel("TIME'S UP!", SwingConstants.CENTER);
        timeUpLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        timeUpLabel.setForeground(Color.RED);
        timeUpLabel.setVisible(false);

        // Main content panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, targetScroll, inputScroll);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(10);

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel = new JLabel("Time: --");
        statsLabel = new JLabel("Words: 0 | Correct: 0 | Incorrect: 0");
        statusPanel.add(timerLabel);
        statusPanel.add(Box.createHorizontalStrut(20));
        statusPanel.add(statsLabel);

        // Add components to frame
        add(modePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        add(startButton, BorderLayout.EAST);
    }

    private void startTest() {
        session = new TestSession(mode);
        textArea.setText(String.join(" ", session.getTargetWords()));
        inputArea.setText("");
        inputArea.setEnabled(true);
        inputArea.requestFocus();
        startButton.setEnabled(false);
        timeUpLabel.setVisible(false);
        correctWordsCount = 0;
        totalWordsTyped = 0;
        startTime = System.currentTimeMillis();
        lastProcessedText = "";

        if (mode instanceof TimeLimitedMode) {
            secondsRemaining = mode.getValue();
            updateTimerDisplay();
            swingTimer = new Timer(1000, e -> {
                secondsRemaining--;
                updateTimerDisplay();
                if (secondsRemaining <= 0) {
                    swingTimer.stop();
                    timeUpLabel.setVisible(true);
                    inputArea.setEnabled(false);
                    showResults();
                }
            });
            swingTimer.start();
        } else {
            timerLabel.setText("Words: 0/" + mode.getValue());
        }

        updateStats();
    }

    private void updateTimerDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void processInput() {
        String currentText = inputArea.getText().trim();
        if (currentText.equals(lastProcessedText)) {
            return;
        }

        String[] words = currentText.split("\\s+");
        int newWordCount = words.length;

        if (newWordCount > totalWordsTyped) {
            // Only process new words
            for (int i = totalWordsTyped; i < newWordCount && i < session.getTargetWords().length; i++) {
                totalWordsTyped++;
                String targetWord = session.getTargetWords()[i];
                boolean isCorrect = words[i].equalsIgnoreCase(targetWord);
                if (isCorrect) {
                    correctWordsCount++;
                }
                session.processWord(words[i]);
            }
            lastProcessedText = currentText;
            updateStats();
        }

        // Check if word-limited test is complete
        if (mode instanceof WordLimitedMode && session.getCurrentWordIndex() >= mode.getValue()) {
            inputArea.setEnabled(false);
            showResults();
        }
    }

    private void updateStats() {
        statsLabel.setText(String.format(
                "Words: %d | Correct: %d | Incorrect: %d",
                totalWordsTyped,
                correctWordsCount,
                totalWordsTyped - correctWordsCount
        ));

        if (mode instanceof WordLimitedMode) {
            timerLabel.setText(String.format("Words: %d/%d",
                    session.getCurrentWordIndex(),
                    mode.getValue()));
        }
    }

    private void showResults() {
        long durationMillis = System.currentTimeMillis() - startTime;
        double minutes = Math.max(durationMillis / 60000.0, 0.01);
        double wpm = (correctWordsCount / minutes);
        int totalChars = session.getCorrectChars() + session.getIncorrectChars();
        double accuracy = totalChars > 0 ?
                ((double)session.getCorrectChars() / totalChars) * 100 : 0;

        String results = String.format(
                "<html><div style='font-size:16px; text-align:center; width:350px;'>" +
                        "<h2 style='color:#2E86C1;'>TEST RESULTS</h2>" +
                        "<table style='margin: 0 auto; text-align:left;'>" +
                        "<tr><td><b>Test Mode:</b></td><td>%s</td></tr>" +
                        "<tr><td><b>Duration:</b></td><td>%.1f seconds</td></tr>" +
                        "<tr><td><b>Words Typed:</b></td><td>%d</td></tr>" +
                        "<tr><td><b>Correct Words:</b></td><td>%d (%.1f%%)</td></tr>" +
                        "<tr><td><b>Characters:</b></td><td>%d correct, %d incorrect</td></tr>" +
                        "<tr><td><b>Typing Speed:</b></td><td><span style='color:%s'>%.1f WPM</span></td></tr>" +
                        "<tr><td><b>Accuracy:</b></td><td><span style='color:%s'>%.1f%%</span></td></tr>" +
                        "</table></div></html>",
                mode instanceof TimeLimitedMode ?
                        mode.getValue() + " second mode" :
                        mode.getValue() + " word mode",
                durationMillis/1000.0,
                totalWordsTyped,
                correctWordsCount,
                (totalWordsTyped > 0 ? ((double)correctWordsCount/totalWordsTyped)*100 : 0),
                session.getCorrectChars(),
                session.getIncorrectChars(),
                wpm > 40 ? "#27AE60" : "#E74C3C",
                wpm,
                accuracy > 90 ? "#27AE60" : "#E74C3C",
                accuracy
        );

        JOptionPane.showMessageDialog(
                this,
                results,
                "Test Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingSpeedTestGUI());
    }
}