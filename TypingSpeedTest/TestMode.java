package TypingSpeedTest;


import TypingSpeedTest.core.TestModeInterface;

public abstract class TestMode implements TestModeInterface {
    protected String modeName;
    protected int value;

    //constructor
    public TestMode(String modeName, int value) {
        this.modeName = modeName;
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}