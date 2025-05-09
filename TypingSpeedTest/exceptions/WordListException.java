package TypingSpeedTest.exceptions;

/**
 * This exception is thrown if words.txt fails to load
 */
public class WordListException extends Exception {
    public WordListException() {
        super("Word list exception");
    }

    public WordListException(String message) {
        super(message);
    }
}