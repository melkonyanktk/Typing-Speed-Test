package TypingSpeedTest.core;

public class TestSession {
    private TypingSpeedTest.TestMode mode; //current mode
    private String[] targetWords; //words to type
    private String[] userWords; //user's typed words
    private int currentWordIndex; //progress tracker
    private int correctChars; //correct character count
    private int incorrectChars; //Incorrect character count
    private long startTime; //test start time

    public TestSession(TypingSpeedTest.TestMode mode) {
        this.mode = mode;
        mode.setupTest(this);
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.userWords = new String[targetWords.length];
    }

    public void processWord(String userWord) {
        // Your existing word processing logic
    }

    //getters
    public String[] getTargetWords() { return targetWords; }
    public int getCurrentWordIndex() { return currentWordIndex; }
    public long getStartTime() { return startTime; }
    public int getCorrectChars() { return correctChars; }
    public int getIncorrectChars() { return incorrectChars; }

    //setters
    public void setTargetWords(String[] words) { targetWords = words; }

    class WordChecker {
        //compares words and characters
    }

    //a method to process a single word from a user and its characters.
    public boolean isComplete() { return mode.isComplete(this); }
}
