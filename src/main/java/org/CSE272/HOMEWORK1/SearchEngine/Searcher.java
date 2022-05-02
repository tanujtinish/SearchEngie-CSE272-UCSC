package org.CSE272.HOMEWORK1.SearchEngine;

import java.io.IOException;
import java.util.List;

import org.CSE272.HOMEWORK1.Ranking.Ranking;
import org.CSE272.HOMEWORK1.VO.LogFileResultRow;
import org.CSE272.HOMEWORK1.VO.ParsedQuery;
import org.apache.lucene.queryparser.classic.ParseException;

public class Searcher
{

    Ranking ranking;
    public Searcher(Ranking ranking)
    {
        this.ranking=ranking;
    }
    public List<LogFileResultRow> search(ParsedQuery parsedSearchQuery) throws IOException, ParseException {
        return ranking.ranking(parsedSearchQuery);
    }



}
