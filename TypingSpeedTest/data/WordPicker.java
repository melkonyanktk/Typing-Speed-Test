package TypingSpeedTest.data;

import java.io.InputStream;
import java.util.Scanner;

public class WordPicker {
    private static final String[] WORDS = loadWords();
    private static final String WORDS_FILE = "TypingSpeedTest/data/words.txt"; // Relative to classpath root
    private static final int MAX_WORDS = 200;

    private static String[] loadWords() {
        String[] words = new String[MAX_WORDS];
        int count = 0;

        try (InputStream inputStream = WordPicker.class.getClassLoader().getResourceAsStream(WORDS_FILE);
             Scanner scanner = new Scanner(inputStream)) {

            if (inputStream == null) {
                System.err.println("Error: words.txt not found at classpath: " + WORDS_FILE);
                System.exit(1);
            }

            while(scanner.hasNextLine() && count < MAX_WORDS) {
                String word = scanner.nextLine().trim();
                if(!word.isEmpty()) words[count++] = word;
            }

            if(count < MAX_WORDS) {
                System.err.println("Error: File must contain " + MAX_WORDS + " non-empty words");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error loading words: " + e.getMessage());
            System.exit(1);
        }
        return words;
    }

    public static String[] getRandomWords(int count) {
        String[] selected = new String[count];
        for(int i = 0; i < count; i++) {
            selected[i] = WORDS[(int)(Math.random() * WORDS.length)];
        }
        return selected;
    }
}