package TypingSpeedTest.data;

import TypingSpeedTest.exceptions.WordListException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Provides word list management for typing tests.
 * Loads words from file and enables random word selection.
 */
public class WordPicker {
    /** Preloaded list of words from words.txt */
    private static final String[] WORDS;
    /** Path to words file in resources */
    private static final String WORDS_FILE = "TypingSpeedTest/data/words.txt";
    /** Required number of words in the file */
    private static final int MAX_WORDS = 200;

    // Static initializer block to handle the exception
    static {
        try {
            WORDS = loadWords();
        } catch (WordListException e) {
            // Wrap in RuntimeException since static initializers can't throw checked exceptions
            throw new RuntimeException("Failed to load word list: " + e.getMessage(), e);
        }
    }

    /**
     * Loads words from text file into memory.
     * @return Array of words from resource file
     * @throws WordListException if file not found or invalid
     */
    private static String[] loadWords() throws WordListException {
        String[] words = new String[MAX_WORDS];
        int count = 0;

        try (InputStream inputStream = WordPicker.class.getClassLoader().getResourceAsStream(WORDS_FILE);
             Scanner scanner = new Scanner(inputStream)) {

            if (inputStream == null) {
                throw new WordListException("words.txt not found at classpath: " + WORDS_FILE);
            }

            while(scanner.hasNextLine() && count < MAX_WORDS) {
                String word = scanner.nextLine().trim();
                if(!word.isEmpty()) words[count++] = word;
            }

            if(count < MAX_WORDS) {
                throw new WordListException("File must contain " + MAX_WORDS + " non-empty words");
            }
        } catch (Exception e) {
            throw new WordListException("Error loading words: " + e.getMessage());
        }
        return words;
    }

    /**
     * Selects random words from preloaded list.
     * @param count Number of words to select
     * @return Array of randomly selected words
     */
    public static String[] getRandomWords(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Word count must be positive");
        }
        String[] selected = new String[count];
        for(int i = 0; i < count; i++) {
            selected[i] = WORDS[(int)(Math.random() * WORDS.length)];
        }
        return selected;
    }
}