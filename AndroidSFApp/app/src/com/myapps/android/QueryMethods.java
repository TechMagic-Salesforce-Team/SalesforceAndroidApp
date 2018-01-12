package com.myapps.android;

import com.myapps.android.com.sobjects.enums.Game__c;
import com.myapps.android.com.sobjects.enums.Player__c;

/**
 * Created by rostyk_haidukevych on 1/12/18.
 */

public class QueryMethods {

    public static String getAllGamesWithAllFieldsByTournament(String tournamentId) {
        String query = "SELECT Id, "+ Game__c.FIRST_COMPETITOR+", "+Game__c.SECOND_COMPETITOR+", "+Game__c.FIRST_COMPETITOR_SCORE+", "+
                Game__c.SECOND_COMPETITOR_SCORE+ ", "+Game__c.FIRST_COMPETITOR_ACCEPT+", "+Game__c.SECOND_COMPETITOR_ACCEPT+", "+
                Game__c.STAGE+" from "+Game__c.OBJECT_NAME+" where Tournament__c = '"+tournamentId+"'";
        return query;
    }


    public static String getAllPlayersWithAllFields() {
        String query = "SELECT Id, "+ Player__c.NAME+", "+Player__c.EMAIL+", "+Player__c.IS_MANAGER+", "+
                Player__c.PASSWORD+ ", "+Player__c.STATUS+", "+Player__c.IMAGE+", "+
                Player__c.RATING+" from "+Player__c.OBJECT_NAME+" where "+Player__c.STATUS+" = 'Active'";
        return query;
    }

}
