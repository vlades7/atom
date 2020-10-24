package com.vlad;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BullsnCowsTest {

    @Test
    public void computeCowsBulls() {
        Game game = new Game(null);
        game.word = "apple";
        int[] result = game.computeCowsBulls("apple");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(5, result[1]);

        result = game.computeCowsBulls("paple");
        Assert.assertEquals(2, result[0]);
        Assert.assertEquals(3, result[1]);

        result = game.computeCowsBulls("vwxyz");
        Assert.assertEquals(0, result[0]);
        Assert.assertEquals(0, result[1]);
    }

    @Test
    public void getWords() {
        List<String> words = BullsnCows.getWords("dictionary.txt");
        Assert.assertEquals(52976, words.size());
    }
}
