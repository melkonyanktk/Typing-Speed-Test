package TypingSpeedTest;

import TypingSpeedTest.core.TestSession;
import TypingSpeedTest.core.TimeLimitedMode;
import TypingSpeedTest.core.WordLimitedMode;
import TypingSpeedTest.exceptions.InvalidModeException;
import java.util.Scanner;

public class TypingSpeedTest {

    public static void main(String[] args) {
        try {
            TestMode mode = selectMode();
            TestSession session = new TestSession(mode);
            Scanner scanner = new Scanner(System.in);

            showTargetWords(session);
            session.start();

            System.out.printf("%nStart typing! You have %d %s%n",
                    mode.getValue(),
                    mode instanceof TimeLimitedMode ? "seconds." : "words.");
            String input = scanner.nextLine();

            if (mode instanceof TimeLimitedMode) {
                ((TimeLimitedMode) mode).cancelTimer();
            }

            processInput(session, input);
            showResults(session);

        } catch (InvalidModeException e) {
            handleError("Mode selection error: " + e.getMessage(), "Please enter 1-5");
        } catch (Exception e) {
            handleError("Unexpected error: " + e.getMessage(), null);

        }
    }

    private static TestMode selectMode() throws InvalidModeException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose test mode:");
        System.out.println("1. 15 seconds\n2. 30 seconds\n3. 60 seconds");
        System.out.println("4. 50 words\n5. 100 words");
        System.out.print("Enter choice (1-5): ");

        return switch (scanner.nextInt()) {
            case 1 -> new TimeLimitedMode(15);
            case 2 -> new TimeLimitedMode(30);
            case 3 -> new TimeLimitedMode(60);
            case 4 -> new WordLimitedMode(50);
            case 5 -> new WordLimitedMode(100);
            default -> throw new InvalidModeException("Invalid choice");
        };
    }

    private static void processInput(TestSession session, String input) {
        String[] inputWords = input.trim().replaceAll("\\s+", " ").split(" ");
        int wordsToProcess = Math.min(inputWords.length, session.getTargetWords().length);

        for (int i = 0; i < wordsToProcess; i++) {
            session.processWord(inputWords[i]);
        }
    }

    private static void showTargetWords(TestSession session) {
        System.out.println("\n----- Type this paragraph -----");
        StringBuilder currentLine = new StringBuilder();
        int lineWidth = 80;

        for (String word : session.getTargetWords()) {
            if (currentLine.length() + word.length() + 1 > lineWidth) {
                System.out.println(currentLine);
                currentLine.setLength(0);
            }
            currentLine.append(word).append(" ");
        }

        if (!currentLine.isEmpty()) {
            System.out.println(currentLine);
        }
    }

    private static void showResults(TestSession session) {
        String[] targetWords = session.getTargetWords();
        String[] userWords = session.getUserWords();
        int correctWords = 0;

        for (int i = 0; i < Math.min(targetWords.length, userWords.length); i++) {
            if (userWords[i] != null &&
                    targetWords[i].replaceAll("[^a-zA-Z]", "").equalsIgnoreCase(
                            userWords[i].replaceAll("[^a-zA-Z]", ""))) {
                correctWords++;
            }
        }

        long durationMillis = System.currentTimeMillis() - session.getStartTime();
        double minutes = Math.max(durationMillis / 60000.0, 0.1);
        int totalChars = session.getCorrectChars() + session.getIncorrectChars();

        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Total words:" + userWords.length);
        System.out.printf("Correct words: %d/%d%n", correctWords, targetWords.length);
        System.out.printf("Correct characters: %d%n", session.getCorrectChars());

        if (durationMillis > 1000) {
            System.out.printf("WPM: %.1f%n", (correctWords / minutes));
        }

        if (totalChars > 0) {
            System.out.printf("Accuracy: %.1f%%%n",
                    ((double) session.getCorrectChars() / totalChars) * 100);
        }
    }

    private static void handleError(String message, String solution) {
        System.err.println("ERROR: " + message);
        if (solution != null) {
            System.err.println("SOLUTION: " + solution);
        }
        System.exit(1);
    }
}