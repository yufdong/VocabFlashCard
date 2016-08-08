package com.yufandong.vocabflashcard.utility;

import java.util.List;
import java.util.Random;

/**
 * Provides utility methods involved with list manipulations.
 */
public class ListUtility {

    /**
     * Takes in a list and randomizes it via shuffling.
     *
     * @param list List to randomize
     * @param <E>  Type of the items in the list
     */
    public static <E> void randomizeList(List<E> list) {
        Random random = new Random();

        for (int i = list.size() - 1; i > 0; i--) {
            int rnum = random.nextInt(i + 1);
            E temp = list.get(i);
            list.set(i, list.get(rnum));
            list.set(rnum, temp);
        }
    }
}
