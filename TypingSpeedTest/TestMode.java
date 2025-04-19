package TypingSpeedTest;

import TypingSpeedTest.core.TestSession;

public abstract class TestMode {
    protected String modeName;
    protected int value;

    //constructor
    public TestMode(String modeName, int value) {
        this.modeName = modeName;
        this.value = value;
    }

    public abstract void setupTest(TestSession session);
    public abstract boolean isComplete(TestSession session);
}