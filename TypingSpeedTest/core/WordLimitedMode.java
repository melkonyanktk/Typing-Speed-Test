package TypingSpeedTest.core;

import TypingSpeedTest.data.CommonWords;

class WordLimitedMode extends TypingSpeedTest.TestMode {
    public WordLimitedMode(int wordCount) {
        super("Word Limited: " + wordCount + " words", wordCount);
    }

    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(CommonWords.getRandomWords(value));
    }

    @Override
    public boolean isComplete(TestSession session) {
        return session.getCurrentWordIndex() >= value;
    }
}