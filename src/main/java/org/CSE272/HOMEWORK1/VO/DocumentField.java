package org.CSE272.HOMEWORK1.VO;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public class DocumentField extends Field {
    public static final FieldType TYPE_STORED = new FieldType();
    public static final FieldType TYPE_NOT_STORED = new FieldType();

    public DocumentField(String name, String value, Field.Store stored ) {
        super(name, value, stored == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
    }



    static {
        TYPE_NOT_STORED.setOmitNorms(true);
        TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        TYPE_NOT_STORED.setTokenized(true);
        TYPE_NOT_STORED.setStoreTermVectors(true);
        TYPE_NOT_STORED.freeze();

        TYPE_STORED.setOmitNorms(true);
        TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setTokenized(false);
        TYPE_STORED.freeze();
    }
}
