package TypingSpeedTest.util;

import TypingSpeedTest.core.TestSession;

/**
 * Provides utilities for formatting display text in typing tests.
 */
public class TestDisplayUtil {

    /**
     * Formats target words into wrapped lines of specified width.
     *
     * @param session The test session containing target words
     * @param lineWidth Maximum characters per line including spaces
     * @return Formatted text with line breaks at specified width
     */
    public static String formatTargetText(TestSession session, int lineWidth) {
        StringBuilder sb = new StringBuilder();
        String[] words = session.getTargetWords();
        String currentLine = "";

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > lineWidth) {
                sb.append(currentLine.trim()).append("\n");
                currentLine = "";
            }
            currentLine += word + " ";
        }

        if (!currentLine.isEmpty()) {
            sb.append(currentLine.trim());
        }

        return sb.toString();
    }
}