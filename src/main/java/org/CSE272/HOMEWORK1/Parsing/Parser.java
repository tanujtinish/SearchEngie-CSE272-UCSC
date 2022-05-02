package org.CSE272.HOMEWORK1.Parsing;

import org.CSE272.HOMEWORK1.Constants;
import org.CSE272.HOMEWORK1.Utils;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.CSE272.HOMEWORK1.VO.ParsedQuery;
import org.CSE272.HOMEWORK1.VoTransformation.VoTransformations;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Parser {

    public static ArrayList<ParsedDocument> parseDocumentFile() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(Constants.documentFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String strLine;
        ArrayList<ParsedDocument> parsedDocumentList = new ArrayList<ParsedDocument>();
        try {
            ParsedDocument parsedDocument = new ParsedDocument();
            while ((strLine = reader.readLine()) != null) {
                if(Pattern.compile("\\.I [0-9]+").matcher(strLine).matches()){
                    parsedDocument.setDocumentId(strLine.split(" ")[1]);
                }
                else if(Pattern.compile("\\.U").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setDocumentId2(strLine);
                }
                else if(Pattern.compile("\\.S").matcher(strLine).matches()){
                    strLine = reader.readLine();
                }
                else if(Pattern.compile("\\.M").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setSubTopics(new ArrayList<>(Arrays.asList(strLine.split(";"))));
                }
                else if(Pattern.compile("\\.T").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setTopic(strLine);
                }
                else if(Pattern.compile("\\.P").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setCategories(new ArrayList<>(Arrays.asList(strLine.split(";"))));
                }
                else if(Pattern.compile("\\.W").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setContent(strLine);
                }
                else if(Pattern.compile("\\.A").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedDocument.setAuthors(new ArrayList<>(Arrays.asList(strLine.split(";"))));

                    parsedDocument.setFullContent(Utils.createFullContentForDocument(parsedDocument));

                    parsedDocument.setLuceneDocument(VoTransformations.convertParsedDocumentToDocument(parsedDocument));

                    parsedDocumentList.add(parsedDocument);
                    parsedDocument = new ParsedDocument();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedDocumentList;
    }

    public static ArrayList<ParsedQuery> parseQueryFile() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(Constants.queryFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        int queryNumber=0;
        String strLine;
        ArrayList<ParsedQuery> queryList = new ArrayList<ParsedQuery>();
        try {
            ParsedQuery parsedQuery=new ParsedQuery();
            while ((strLine = reader.readLine()) != null) {
                if(Pattern.compile("<num> Number: .*").matcher(strLine).matches()){
                    parsedQuery.setQueryId(strLine.split("<num> Number: ")[1]);
                }
                else if(Pattern.compile("<title> .*").matcher(strLine).matches()){
                    parsedQuery.setTitle(strLine.split("<title> ")[1]);
                }
                else if(Pattern.compile("<desc> Description:").matcher(strLine).matches()){
                    strLine = reader.readLine();
                    parsedQuery.setDescription(strLine);
                }
                else if(Pattern.compile("</top>").matcher(strLine).matches()){
                    String content= parsedQuery.getTitle() + " " + parsedQuery.getDescription();
//                    String content= parsedQuery.getDescription();
                    content= Utils.remove_stop_words(content);
                    parsedQuery.setContent(content);
                    parsedQuery.setQueryNumber("Q"+queryNumber);
                    queryList.add(parsedQuery);

                    queryNumber=queryNumber+1;
                    parsedQuery=new ParsedQuery();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return queryList;
    }

}
