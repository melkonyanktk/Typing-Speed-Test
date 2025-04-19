package TypingSpeedTest.core;

import TypingSpeedTest.data.CommonWords;

class TimeLimitedMode extends TypingSpeedTest.TestMode {
    public TimeLimitedMode(int seconds) {
        super("Time Limited: " + seconds + " seconds", seconds);
    }

    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(CommonWords.getRandomWords(100));
    }

    @Override
    public boolean isComplete(TestSession session) {
        long elapsed = System.currentTimeMillis() - session.getStartTime();
        return elapsed >= value * 1000;
    }
}