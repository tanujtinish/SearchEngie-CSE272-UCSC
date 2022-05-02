package org.CSE272.HOMEWORK1.VO;

public class LogFileResultRow {

    private ParsedQuery parsedQuery;
    private ParsedDocument parsedDocument;

    public ParsedQuery getParsedQuery() {
        return parsedQuery;
    }

    public ParsedDocument getParsedDocument() {
        return parsedDocument;
    }


    public void setParsedQuery(ParsedQuery parsedQuery) {
        this.parsedQuery = parsedQuery;
    }

    public void setParsedDocument(ParsedDocument parsedDocument) {
        this.parsedDocument = parsedDocument;
    }

}
