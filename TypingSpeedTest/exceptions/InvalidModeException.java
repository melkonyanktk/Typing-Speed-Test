package TypingSpeedTest.exceptions;

// This exception will be thrown, when the user selects any invalid test mode in the first menu
public class InvalidModeException extends RuntimeException {
    public InvalidModeException(String message) {
        super(message);
    }
}