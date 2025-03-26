import java.util.Scanner;

public class TypingSpeedTest {
    private abstract class TestMode {
        protected String modeName;
        protected int value;

        //constructor
        public TestMode(String modeName, int value) {
            this.modeName = modeName;
            this.value = value;
        }

        public abstract void setupTest(TestSession session);
        public abstract boolean isComplete(TestSession session);
    }

    class TimeLimitedMode extends TestMode {
        public TimeLimitedMode(int seconds) {
            super("Time Limited: " + seconds + " seconds", seconds);
        }

        @Override
        public void setupTest(TestSession session) {
            session.setTargetWords(CommonWords.getRandomWords(100));
        }

        @Override
        public boolean isComplete(TestSession session) {
            long elapsed = System.currentTimeMillis() - session.getStartTime();
            return elapsed *= 1000;
        }
    }

    class WordLimitedMode extends TestMode {
        public WordLimitedMode(int wordCount) {
            super("Word Limited: " + wordCount + " words", wordCount);
        }

        @Override
        public void setupTest(TestSession session) {
            session.setTargetWords(CommonWords.getRandomWords(value));
        }

        @Override
        public boolean isComplete(TestSession session) {
            return session.getCurrentWordIndex >= value;
        }
    }

    class TestSession {
        private TestMode mode; //current mode
        private String[] targetWords; //words to type
        private String[] userWords; //user's typed words
        private int currentWordIndex; //progress tracker
        private int correctChars; //correct character count
        private int incorrectChars; //Incorrect character count
        private long startTime; //test start time

        public TestSession(TestMode mode) {
            this.mode = mode;
            mode.setupTest(this);
        }

        public void start() {
            this.startTime = System.currentTimeMillis();
            this.userWords = new String[targerWords.length];
        }

        //a method to process a single word from a user and its characters.

        public boolean isComplete() {}

        //getters
        public String[] getTargetWords() { return targetWords; }
        public String[] getCurrentWordIndex() { return currentWordIndex; }
        public String[] getStartTime() { return startTime; }
        public String[] getCorrectChars() { return correctChars; }
        public String[] getIncorrectChars() { return incorrectChars; }

        //setters
        public void setTargetWords(String[] words) { targetWords = words; }
    }

    //bank of words
    class CommonWords {
        private static final String[] Words = {/*100 words*/}
        public static String[] getRandomWords(int count) { /*words randomizer*/}
    }

    class WordChecker {
        //compares words and characters
    }

    class displayText {
        //method that shows the words clears console and collects input

        private static void showTargetWords(TestSession session) {}

        //formatting...

        private static void clearConsole() {}
    }

    private static TestMode selectMode() {
        //
    }

    public static void main(String[] args) {
        //create testmode

        session.start()
    }

    pricate static void showResults(TestSession session) {}
}

