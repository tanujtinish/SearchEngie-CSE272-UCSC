package org.CSE272.HOMEWORK1.VoTransformation;

import org.CSE272.HOMEWORK1.VO.DocumentField;
import org.CSE272.HOMEWORK1.Utils;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import java.util.Arrays;

public class VoTransformations {

    public static Document convertParsedDocumentToDocument(ParsedDocument parsedDocument){

        Document document = new Document();

        //index file contents
        Field contentField = new DocumentField("Content",parsedDocument.getFullContent(),Field.Store.NO);

        //index categories
        Field categoriesField = new StringField("Categories", Utils.concatenate(parsedDocument.getCategories()),Field.Store.YES);
        //index documentId
        Field documentIdField = new StringField("DocumentId",parsedDocument.getDocumentId(),Field.Store.YES);
        Field documentId2Field = new StringField("DocumentId2",parsedDocument.getDocumentId2(),Field.Store.YES);
//index authors name
        Field authorsField = new StringField("AuthorNames",Utils.concatenate(parsedDocument.getAuthors()),Field.Store.YES);
        //index Title
        Field titleField = new StringField("Topic",parsedDocument.getTopic(),Field.Store.YES);

        document.add(contentField);
        document.add(categoriesField);
        document.add(documentIdField);
        document.add(documentId2Field);
        document.add(authorsField);
        document.add(titleField);


        return document;
    }

    public static ParsedDocument convertDocumentToParsedDocument(Document document){

        ParsedDocument parsedDocument = new ParsedDocument();

        parsedDocument.setDocumentId(document.get("DocumentId"));
        parsedDocument.setDocumentId2(document.get("DocumentId2"));
        parsedDocument.setLuceneDocument(document);
//        parsedDocument.setFullContent(document.get("Content"));
//        if(document.get("Content")!=null && document.get("Content").length()>0) {
//            if(document.get("Content").split(" ").length>2)
//                parsedDocument.setContent(document.get("Content").split(" ")[2]);
//        }
//        if(document.get("Content")!=null && document.get("Content").length()>0)
//        {
//            if(document.get("Content").split(" ").length>1)
//                if(document.get("Content").split(" ")[1]!=null && document.get("Content").split(" ")[1].length()>0)
//                    parsedDocument.setSubTopics(Arrays.asList(document.get("Content").split(" ")[1].split(" ")));
//        }
        if(document.get("AuthorNames")!=null && document.get("AuthorNames").length()>0)
        {
            parsedDocument.setAuthors(Arrays.asList(document.get("AuthorNames").split(" ")));
        }
        if(document.get("Categories")!=null && document.get("Categories").length()>0)
        {
            parsedDocument.setAuthors(Arrays.asList(document.get("Categories").split(" ")));
        }
        parsedDocument.setTopic(document.get("Topic"));


        return parsedDocument;
    }




}
