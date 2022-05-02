package org.CSE272.HOMEWORK1;

public class Constants {

    public static final String documentFilePath = "src/main/resources/ohsumed.88-91";

    public static final String queryFilePath = "src/main/resources/query.ohsu.1-63";

    public static final int searchDocsNum = 50;
    public static final int searchMaxDocsNum = 50000;
    public static final int relevanceFeedbackDocsNum = 5;

    private static String rankingAlgorithm;

    public static String getRankingAlgorithm() {
        return rankingAlgorithm;
    }

    public static void setRankingAlgorithm(String rankingAlgorithm) {
        Constants.rankingAlgorithm = rankingAlgorithm;
    }

}
