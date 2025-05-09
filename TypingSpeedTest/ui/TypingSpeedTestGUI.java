package TypingSpeedTest.ui;

import TypingSpeedTest.core.TestSession;
import TypingSpeedTest.TestMode;
import TypingSpeedTest.core.TimeLimitedMode;
import TypingSpeedTest.core.WordLimitedMode;
import TypingSpeedTest.util.TestDisplayUtil;
import TypingSpeedTest.util.TestResultCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Main GUI window for the typing speed test application.
 * Handles user interface components and test session management.
 */
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
    private long startTime;
    private String lastProcessedText = "";
    private boolean testStarted = false;

    /**
     * Constructs and initializes the main application window.
     */
    public TypingSpeedTestGUI() {
        initializeGUI();
        setTitle("Typing Speed Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes all GUI components and layouts.
     */
    private void initializeGUI() {
        setLayout(new BorderLayout());

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

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        JScrollPane targetScroll = new JScrollPane(textArea);
        targetScroll.setPreferredSize(new Dimension(800, 300));

        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        inputArea.setEnabled(false);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setPreferredSize(new Dimension(800, 300));

        inputArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            /**
             * Handles keyboard input for space and enter keys.
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processInput();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        timeUpLabel = new JLabel("TIME'S UP!", SwingConstants.CENTER);
        timeUpLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        timeUpLabel.setForeground(Color.RED);
        timeUpLabel.setVisible(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, targetScroll, inputScroll);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(10);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel = new JLabel("Time: --");
        statsLabel = new JLabel("Words: 0 | Correct: 0 | Incorrect: 0");
        statusPanel.add(timerLabel);
        statusPanel.add(Box.createHorizontalStrut(20));
        statusPanel.add(statsLabel);

        add(modePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        add(startButton, BorderLayout.EAST);
    }

    /**
     * Starts a new test session with selected parameters.
     */
    private void startTest() {
        session = new TestSession(mode);
        session.start();
        textArea.setText(TestDisplayUtil.formatTargetText(session, 70));
        inputArea.setText("");
        inputArea.setEnabled(true);
        inputArea.requestFocus();
        startButton.setEnabled(false);
        timeUpLabel.setVisible(false);
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

    /**
     * Updates the timer display with current remaining time.
     */
    private void updateTimerDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    /**
     * Processes user input and updates test progress.
     */
    private void processInput() {
        String currentText = inputArea.getText().trim();
        if (currentText.equals(lastProcessedText)) return;

        String[] words = currentText.split("\\s+");
        for (int i = 0; i < words.length && i < session.getTargetWords().length; i++) {
            if (i >= session.getCurrentWordIndex()) {
                session.processWord(words[i]);
            }
        }
        lastProcessedText = currentText;
        updateStats();

        if (mode instanceof WordLimitedMode && session.getCurrentWordIndex() >= mode.getValue()) {
            inputArea.setEnabled(false);
            session.setEndTime(System.currentTimeMillis());
            showResults();
        }
    }

    /**
     * Updates the statistics display with current values.
     */
    private void updateStats() {
        statsLabel.setText(String.format(
                "Words: %d | Correct Chars: %d | Incorrect Chars: %d",
                session.getCurrentWordIndex(),
                session.getCorrectChars(),
                session.getIncorrectChars()
        ));

        if (mode instanceof WordLimitedMode) {
            timerLabel.setText(String.format("Words: %d/%d",
                    session.getCurrentWordIndex(),
                    mode.getValue()));
        }
    }

    /**
     * Displays final test results in a dialog.
     */
    private void showResults() {
        long durationMillis = session.getEndTime() - session.getStartTime();
        if (durationMillis <= 0) durationMillis = 1;

        double seconds = durationMillis / 1000.0;
        int totalTyped = session.getCurrentWordIndex();
        int correctWords = TestResultCalculator.getCorrectWords(session);
        int correctChars = session.getCorrectChars();
        int incorrectChars = session.getIncorrectChars();

        double correctWordPercentage = TestResultCalculator.calculateWordPercentage(correctWords, totalTyped);
        double wpm = TestResultCalculator.calculateWPM(session, durationMillis);
        double accuracy = TestResultCalculator.calculateAccuracy(session);

        String wpmColor = wpm >= 40 ? "#27AE60" : "#E74C3C";
        String accuracyColor = accuracy >= 90 ? "#27AE60" : "#E74C3C";

        String results = String.format(
                "<html><div style='font-size:16px; text-align:center; width:450px; padding:15px;'>" +
                        "<h2 style='color:#2E86C1; margin:0 0 15px 0;'>TEST RESULTS</h2>" +
                        "<table style='margin:0 auto; text-align:left; border-collapse:collapse;'>" +
                        "<tr><td style='padding:3px 10px;'><b>Test Mode:</b></td><td style='padding:3px 10px;'>%s</td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Duration:</b></td><td style='padding:3px 10px;'>%.1f seconds</td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Words Typed:</b></td><td style='padding:3px 10px;'>%d/%d</td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Correct Words:</b></td><td style='padding:3px 10px;'>%d (%.1f%%)</td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Characters:</b></td><td style='padding:3px 10px;'>%d correct, %d incorrect</td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Typing Speed:</b></td><td style='padding:3px 10px;'><span style='color:%s'>%.1f WPM</span></td></tr>" +
                        "<tr><td style='padding:3px 10px;'><b>Accuracy:</b></td><td style='padding:3px 10px;'><span style='color:%s'>%.1f%%</span></td></tr>" +
                        "</table></div></html>",
                mode instanceof TimeLimitedMode ? mode.getValue() + " second mode" : mode.getValue() + " word mode",
                seconds,
                totalTyped,
                session.getTargetWords().length,
                correctWords,
                correctWordPercentage,
                correctChars,
                incorrectChars,
                wpmColor,
                wpm,
                accuracyColor,
                accuracy
        );

        JDialog resultsDialog = new JDialog(this, "Test Complete", true);
        resultsDialog.setSize(500, 400);
        resultsDialog.setLayout(new BorderLayout());

        JLabel resultsLabel = new JLabel(results);
        resultsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        resultsDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
        resultsDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");

        JButton closeButton = new JButton("Close Results");
        closeButton.addActionListener(e -> resultsDialog.dispose());
        closeButton.setFocusPainted(false);
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);

        InputMap im = closeButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "none");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        resultsDialog.add(resultsLabel, BorderLayout.CENTER);
        resultsDialog.add(buttonPanel, BorderLayout.SOUTH);
        resultsDialog.setLocationRelativeTo(this);
        resultsDialog.setVisible(true);
    }

    /**
     * Application entry point
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypingSpeedTestGUI());
    }
}