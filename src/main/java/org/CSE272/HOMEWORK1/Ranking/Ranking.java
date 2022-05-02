package org.CSE272.HOMEWORK1.Ranking;

import org.CSE272.HOMEWORK1.Comparator.CustomComparator;
import org.CSE272.HOMEWORK1.Constants;
import org.CSE272.HOMEWORK1.SearchEngine.Reader;
import org.CSE272.HOMEWORK1.Utils;
import org.CSE272.HOMEWORK1.VO.LogFileResultRow;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.CSE272.HOMEWORK1.VO.ParsedQuery;
import org.CSE272.HOMEWORK1.VoTransformation.VoTransformations;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

import java.io.IOException;
import java.util.*;

public class Ranking {

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Reader engineReader;

    boolean rf;
    public Ranking(IndexWriter writer) throws IOException {
        indexSearcher = new IndexSearcher(DirectoryReader.open(writer, true, true));
        queryParser = new QueryParser("Content", Utils.getAnalyzer());
        engineReader=new Reader(writer);
    }



    public List<LogFileResultRow> ranking(ParsedQuery parsedSearchQuery) throws IOException, ParseException {
//        System.out.println("Parsed parsedSearchQuery: ");
//        System.out.println(parsedSearchQuery.getLuceneQuery());

        Query query= queryParser.parse(QueryParser.escape(parsedSearchQuery.getContent()));
        RankingUtils.setQueryTermStats(parsedSearchQuery, queryParser, indexSearcher);
        parsedSearchQuery.setLuceneQuery(query);

        if(Constants.getRankingAlgorithm().equalsIgnoreCase("Boolean")){
            return booleanRanking(parsedSearchQuery);
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("Tf")){
            return tfRanking(parsedSearchQuery);
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("tfidf-Both")){
            return tfIdfRanking(parsedSearchQuery);
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("PseudoRelevanceFeedback")){
            return pseudoRelevanceFeedbackRanking(parsedSearchQuery);
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("Own")){
            return ownRanking(parsedSearchQuery);
        }

        return null;
    }

    public List<LogFileResultRow> booleanRanking(ParsedQuery parsedSearchQuery) throws IOException {

        indexSearcher.setSimilarity(new BooleanSimilarity());
        TopDocs hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.searchDocsNum);

        List<ParsedDocument> parsedDocs = RankingUtils.fetchParsedDocsFromHits(hits, indexSearcher);
        return RankingUtils.fetchLogFileResultRows(parsedDocs, parsedSearchQuery);
    }

    public List<LogFileResultRow> tfRanking(ParsedQuery parsedSearchQuery)throws IOException {

        indexSearcher.setSimilarity(new TfSimilarity());
        TopDocs hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.searchDocsNum);

        List<ParsedDocument> parsedDocs = RankingUtils.fetchParsedDocsFromHits(hits, indexSearcher);
        return RankingUtils.fetchLogFileResultRows(parsedDocs, parsedSearchQuery);
    }

    public List<LogFileResultRow> tfIdfRanking(ParsedQuery parsedSearchQuery)throws IOException {
        indexSearcher.setSimilarity(new ClassicSimilarity());
        TopDocs hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.searchDocsNum);

        List<ParsedDocument> parsedDocs = RankingUtils.fetchParsedDocsFromHits(hits, indexSearcher);

        if(rf)
            Constants.setRankingAlgorithm("PseudoRelevanceFeedback");
        return RankingUtils.fetchLogFileResultRows(parsedDocs, parsedSearchQuery);
    }

    private String expandQuery(String searchQuery, TopDocs hits) throws IOException {
        StringBuilder expandedQuery= new StringBuilder(searchQuery);

        for(ScoreDoc scoreDoc : hits.scoreDocs)
        {
            List<String> terms = engineReader.getAllTermsInDocument(scoreDoc.doc);
            for(String term : terms){
                expandedQuery.append(" ").append(term);
            }
        }

        return expandedQuery.toString();
    }

    public List<LogFileResultRow> pseudoRelevanceFeedbackRanking(ParsedQuery parsedSearchQuery) throws IOException, ParseException {
        rf=true;

        indexSearcher.setSimilarity(new ClassicSimilarity());
        TopDocs hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.relevanceFeedbackDocsNum);

        parsedSearchQuery.setContent(expandQuery(parsedSearchQuery.getContent(), hits));

        Constants.setRankingAlgorithm("tfidf-Both");
        return ranking(parsedSearchQuery);
    }

    public List<LogFileResultRow> ownRanking(ParsedQuery parsedSearchQuery) throws IOException {
        indexSearcher.setSimilarity(new ClassicSimilarity());
        TopDocs hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.relevanceFeedbackDocsNum);

        parsedSearchQuery.setContent(expandQuery(parsedSearchQuery.getContent(), hits));


        hits = indexSearcher.search(parsedSearchQuery.getLuceneQuery(),Constants.searchMaxDocsNum);

//        System.out.println("fetching for query: "+parsedSearchQuery.getContent());
        List<ParsedDocument> parsedDocs = RankingUtils.fetchParsedDocsFromHits(hits, parsedSearchQuery, engineReader, indexSearcher);
        return RankingUtils.fetchLogFileResultRows(parsedDocs, parsedSearchQuery);
    }

}
