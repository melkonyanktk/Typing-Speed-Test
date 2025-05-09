package TypingSpeedTest.core;

import TypingSpeedTest.data.WordPicker;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implements time-limited test mode functionality.
 * Manages countdown timer and test completion checks.
 */
public class TimeLimitedMode extends TypingSpeedTest.TestMode {
    private Timer timer;
    private volatile boolean timeExpired;

    /**
     * Creates time-limited test mode configuration.
     * @param seconds Test duration in seconds
     */
    public TimeLimitedMode(int seconds) {
        super("Time Limited: " + seconds + " seconds", seconds);
    }

    /**
     * Starts countdown timer for the test session.
     * @param session Test session to monitor
     */
    public void testStart(TestSession session) {
        timer = new Timer();
        final TestSession finalSession = session;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeExpired = true;
                finalSession.setEndTime(System.currentTimeMillis());
                System.out.println("\nTime's up!");
            }
        }, value * 1000);
    }

    /**
     * Stops the countdown timer prematurely.
     */
    public void cancelTimer() {
        if(timer != null) timer.cancel();
    }

    /**
     * Checks if test duration has expired.
     * @return true if time limit has been reached
     */
    public boolean isTimeExpired() {
        return timeExpired;
    }

    /**
     * Configures test session with random words.
     * @param session Test session to initialize
     */
    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(WordPicker.getRandomWords(100));
    }

    /**
     * Determines if test should end.
     * @param session Test session to check
     * @return true if time expired or all words processed
     */
    @Override
    public boolean isComplete(TestSession session) {
        return timeExpired || session.getCurrentWordIndex() >= session.getTargetWords().length;
    }
}