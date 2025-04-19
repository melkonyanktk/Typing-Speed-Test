package TypingSpeedTest;
import java.io.*;
import java.util.Scanner;

public class WordPicker {
    public static void main(String[] args) {
        String javaFile = "CommonWords.java";
        String[] words = new String[100];
        int wordCount = 0;

        try {
            File file = new File(javaFile);
            Scanner scanner = new Scanner(file);
            
            // Skip Java code structure and look for word declarations
            while (scanner.hasNextLine() && wordCount < 100) {
                String line = scanner.nextLine().trim();
                
                // Look for lines containing word definitions like: "apple", "banana",
                if (line.matches(".*\".+\".*")) {  // Finds lines with quotes
                    String[] quotedWords = line.split("\"");
                    for (String word : quotedWords) {
                        if (word.matches("[a-zA-Z]+")) {  // Only alphabetic words
                            words[wordCount++] = word;
                        }
                    }
                }
            }
            scanner.close();

            System.out.println("Words loaded from Java file:");
            for (int i = 0; i < wordCount; i++) {
                System.out.println(words[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File '" + javaFile + "' not found!");
        }
    }
}