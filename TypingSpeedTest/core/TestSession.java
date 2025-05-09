package TypingSpeedTest.core;

import TypingSpeedTest.TestMode;
import TypingSpeedTest.exceptions.InvalidModeException;

/**
 * Manages a typing test session, tracking user input and calculating statistics.
 */
public class TestSession {
    /** Configuration for the current test mode */
    private final TypingSpeedTest.TestMode mode;
    /** Words the user needs to type */
    private String[] targetWords;
    /** Words actually typed by the user */
    private String[] userWords;
    /** Index of current word being processed */
    private int currentWordIndex;
    /** Count of correctly typed characters */
    private int correctChars;
    /** Count of incorrect characters */
    private int incorrectChars;
    /** Timestamp when test started */
    private long startTime;
    /** Timestamp when test ended */
    private long endTime;

    /**
     * Creates new test session with specified mode.
     * Ensures session configuration cannot be modified externally after its initialization.
     * @param mode Test configuration to use
     * @throws InvalidModeException if unsupported mode type is provided
     */
    public TestSession(TestMode mode) throws InvalidModeException {
        if (mode == null) {
            throw new InvalidModeException("Test mode cannot be null");
        }
        if (mode instanceof TimeLimitedMode) {
            this.mode = new TimeLimitedMode((TimeLimitedMode) mode);
        } else if (mode instanceof WordLimitedMode) {
            this.mode = new WordLimitedMode((WordLimitedMode) mode);
        } else {
            throw new InvalidModeException("Unsupported TestMode type: " + mode.getClass().getName());
        }
        this.mode.setupTest(this);
    }

    /**
     * Initializes timing and storage for new test.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.userWords = new String[targetWords.length];
        mode.testStart(this);
    }

    /**
     * Processes user's word input and updates statistics.
     * @param userWord The text entered by the user
     */
    public void processWord(String userWord) {
        if(currentWordIndex >= targetWords.length) return;

        String targetWord = targetWords[currentWordIndex];
        int correct = 0;
        int incorrect = 0;

        for(int i = 0; i < Math.min(userWord.length(), targetWord.length()); i++) {
            if(userWord.charAt(i) == targetWord.charAt(i)) correct++;
            else incorrect++;
        }
        incorrect += Math.abs(userWord.length() - targetWord.length());

        correctChars += correct;
        incorrectChars += incorrect;
        userWords[currentWordIndex] = userWord;
        currentWordIndex++;
    }

    /**
     * Gets user's typed words up to current progress.
     * @return Array of processed user words
     */
    public String[] getUserWords() {
        return java.util.Arrays.copyOf(userWords, currentWordIndex);
    }

    /**
     * Checks if test completion conditions are met.
     * @return true if test should end
     */
    public boolean isComplete() {
        return mode.isComplete(this);
    }

    /**
     * @return Target words for the test
     */
    public String[] getTargetWords() { return targetWords; }

    /**
     * @return Current word position in test
     */
    public int getCurrentWordIndex() { return currentWordIndex; }

    /**
     * @return Timestamp when test started
     */
    public long getStartTime() { return startTime; }

    /**
     * @return Count of correctly typed characters
     */
    public int getCorrectChars() { return correctChars; }

    /**
     * @return Count of incorrect characters
     */
    public int getIncorrectChars() { return incorrectChars; }

    /**
     * @return Timestamp when test ended
     */
    public long getEndTime() { return endTime; }

    /**
     * @return Current test mode configuration
     */
    public TestMode getMode() { return mode; }

    /**
     * Sets target words for the test.
     * @param words Array of words to use as targets
     */
    public void setTargetWords(String[] words) {
        targetWords = words;
    }

    /**
     * Records test completion timestamp.
     * @param endTime System time when test ended
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}