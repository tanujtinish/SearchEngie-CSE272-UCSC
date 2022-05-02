package org.CSE272.HOMEWORK1.Ranking;

import org.CSE272.HOMEWORK1.Comparator.CustomComparator;
import org.CSE272.HOMEWORK1.SearchEngine.Reader;
import org.CSE272.HOMEWORK1.VO.LogFileResultRow;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.CSE272.HOMEWORK1.VO.ParsedQuery;
import org.CSE272.HOMEWORK1.VoTransformation.VoTransformations;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.*;

public class RankingUtils {

    static public List<LogFileResultRow> fetchLogFileResultRows(List<ParsedDocument> parsedDocuments, ParsedQuery parsedSearchQuery) throws IOException {

        List<LogFileResultRow> logFileResultRows= new ArrayList<>();

        for(ParsedDocument parsedDocument : parsedDocuments)
        {
            LogFileResultRow logFileResultRow= new LogFileResultRow();

            logFileResultRow.setParsedDocument(parsedDocument);
            logFileResultRow.setParsedQuery(parsedSearchQuery);
            logFileResultRows.add(logFileResultRow);
        }

        return logFileResultRows;
    }

    static public List<ParsedDocument> fetchParsedDocsFromHits(TopDocs hits, IndexSearcher indexSearcher) throws IOException {
        List<ParsedDocument> parsedDocuments = new ArrayList<>();
        int i=1;
        for(ScoreDoc scoreDoc : hits.scoreDocs)
        {
            Document document = indexSearcher.doc(scoreDoc.doc);
            ParsedDocument parsedDocument = VoTransformations.convertDocumentToParsedDocument(document);
            parsedDocument.setScore(scoreDoc.score);
            parsedDocument.setRank(i);
            i=i+1;
            parsedDocuments.add(parsedDocument);
        }

        return parsedDocuments;
    }

    static public void setQueryTermStats(ParsedQuery parsedSearchQuery, QueryParser queryParser, IndexSearcher indexSearcher) throws ParseException {
        Map<String, Long> queryTermFrequencies= new HashMap<>();

        Query titleQuery= queryParser.parse(QueryParser.escape(parsedSearchQuery.getTitle()));
        List<String> titleQueryTerms= Arrays.asList(titleQuery.toString().split(" "));
        List<String> queryWordsInTopic= new ArrayList<>();

        for(String queryTerm: titleQueryTerms){
            String x= queryTerm.split("Content:")[1];
            queryWordsInTopic.add(x);

            if(queryTermFrequencies.containsKey(queryTerm))
                queryTermFrequencies.put(queryTerm, queryTermFrequencies.get(queryTerm)+1);
            else
                queryTermFrequencies.put(queryTerm, 1L);

        }

        Query desQuery= queryParser.parse(QueryParser.escape(parsedSearchQuery.getDescription()));
        List<String> desQueryTerms= Arrays.asList(desQuery.toString().split(" "));
        List<String> queryWordsInDescription= new ArrayList<>();;

        for(String queryTerm: desQueryTerms){
            String x= queryTerm.split("Content:")[1];
            queryWordsInDescription.add(x);

            if(queryTermFrequencies.containsKey(x))
                queryTermFrequencies.put(x, queryTermFrequencies.get(x)+1);
            else{
                queryTermFrequencies.put(x, 1L);
//                System.out.println(x);
            }

        }

        parsedSearchQuery.setQueryWordsInDescription(queryWordsInDescription);
        parsedSearchQuery.setQueryWordsInTopic(queryWordsInTopic);
        parsedSearchQuery.setQueryTermFrequencies(queryTermFrequencies);
    }

    static float idf(long docFreq, long docCount) {
        return (float)(Math.log((double)(docCount + 1L) / (double)(docFreq + 1L)) + 1.0);
    }

