package org.CSE272.HOMEWORK1.SearchEngine;

import java.io.IOException;
import java.util.ArrayList;

import org.CSE272.HOMEWORK1.Parsing.Parser;
import org.CSE272.HOMEWORK1.Utils;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;


public class Indexer
{
    private IndexWriter writer;

    public IndexWriter getWriter() {
        return writer;
    }

    public Indexer() throws IOException
    {
        //this directory will contain the indexes
        Directory indexDirectory = new ByteBuffersDirectory();

        Analyzer analyzer = Utils.getAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        //create the indexer
        writer = new IndexWriter(indexDirectory, indexWriterConfig);
    }
    public void close() throws CorruptIndexException, IOException
    {
        writer.close();
    }

    public int createIndex() throws IOException
    {

        long startTime = System.currentTimeMillis();

        ArrayList<ParsedDocument> parsedDocsList= Parser.parseDocumentFile();
        for (ParsedDocument doc : parsedDocsList)
        {
            writer.addDocument(doc.getLuceneDocument());
        }
        long endTime = System.currentTimeMillis();
        System.out.println(" File indexed, time taken: " +(endTime-startTime)+" ms");

        return 1;
    }


}