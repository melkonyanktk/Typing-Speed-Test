package TypingSpeedTest.core;

import TypingSpeedTest.data.WordPicker;

public class WordLimitedMode extends TypingSpeedTest.TestMode {
    public WordLimitedMode(int wordCount) {
        super("Word Limited: " + wordCount + " words", wordCount);
    }

    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(WordPicker.getRandomWords(value));
    }

    @Override
    public boolean isComplete(TestSession session) {
        return session.getCurrentWordIndex() >= value;
    }

    @Override
    public void testStart() {
        //blank
    }
}