    static float getQueryVectorMod(Map<String, Long> queryTermFrequencies, Map<String, Long> docFrequencies){
        float queryVectorMod=0;
        float querySize= queryTermFrequencies.size();

        for (Map.Entry<String,Long> entry : queryTermFrequencies.entrySet()){
            if(docFrequencies.containsKey(entry.getKey()))
                queryVectorMod=queryVectorMod + (entry.getValue()*idf(docFrequencies.get(entry.getKey()),Reader.totalDocs))/querySize;
            else
                queryVectorMod=queryVectorMod + (entry.getValue()*idf(0,Reader.totalDocs))/querySize;

        }

        return (float) Math.sqrt(queryVectorMod);
    }
    static float getDocumentVectorMod(Map<String, Long> documentTermFrequencies, Map<String, Long> docFrequencies){
        float documentVectorMod=0;
        float documentSize= documentTermFrequencies.size();

        for (Map.Entry<String,Long> entry : documentTermFrequencies.entrySet()){
            if(docFrequencies.containsKey(entry.getKey()))
                documentVectorMod=documentVectorMod + (entry.getValue()*idf(docFrequencies.get(entry.getKey()),Reader.totalDocs))/documentSize;
            else
                documentVectorMod=documentVectorMod + (entry.getValue()*idf(0,Reader.totalDocs))/documentSize;
        }

        return (float) Math.sqrt(documentVectorMod);
    }

    static void updateDocFrequency(Map<String, Long> docFrequencies, ParsedQuery parsedSearchQuery){
        for (Map.Entry<String,Long> entry : docFrequencies.entrySet()){
            if(parsedSearchQuery.getQueryWordsInTopic().contains(entry.getKey()) ){
                docFrequencies.put(entry.getKey(), entry.getValue()*3);
            }
            else if(parsedSearchQuery.getQueryWordsInTopic().contains(entry.getKey()) && parsedSearchQuery.getQueryWordsInDescription().contains(entry.getKey())){
                docFrequencies.put(entry.getKey(), entry.getValue()*2);
            }
        }
    }

    static float getNumTermsInDoc(Map<String, Long> documentTermFrequencies){
        float ans = 0;
        for (Map.Entry<String,Long> entry : documentTermFrequencies.entrySet()){
            ans=ans+entry.getValue();
        }
        return ans;
    }
    static public float getOwnDocumentScore(Map<String, Long> docFrequencies, Map<String, Long> documentTermFrequencies, ParsedQuery parsedSearchQuery ){
        Map<String, Long> queryTermFrequencies= parsedSearchQuery.getQueryTermFrequencies();
        updateDocFrequency(documentTermFrequencies, parsedSearchQuery);

        float score=0;

        float queryMod=getQueryVectorMod(queryTermFrequencies, docFrequencies);
        float documentMod=getDocumentVectorMod(documentTermFrequencies, docFrequencies);


        float documentSize= documentTermFrequencies.size();
        float querySize= queryTermFrequencies.size();
        for (Map.Entry<String,Long> entry : queryTermFrequencies.entrySet()) {
            float tfQuery= entry.getValue();
            if(documentTermFrequencies.containsKey(entry.getKey()))
            {
                float tfDocument= documentTermFrequencies.get(entry.getKey());
                float idf;

                if(docFrequencies.containsKey(entry.getKey()))
                    idf=idf(docFrequencies.get(entry.getKey()), Reader.totalDocs);
                else
                    idf=idf(0, Reader.totalDocs);


                float tfidf_query= (tfQuery*idf)/querySize;
                float tfidf_document= (tfDocument*idf)/documentSize;
                score+=tfidf_query*tfidf_document;
            }


        }

        score=score/(queryMod*documentMod);
        score=score/getNumTermsInDoc(documentTermFrequencies);
        return score;
    }

    static public List<ParsedDocument> fetchParsedDocsFromHits(TopDocs hits, ParsedQuery parsedSearchQuery, Reader engineReader, IndexSearcher indexSearcher) throws IOException {
        List<ParsedDocument> parsedDocuments= new ArrayList<>();

        int i=1;
        for(ScoreDoc scoreDoc : hits.scoreDocs)
        {
            Document document = indexSearcher.doc(scoreDoc.doc);
            ParsedDocument parsedDocument= VoTransformations.convertDocumentToParsedDocument(document);
            Map<String, Long> documentTermFrequencies = engineReader.getTermFrequencies(scoreDoc.doc);
            parsedDocument.setScore(getOwnDocumentScore( Reader.docFrequencies, documentTermFrequencies, parsedSearchQuery ));
            parsedDocument.setRank(i);
            i=i+1;
            parsedDocuments.add(parsedDocument);
        }

        parsedDocuments.sort(new CustomComparator());

        List<ParsedDocument> parsedDocumentsFinal= new ArrayList<>();
        for(i=0;i<50;i++)
        {
            parsedDocumentsFinal.add(parsedDocuments.get(i));
        }

        return parsedDocumentsFinal;
    }
}
