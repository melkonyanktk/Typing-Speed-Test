package TypingSpeedTest.core;

public class TestSession {
    private final TypingSpeedTest.TestMode mode;
    private String[] targetWords;
    private String[] userWords;
    private int currentWordIndex;
    private int correctChars;
    private int incorrectChars;
    private long startTime;

    public TestSession(TypingSpeedTest.TestMode mode) {
        this.mode = mode;
        this.mode.setupTest(this);
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.userWords = new String[targetWords.length];
        if(mode instanceof TimeLimitedMode) {
            ((TimeLimitedMode) mode).testStart();
        }
    }

    public void processWord(String userWord) {
        if(currentWordIndex >= targetWords.length) return;

        String targetWord = targetWords[currentWordIndex];
        int correct = 0;
        int incorrect = 0;

        // Character comparison logic
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

    public String[] getUserWords() {
        return java.util.Arrays.copyOf(userWords, currentWordIndex);
    }

    public boolean isComplete() {
        return mode.isComplete(this);
    }

    // Getters
    public String[] getTargetWords() { return targetWords; }
    public int getCurrentWordIndex() { return currentWordIndex; }
    public long getStartTime() { return startTime; }
    public int getCorrectChars() { return correctChars; }
    public int getIncorrectChars() { return incorrectChars; }

    // Setter
    public void setTargetWords(String[] words) {
        targetWords = words;
    }
}