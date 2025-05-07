package TypingSpeedTest.core;

public interface TestModeInterface {
    void setupTest(TestSession session);
    boolean isComplete(TestSession session);
    void testStart();
}
