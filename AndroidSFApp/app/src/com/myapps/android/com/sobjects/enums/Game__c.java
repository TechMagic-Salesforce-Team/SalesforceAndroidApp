package com.myapps.android.com.sobjects.enums;

/**
 * Created by rostyk_haidukevych on 1/12/18.
 */

public enum Game__c {
    FIRST_COMPETITOR("FirstCompetitor__c"), SECOND_COMPETITOR("SecondCompetitor__c"),
    FIRST_COMPETITOR_SCORE("FirstCompetitorScore__c"), SECOND_COMPETITOR_SCORE("SecondCompetitorScore__c"),
    FIRST_COMPETITOR_ACCEPT("FirstCompetitorAccept__c"), SECOND_COMPETITOR_ACCEPT("SecondCompetitorAccept__c"),
    STAGE("Stage__c"), OBJECT_NAME("Game__c");

    private final String value;

     Game__c(String value){
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }


}
