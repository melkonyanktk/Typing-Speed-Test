package TypingSpeedTest.core;

import TypingSpeedTest.data.WordPicker;

import java.util.Timer;
import java.util.TimerTask;

public class TimeLimitedMode extends TypingSpeedTest.TestMode {
    private Timer timer;
    private volatile boolean timeExpired;

    public TimeLimitedMode(int seconds) {
        super("Time Limited: " + seconds + " seconds", seconds);
    }

    public void testStart(TestSession session) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeExpired = true;
                System.out.println("\nTime's up! Press Enter.");
            }
        }, value * 1000);
    }

    public void cancelTimer() {
        if(timer != null) timer.cancel();
    }

    public boolean isTimeExpired() {
        return timeExpired;
    }

    @Override
    public void setupTest(TestSession session) {
        session.setTargetWords(WordPicker.getRandomWords(100));
    }

    @Override
    public boolean isComplete(TestSession session) {
        return timeExpired;
    }
}