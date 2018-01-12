package com.myapps.android;

import com.salesforce.androidsdk.rest.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rostyk_haidukevych on 1/10/18.
 */

public class StaticSources {
    public static RestClient restClient = null;
    public static JSONObject tournamentSelected = null;
    public static JSONArray players = new JSONArray();


    public static JSONObject getPlayerById(String id) throws JSONException {
        for (int i = 0; i < players.length(); i++) {
            if (players.getJSONObject(i).get("Id").toString().contains(id))
                return players.getJSONObject(i);
        }
        return null;
    }

}
