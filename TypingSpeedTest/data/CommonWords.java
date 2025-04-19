package TypingSpeedTest.data;

public class CommonWords {
    private static final String[] Words = {/*100 words*/};

    public static String[] getRandomWords(int count) {
        String[] selected = new String[count];
        for(int i = 0; i < count; i++) {
            selected[i] = Words[(int)(Math.random() * Words.length)];
        }
        return selected;
    }
}