package org.CSE272.HOMEWORK1.Comparator;

import org.CSE272.HOMEWORK1.VO.ParsedDocument;

import java.util.Comparator;

public class CustomComparator implements Comparator<ParsedDocument> {
    @Override
    public int compare(ParsedDocument o1, ParsedDocument o2) {
        if( o1.getScore() < o2.getScore())
            return 1;

        return 0;
    }
}
