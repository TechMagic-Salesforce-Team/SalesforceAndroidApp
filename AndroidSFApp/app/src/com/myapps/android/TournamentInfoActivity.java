package com.myapps.android;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.myapps.android.com.sobjects.enums.Game__c;
import com.myapps.android.com.sobjects.enums.Player__c;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by rostyk_haidukevych on 1/12/18.
 */

public class TournamentInfoActivity extends ListActivity {

    private ListView gamesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONArray arr = null;
        try {
            loadAllGamesOfTournament(QueryMethods.getAllGamesWithAllFieldsByTournament(StaticSources.tournamentSelected.get("Id").toString()));
        } catch (JSONException e) {
            System.out.println("Exception while loading games");
            e.printStackTrace();
        }

        //System.out.println("size: "+arr.length());

//        gamesListView = findViewById(android.R.id.list);
//        this.setListAdapter(new ArrayAdapter<String>(
//                this, R.layout.mylist,
//                R.id.Itemname,itemname));

    }


    private void loadAllGamesOfTournament(String soql) {
        final RestRequest restRequest;
        //final JSONArray[] records = new JSONArray[1];
        try {
            restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);
            StaticSources.restClient.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(final RestRequest request, final RestResponse result) {
                    result.consumeQuietly(); // consume before going back to main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray records = result.asJSONObject().getJSONArray("records");
                                String[]values = new String[records.length()];
                                for (int i = 0; i < records.length(); i++) {
                                    JSONObject gameJson = records.getJSONObject(i);
                                    values[i] = StaticSources.getPlayerById(
                                            gameJson.get(Game__c.FIRST_COMPETITOR.toString()).toString()
                                    ).get(Player__c.NAME.toString())
                                            +" "+gameJson.get(Game__c.FIRST_COMPETITOR_SCORE.toString()).toString()+"\n"+
                                            StaticSources.getPlayerById(
                                                    gameJson.get(Game__c.SECOND_COMPETITOR.toString()).toString()
                                            ).get(Player__c.NAME.toString())+" "+gameJson.get(Game__c.SECOND_COMPETITOR_SCORE.toString()).toString()+"\n";
                                }
                                gamesListView = findViewById(android.R.id.list);
                                TournamentInfoActivity.this.setListAdapter(new ArrayAdapter<String>(
                                        TournamentInfoActivity.this, R.layout.mylist,
                                        R.id.Itemname,values));
                                gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Object item = parent.getItemAtPosition(position);
                                    System.out.println("Position: "+position);
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                }
                            });

                            } catch (Exception e) {
                                onError(e);
                            }
                        }
                    });
                }

                @Override
                public void onError(final Exception exception) {
                    new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TournamentInfoActivity.this,
                                    TournamentInfoActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                    Toast.LENGTH_LONG).show();
                        }
                    };
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}