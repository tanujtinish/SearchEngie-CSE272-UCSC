package org.CSE272.HOMEWORK1;

import org.CSE272.HOMEWORK1.VO.LogFileResultRow;
import org.CSE272.HOMEWORK1.VO.ParsedDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.*;
import java.util.List;

public class Utils {

    public static String remove_stop_words(String str){
        String no_and=str.replace(" and ", " ");
        String no_AND = no_and.replace(" AND ", " ");
        String no_of=no_AND.replace(" of ", " ");
        String no_the=no_of.replace(" the ", " ");
        String no_in = no_the.replace(" in ", " ");
        String no_to = no_in.replace(" to ", " ");
        String no_a = no_to.replace(" a ", " ");
        String no_for = no_a.replace(" for ", " ");
        String no_comma = no_for.replace(", ", " ");
        String no_semi = no_comma.replace("; ", " ");
        String no_was = no_semi.replace(" was ", " ");
        String no_as = no_was.replace(" as ", " ");
        String no_at = no_as.replace(" at ", " ");
        String no_are = no_at.replace(" are ", " ");
        String no_were = no_are.replace(" were ", " ");
        String no_that = no_were.replace(" that ", " ");
        String no_slash = no_that.replace("/", "");
        String no_stop_words = no_slash;
        return no_stop_words;
    }

    public static Analyzer getAnalyzer(){
        return new StandardAnalyzer();
    }

    public static String concatenate(List<String> arr){

        if(arr==null || arr.size()==0)
            return "";

        StringBuilder sb = new StringBuilder(arr.get(0));

        for (String s : arr) {
            sb.append(" ").append(s);
        }

        return sb.toString();
    }



    public static String createFullContentForDocument(ParsedDocument document){

        return document.getTopic() + " " + concatenate(document.getSubTopics()) + " " + document.getContent() + " " + concatenate(document.getAuthors());
    }

    public static void printLogFileResultRows(List<LogFileResultRow> logFileResultRows){

        for(int i=0; i< logFileResultRows.size(); i++){
            String queryID= logFileResultRows.get(i).getParsedQuery().getQueryId();
            String queryNumber= logFileResultRows.get(i).getParsedQuery().getQueryNumber();
            String docID= logFileResultRows.get(i).getParsedDocument().getDocumentId2();
            int rank= logFileResultRows.get(i).getParsedDocument().getRank();
            float Score= logFileResultRows.get(i).getParsedDocument().getScore();
            String runID= Constants.getRankingAlgorithm();

            String s= queryID + ' ' + queryNumber + ' ' + docID + ' ' + rank + ' ' + Score + ' ' + runID;
            System.out.println(s);
        }
        System.out.println();

    }

    public static void generateLogFile(List<LogFileResultRow> logFileResultRows){

        File file=new File("");
        if(Constants.getRankingAlgorithm().equalsIgnoreCase("Boolean")){
            file = new File("/Users/tanujgupta/IdeaProjects/CSE272_HOMEWORK1/src/main/resources/logfileBoolean.txt");
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("Tf")){
            file = new File("/Users/tanujgupta/IdeaProjects/CSE272_HOMEWORK1/src/main/resources/logfileTf.txt");
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("tfidf-Both")){
            file = new File("/Users/tanujgupta/IdeaProjects/CSE272_HOMEWORK1/src/main/resources/logfileTfidfBoth.txt");
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("PseudoRelevanceFeedback")){
            file = new File("/Users/tanujgupta/IdeaProjects/CSE272_HOMEWORK1/src/main/resources/logfilePRF.txt");
        }
        else if(Constants.getRankingAlgorithm().equalsIgnoreCase("Own")){
            file = new File("/Users/tanujgupta/IdeaProjects/CSE272_HOMEWORK1/src/main/resources/logfileOwn.txt");
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for(int i=0; i< logFileResultRows.size(); i++){
                String queryID= logFileResultRows.get(i).getParsedQuery().getQueryId();
                String queryNumber= logFileResultRows.get(i).getParsedQuery().getQueryNumber();
                String docID= logFileResultRows.get(i).getParsedDocument().getDocumentId2();
                int rank= logFileResultRows.get(i).getParsedDocument().getRank();
                float Score= logFileResultRows.get(i).getParsedDocument().getScore();
                String runID= Constants.getRankingAlgorithm();

                String s= queryID + ' ' + queryNumber + ' ' + docID + ' ' + rank + ' ' + Score + ' ' + runID;
                bw.write(s);
                bw.newLine();
            }


            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
