package TypingSpeedTest;

import TypingSpeedTest.core.TestSession;
import TypingSpeedTest.core.TimeLimitedMode;
import TypingSpeedTest.core.WordLimitedMode;
import java.util.Scanner;

public class TypingSpeedTest {

    public static void main(String[] args) {
        TestMode mode = selectMode();
        TestSession session = new TestSession(mode);
        Scanner scanner = new Scanner(System.in);

        showTargetWords(session);
        session.start();

        System.out.println("Start typing! You have " + mode.getValue() + " seconds.");
        String input = scanner.nextLine();

        if (mode instanceof TimeLimitedMode) {
            ((TimeLimitedMode) mode).cancelTimer();
        }

        processInput(session, input);
        showResults(session);

    }

    private static void processInput(TestSession session, String input) {

        String normalizedInput = input.trim().replaceAll("\\s+", " ");
        String[] inputWords = normalizedInput.split(" ");

        int wordsToProcess = Math.min(inputWords.length, session.getTargetWords().length);
        for (int i = 0; i < wordsToProcess; i++) {
            session.processWord(inputWords[i]);
        }
    }

    private static TestMode selectMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose test mode:");
        System.out.println("1. 15 seconds\n2. 30 seconds\n3. 60 seconds");
        System.out.println("4. 50 words\n5. 100 words");
        System.out.print("Enter choice (1-5): ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return new TimeLimitedMode(15);
            case 2:
                return new TimeLimitedMode(30);
            case 3:
                return new TimeLimitedMode(60);
            case 4:
                return new WordLimitedMode(50);
            case 5:
                return new WordLimitedMode(100);
            default:
                throw new IllegalArgumentException("Invalid choice");
        }
    }

    private static void showTargetWords(TestSession session) {
        System.out.println("\n----- Type this paragraph -----");
        String[] words = session.getTargetWords();
        int lineWidth = 80;
        String currentLine = "";

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (currentLine.length() + word.length() + 1 > lineWidth) {
                System.out.println(currentLine);
                currentLine = "";
            }
            currentLine += word + " ";
        }

        if (!currentLine.isEmpty()) {
            System.out.println(currentLine);
        }
    }
    private static void showResults(TestSession session) {
        String[] targetWords = session.getTargetWords();
        String[] userWords = session.getUserWords();

        int correctWords = 0;
        int correctChars = session.getCorrectChars();
        int incorrectChars = session.getIncorrectChars();

        int wordsToCompare = Math.min(targetWords.length, userWords.length);

        for (int i = 0; i < wordsToCompare; i++) {
            if (userWords[i] == null) continue;

            String cleanTarget = targetWords[i].replaceAll("[^a-zA-Z]", "").toLowerCase();
            String cleanUser = userWords[i].replaceAll("[^a-zA-Z]", "").toLowerCase();

            if (cleanTarget.equals(cleanUser)) {
                correctWords++;
            }
        }

        long durationMillis = System.currentTimeMillis() - session.getStartTime();
        double minutes = Math.max(durationMillis / 60000.0, 0.1);

        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Total words:" + userWords.length);
        System.out.printf("Correct words: %d/%d%n", correctWords, targetWords.length);
        System.out.printf("Correct characters: %d%n", correctChars);

        if (durationMillis > 1000) {
            System.out.printf("WPM: %.1f%n", (correctWords / minutes));
        } else {
            System.out.println("WPM: Test duration too short for accurate measurement");
        }

        if (correctChars + incorrectChars > 0) {
            System.out.printf("Accuracy: %.1f%%%n",
                    ((double) correctChars / (correctChars + incorrectChars)) * 100);
        } else {
            System.out.println("Accuracy: No characters typed");
        }
    }
}

