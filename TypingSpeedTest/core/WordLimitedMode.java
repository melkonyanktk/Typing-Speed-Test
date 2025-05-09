package TypingSpeedTest.core;

import TypingSpeedTest.data.WordPicker;

/**
 * Implements word-limited test mode functionality.
 * Tracks progress based on number of words typed rather than time.
 */
public class WordLimitedMode extends TypingSpeedTest.TestMode {

    /**
     * Creates word-limited test mode configuration.
     * @param wordCount Target number of words for the test
     */
    public WordLimitedMode(int wordCount) {
        super("Word Limited: " + wordCount + " words", wordCount);
    }

    /**
     * Initializes test session with random target words.
     * @param session Test session to configure
     */
    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(WordPicker.getRandomWords(value));
    }

    /**
     * Checks if required word count has been reached.
     * @param session Test session to evaluate
     * @return true if user has processed all required words
     */
    @Override
    public boolean isComplete(TestSession session) {
        return session.getCurrentWordIndex() >= value;
    }

    /**
     * No initialization needed for word-limited mode.
     * Required by parent class but unused in this mode.
     * @param session Test session (unused)
     */
    @Override
    public void testStart(TestSession session) {
        // Intentionally blank - no timer needed for word-limited mode
    }
}