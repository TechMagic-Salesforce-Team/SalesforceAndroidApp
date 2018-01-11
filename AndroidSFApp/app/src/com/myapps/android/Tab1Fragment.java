package com.myapps.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class Tab1Fragment extends Fragment {
   private static final String TAG = "Tab1Fragment";
   private TableLayout tableLayout;
   //private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment, container, false);
        /*
         *  in onCreateView method - all the components are nullable, so we can not use getView().findViewById(id);
         */
        if (view != null) {
            tableLayout = view.findViewById(R.id.home_layout_table);
            //textView = view.findViewById(R.id.text_tab1);
            Button button = (Button) view.findViewById(R.id.load_tournaments_button);
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clearTable();
                    loadAllTournamentsToTheTable("SELECT Id, Name, Status__c, Format__c, Type__c from Tournament__c");
                }
            });
        } else {
            try {
                Thread.sleep(1000);
                onCreateView(inflater, container, savedInstanceState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        loadAllTournamentsToTheTable("SELECT Id, Name, Status__c, Format__c, Type__c from Tournament__c");
        return view;
    }

//        void updateUI(String text) throws InterruptedException {
//            textView = getView().findViewById(R.id.text_tab1);
//            if (textView==null) {
//                Thread.sleep(1000);
//                updateUI(text);
//            }
//            textView.setText("Ui text: "+text);
//        }

    private void addTableRow(JSONObject jsonObject) throws JSONException, InterruptedException {
        //tableLayout = (TableLayout) getView().findViewById(R.id.home_layout_table);
//        if (tableLayout==null) {
//            Thread.sleep(1000);
//            addTableRow(jsonObject);
//        }
        TableRow tableRow = new TableRow(getActivity());
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
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PT,10);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(10,10,0,0);
        return textView;
    }

    private void loadAllTournamentsToTheTable(String soql) {
        final RestRequest restRequest;
        try {
            restRequest = RestRequest.getRequestForQuery(ApiVersionStrings.getVersionNumber(getActivity()), soql);
            StaticSources.restClient.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(final RestRequest request, final RestResponse result) {
                    result.consumeQuietly(); // consume before going back to main thread
                    //updateUI(result.asJSONObject().getJSONArray("records").toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray records = result.asJSONObject().getJSONArray("records");
                                for (int i = 0; i < records.length(); i++) {
                                    JSONObject jsonObject = records.getJSONObject(i);
                                    addTableRow(jsonObject);
                                }
                                //updateUI(records.toString());
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
                            Toast.makeText(getActivity(),
                                    getActivity().getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                    Toast.LENGTH_LONG).show();
                        }
                    };
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void clearTable(){
        tableLayout.removeViews(3,1);
    }

}
