package org.CSE272.HOMEWORK1.Run;

import org.CSE272.HOMEWORK1.Constants;
import org.CSE272.HOMEWORK1.Parsing.Parser;
import org.CSE272.HOMEWORK1.Ranking.Ranking;
import org.CSE272.HOMEWORK1.SearchEngine.Indexer;
import org.CSE272.HOMEWORK1.SearchEngine.Searcher;
import org.CSE272.HOMEWORK1.Utils;
import org.CSE272.HOMEWORK1.VO.LogFileResultRow;
import org.CSE272.HOMEWORK1.VO.ParsedQuery;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Run {
    Indexer indexer;
    Searcher searcher;
    Ranking ranking;

    static List<LogFileResultRow> results;

    public Run() throws IOException {
        results=new ArrayList<>();
        this.indexer = new Indexer();
    }

    public static void main(String[] args) throws IOException {
        Run tester;
        try
        {
            tester = new Run();
            tester.createIndex();

            ArrayList<ParsedQuery> queryList= Parser.parseQueryFile();

            for(ParsedQuery query :queryList) {
                Constants.setRankingAlgorithm("PseudoRelevanceFeedback");
                tester.search(query);
            }

            Utils.printLogFileResultRows(results);
//            Utils.generateLogFile(results);
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }
    private void createIndex() throws IOException
    {
        indexer.createIndex();
    }
    private void search(ParsedQuery parsedSearchQuery) throws IOException, ParseException {
        ranking = new Ranking(indexer.getWriter());
        searcher = new Searcher(ranking);

        List<LogFileResultRow> results = searcher.search(parsedSearchQuery);
//        Utils.printLogFileResultRows(results);

        if(results.size()>0)
            Run.results.addAll(results);
    }
}
