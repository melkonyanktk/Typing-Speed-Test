package TypingSpeedTest.util;

import TypingSpeedTest.core.TestSession;

public class TestResultCalculator {

    public static int getCorrectWords(TestSession session) {
        String[] target = session.getTargetWords();
        String[] user = session.getUserWords();
        int correct = 0;
        for(int i = 0; i < Math.min(target.length, user.length); i++) {
            if(target[i].equals(user[i])) correct++;
        }
        return correct;
    }

    public static double calculateWPM(TestSession session, long durationMillis) {
        int correctWords = session.getCurrentWordIndex();
        double minutes = Math.max(durationMillis / 60000.0, 0.001);
        return correctWords / minutes;
    }

    public static double calculateAccuracy(TestSession session) {
        int total = session.getCorrectChars() + session.getIncorrectChars();
        return total > 0 ? ((double) session.getCorrectChars() / total) * 100 : 0;
    }

    public static double calculateWordPercentage(int correctWords, int totalTyped) {
        return totalTyped > 0 ? ((double) correctWords / totalTyped) * 100 : 0;
    }

    public static double getDurationSeconds(long durationMillis) {
        return durationMillis / 1000.0;
    }
}