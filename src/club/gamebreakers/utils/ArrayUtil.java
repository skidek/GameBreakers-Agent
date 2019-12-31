package club.gamebreakers.utils;

import java.util.Arrays;

public class ArrayUtil {

    public static boolean contains(String[] haystack, String[] needles) {
        for (String needle : needles) {
            if (Arrays.stream(haystack).noneMatch(needle::equalsIgnoreCase)) return false;
        }
        return true;
    }

}