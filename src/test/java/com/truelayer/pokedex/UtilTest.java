package com.truelayer.pokedex;

import com.truelayer.pokedex.util.Constants;
import com.truelayer.pokedex.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UtilTest {

    @Test
    public void testRemoveNewLineFromString() {
        String text = "Hello\nWorld";
        String finalText = "Hello World";
        Assert.assertNotEquals(finalText, text);
        String textWithRemovedNewLine = Util.removeEscapeSequence(text);
        Assert.assertEquals(finalText, textWithRemovedNewLine);
    }

    @Test
    public void testRemoveTabEscapeCharFromString() {
        String text = "Hello\tWorld";
        String finalText = "Hello World";
        Assert.assertNotEquals(finalText, text);
        String textWithRemovedNewLine = Util.removeEscapeSequence(text);
        Assert.assertEquals(finalText, textWithRemovedNewLine);
    }
}
