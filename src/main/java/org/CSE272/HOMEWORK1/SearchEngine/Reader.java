package org.CSE272.HOMEWORK1.SearchEngine;

import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.SocketOption;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reader {

    static IndexReader reader;

    public static int totalDocs;

    public static Map<String, Long> docFrequencies;


    public Reader(IndexWriter writer) throws IOException {
        reader =  DirectoryReader.open(writer, true, true);

        totalDocs = reader.numDocs();
        docFrequencies=getDocFrequencies();
    }

    public Map<String, Long> getDocFrequencies() throws IOException {

        Map<String, Long> docFrequencies = new HashMap<>();

        Terms terms = MultiTerms.getTerms(reader, "Content");
        TermsEnum termsEnum = terms.iterator();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
//            System.out.println(term);
            int docFreq = termsEnum.docFreq();

            docFrequencies.put(term, (long) docFreq);
        }

        return docFrequencies;
    }

    public Map<String, Long> getTermFrequencies(int docId)
            throws IOException {
        Terms vector = reader.getTermVector(docId, "Content");
        TermsEnum termsEnum = vector.iterator();
        Map<String, Long> frequencies = new HashMap<>();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, (long) freq);
        }
        return frequencies;
    }

    public List<String> getAllTermsInDocument(int docId)
            throws IOException {
        Terms vector = reader.getTermVector(docId, "Content");
        TermsEnum termsEnum = vector.iterator();
        List<String> terms = new ArrayList<>();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            terms.add(text.utf8ToString());
        }
        return terms;
    }
}
