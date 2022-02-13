package com.truelayer.pokedex.util;

public class Util {

    public static String removeEscapeSequence(String text) {
        return text.replaceAll("[\\n\\t\\f]"," ");
    }
}
