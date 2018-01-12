package com.myapps.android.com.sobjects.enums;

/**
 * Created by rostyk_haidukevych on 1/12/18.
 */

public enum  Player__c {

    ID("Id"),NAME("Name"),EMAIL("Email__c"),
    IS_MANAGER("IsManager__c"), PASSWORD("Password__c"),
    STATUS("Status__c"), IMAGE("Image__c"),
    RATING("Rating__c"), OBJECT_NAME("Player__c");

    private final String value;

    Player__c(String value){
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }

}
