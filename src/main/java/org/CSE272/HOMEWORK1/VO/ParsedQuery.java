package org.CSE272.HOMEWORK1.VO;

import org.apache.lucene.search.Query;

import java.util.List;
import java.util.Map;

public class ParsedQuery {

    private String queryId;

    private String queryNumber;

    private String description;

    private String title;

    private String content;

    private Query luceneQuery;

    private List<String> queryWordsInTopic;

    private List<String> queryWordsInDescription;

    Map<String, Long> queryTermFrequencies;

    public String getQueryId() {
        return queryId;
    }

    public String getQueryNumber() {
        return queryNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Query getLuceneQuery() {
        return luceneQuery;
    }

    public List<String> getQueryWordsInTopic() {
        return queryWordsInTopic;
    }

    public List<String> getQueryWordsInDescription() {
        return queryWordsInDescription;
    }

    public Map<String, Long> getQueryTermFrequencies() {
        return queryTermFrequencies;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setQueryNumber(String queryNumber) {
        this.queryNumber = queryNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLuceneQuery(Query luceneQuery) {
        this.luceneQuery = luceneQuery;
    }

    public void setQueryWordsInTopic(List<String> queryWordsInTopic) {
        this.queryWordsInTopic = queryWordsInTopic;
    }

    public void setQueryWordsInDescription(List<String> queryWordsInDescription) {
        this.queryWordsInDescription = queryWordsInDescription;
    }

    public void setQueryTermFrequencies(Map<String, Long> queryTermFrequencies) {
        this.queryTermFrequencies = queryTermFrequencies;
    }
}
