package com.yufandong.vocabflashcard.utility;

import android.content.Context;
import android.util.Xml;

import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Flashcard;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for parsing XML files into VocabSets
 */
public class XmlUtility {

    private static final String ns = null;

    public static VocabSet parse(int resId, Context context) throws XmlPullParserException, IOException {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(resId);

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();

            return readFeed(parser);
        } finally {
            if(inputStream != null)
                inputStream.close();
        }
    }

    private static VocabSet readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        VocabSet entries = new VocabSet();

        parser.require(XmlPullParser.START_TAG, ns, "set");
        String setName = parser.getAttributeValue(null, "name");
        entries.setName(setName);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("word")) {
                entries.getList().add(readEntry(parser));
            }
            else {
                skip(parser);
            }
        }
        return entries;

    }

    private static Flashcard readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "word");
        String front = null;
        String back = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("front")) {
                parser.require(XmlPullParser.START_TAG, ns, "front");
                front = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "front");
            }
            else if (name.equals("back")) {
                parser.require(XmlPullParser.START_TAG, ns, "back");
                back = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "back");
            }
            else {
                skip(parser);
            }
        }
        Flashcard newFlashcard = new Flashcard();
        newFlashcard.setFront(front);
        newFlashcard.setBack(back);
        return newFlashcard;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {

        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }


    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
