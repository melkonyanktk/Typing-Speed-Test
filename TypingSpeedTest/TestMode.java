package TypingSpeedTest;

import TypingSpeedTest.core.TestSession;

/**
 * Abstract base class defining test mode configurations and operations.
 * Provides common structure for different typing test variants.
 */
public abstract class TestMode {
    /** Display name for the test mode */
    protected String modeName;
    /** Configuration value (seconds or word count depending on mode) */
    public int value;

    /**
     * Creates a new test mode configuration.
     * @param modeName Human-readable mode name
     * @param value Mode-specific value (duration or word count)
     */
    public TestMode(String modeName, int value) {
        this.modeName = modeName;
        this.value = value;
    }

    /**
     * Gets the mode's configuration value.
     * @return Test duration in seconds or word count depending on mode
     */
    public int getValue() {
        return value;
    }

    /**
     * Initializes test session with mode-specific settings.
     * @param session Test session to configure
     */
    public abstract void setupTest(TestSession session);

    /**
     * Checks if test completion conditions are met.
     * @param session Test session to evaluate
     * @return true if test should end
     */
    public abstract boolean isComplete(TestSession session);

    /**
     * Starts mode-specific test components.
     * @param session Test session to manage
     */
    public abstract void testStart(TestSession session);
}