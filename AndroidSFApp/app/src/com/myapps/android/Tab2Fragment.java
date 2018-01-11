package com.myapps.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.smartstore.app.SmartStoreSDKManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Tab2Fragment extends Fragment {
   private static final String TAG = "Tab2Fragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        final TextView textView = view.findViewById(R.id.text_tab2);
        UserAccount currentAccount = SmartStoreSDKManager.getInstance().getUserAccountManager().getCurrentUser();
            String endpoint = currentAccount.getInstanceServer()+"/services/data/"+ ApiVersionStrings.getVersionNumber(getActivity())+"/sobjects/Account/";
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder().header("Authorization", "Bearer "+currentAccount.getAuthToken())
//                    .header("Accept", "application/json")
//                    .url(endpoint)
//                    .build();
//            Response response = null;
//            final String[] responseMsg = {""};
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                responseMsg[0] = e.getMessage();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                responseMsg[0] = response.body().toString();
//                responseMsg[0] = ""+response.code();
//                responseMsg[0] = "response => "+response.isSuccessful();
//                //updateUI(response.body().string());
//            }
//        });
        return view;
    }


    void updateUI(String text) {
        TextView textView = getView().findViewById(R.id.text_tab2);
        textView.setText("Ui text: "+text);
    }

    private TableRow makeUserCredentialsRow(){
        UserAccount currentAccount = SmartStoreSDKManager.getInstance().getUserAccountManager().getCurrentUser();
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tableRow.addView(makeTextColumn(currentAccount.getAuthToken()),0);
        tableRow.addView(makeTextColumn(currentAccount.getInstanceServer()),1);
        tableRow.addView(makeTextColumn(currentAccount.getLoginServer()),2);
        tableRow.addView(makeTextColumn(currentAccount.getOrgId()),3);
        tableRow.addView(makeTextColumn(currentAccount.getRefreshToken()),4);
        return tableRow;
    }

    private TextView makeTextColumn(String text){
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PT,10);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(10,10,0,0);
        return textView;
    }

}
