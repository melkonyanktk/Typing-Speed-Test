package TypingSpeedTest.exceptions;

// This exception is thrown if words.txt fails to load
public class WordListException extends Exception {
    public WordListException(String message) {
        super(message); // Custom error message
    }
}