package org.CSE272.HOMEWORK1.Ranking;

import org.apache.lucene.search.similarities.TFIDFSimilarity;

public class TfSimilarity extends TFIDFSimilarity {
    @Override
    public float tf(float freq) {
        return (float)Math.sqrt((double)freq);
    }

    @Override
    public float idf(long l, long l1) {
        return 1;
    }

    @Override
    public float lengthNorm(int numTerms) {
        return (float)(1.0 / Math.sqrt((double)numTerms));
    }
}
