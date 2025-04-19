package TypingSpeedTest;

import TypingSpeedTest.core.TestSession;

import java.util.Scanner;

public class TypingSpeedTest {
    private static TestMode selectMode() {
        //
        return null; //fornow
    }

    public static void main(String[] args) {
        TestMode mode = selectMode();
        TestSession session = new TestSession(mode);
        Scanner scanner = new Scanner(System.in);

        session.start();

        while(!session.isComplete()) {
            session.processWord(scanner.next());
        }

        showResults(session);
    }

    private static void showResults(TestSession session) {}

    //method that shows the words, clears console and collects input

    private static void showTargetWords(TestSession session) {}

    //formatting class

    private static void clearConsole() {}
}

