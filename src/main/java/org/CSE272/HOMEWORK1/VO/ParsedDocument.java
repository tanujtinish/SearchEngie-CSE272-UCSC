package org.CSE272.HOMEWORK1.VO;

import org.apache.lucene.document.Document;

import java.util.List;

public class ParsedDocument {
    private String documentId;

    private String documentId2;

    private int rank;

    private Document luceneDocument;
    private String content;

    private String fullContent;

    private List<String> subTopics;

    private List<String> authors;

    private List<String> categories;

    private String Topic;

    private float score;

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentId2() {
        return documentId2;
    }

    public int getRank() {
        return rank;
    }

    public Document getLuceneDocument() {
        return luceneDocument;
    }

    public String getContent() {
        return content;
    }

    public String getFullContent() {
        return fullContent;
    }

    public List<String> getSubTopics() {
        return subTopics;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getTopic() {
        return Topic;
    }

    public float getScore() {
        return score;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setDocumentId2(String documentId2) {
        this.documentId2 = documentId2;
    }

    public void setLuceneDocument(Document luceneDocument) {
        this.luceneDocument = luceneDocument;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public void setSubTopics(List<String> subTopics) {
        this.subTopics = subTopics;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ParsedDocument{" +
                "documentId='" + documentId + '\'' +
                ", documentId2='" + documentId2 + '\'' +
                ", luceneDocument=" + luceneDocument +
                ", content='" + content + '\'' +
                ", fullContent='" + fullContent + '\'' +
                ", subTopics=" + subTopics +
                ", authors=" + authors +
                ", categories=" + categories +
                ", Topic='" + Topic + '\'' +
                '}';
    }
}
