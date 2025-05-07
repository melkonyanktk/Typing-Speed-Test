package TypingSpeedTest;
import TypingSpeedTest.exceptions.WordListException;
import java.io.InputStream;
import java.util.Scanner;

public final class WordPicker {
    private static final String[] WORDS;
    private static final String WORDS_FILE = "/TypingSpeedTest/data/words.txt";
    private static final int MAX_WORDS = 200;

    static {
        WORDS = initializeWords();
    }

    private WordPicker() {}

    private static String[] initializeWords() {
        try {
            return loadWords();
        } catch (WordListException e) {
            throw new IllegalStateException("Failed to initialize word list", e);
        }
    }

    private static String[] loadWords() throws WordListException {
        String[] words = new String[MAX_WORDS];
        int count = 0;

        try (InputStream inputStream = WordPicker.class.getResourceAsStream(WORDS_FILE);
             Scanner scanner = new Scanner(inputStream)) {

            if (inputStream == null) {
                throw new WordListException("Word file not found: " + WORDS_FILE);
            }

            while (scanner.hasNextLine() && count < MAX_WORDS) {
                String word = scanner.nextLine().trim();
                if (!word.isEmpty()) {
                    words[count++] = word;
                }
            }

            if (count < MAX_WORDS) {
                throw new WordListException(
                        String.format("Insufficient words. Found %d, need %d", count, MAX_WORDS)
                );
            }
        } catch (Exception e) {
            throw new WordListException("Error loading words");
        }
        return words;
    }
}