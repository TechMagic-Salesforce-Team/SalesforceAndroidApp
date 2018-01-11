package com.myapps.android;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.classes.Operation;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class HomeActivity extends SalesforceActivity {

    private RestClient client;
    private TableLayout tableLayout;
    private Boolean sfCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tableLayout = (TableLayout) findViewById(R.id.home_layout_table);
    }

    @Override
    public void onResume(RestClient client) {
        this.client = client;
        //if (StaticSources.restClient==null) StaticSources.restClient = client;
        try {
            if (tableLayout.getChildCount()>3) {
                tableLayout.removeViews(3, 1);
            } else {
                if (!sfCalled) {
                    loadAllTournaments("SELECT Id, Name, Status__c, Format__c, Type__c from Tournament__c");
                    sfCalled = true;
                }
            }
        } catch (Exception ex) {

        }
        System.out.println("Hello");
    }


    public void onLoadTournamentsClick(View v) throws UnsupportedEncodingException {
        if (tableLayout.getChildCount()>3) {
            tableLayout.removeViews(3, 1);
        }
        loadAllTournaments("SELECT Id, Name, Status__c, Format__c, Type__c from Tournament__c");
    }

    public void allTournamentsClick(View v) throws UnsupportedEncodingException {
        Intent operationsScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(operationsScreen);
    }

    private void loadAllTournaments(String soql) throws UnsupportedEncodingException {
        final RestRequest restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(this), soql);
        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(final RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray records = result.asJSONObject().getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject jsonObject = records.getJSONObject(i);
                                addTableRow(jsonObject);
                            }
                        } catch (Exception e) {
                            onError(e);
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this,
                                HomeActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void addTableRow(JSONObject jsonObject) throws JSONException{
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tableRow.addView(makeColumn((String) jsonObject.get("Name")),0);
        tableRow.addView(makeColumn((String) ""+jsonObject.get("Type__c").toString().split(" ")[0].charAt(0)+
                jsonObject.get("Type__c").toString().split(" ")[1].charAt(0)),1);
        tableRow.addView(makeColumn((String) jsonObject.get("Format__c")),2);
        tableRow.addView(makeColumn((String) jsonObject.get("Status__c")),3);
        tableRow.setPadding(0,10,0,0);
        tableLayout.addView(tableRow);
    }

    private TextView makeColumn(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PT,10);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(10,10,0,0);
        return textView;
    }

}
