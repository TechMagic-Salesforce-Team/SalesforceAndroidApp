package com.myapps.android;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.auth.OAuth2;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivityInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Tournament1Activity extends AppCompatActivity implements SalesforceActivityInterface {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPageAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private RestClient client;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament1);

        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tableLayout = (TableLayout) findViewById(R.id.home_layout_table);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        TextView textViewOauth = (TextView) findViewById(R.id.text_tab1);
//        if (textViewOauth == null) throw new NullPointerException("a");

        //textViewOauth.setText(OAuth2.getAccessToken());

    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;

        // Show everything
        findViewById(R.id.root).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLogoutComplete() {

    }

    @Override
    public void onUserSwitched() {

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Home");
        adapter.addFragment(new Tab2Fragment(), "Profile");
        viewPager.setAdapter(adapter);
    }


    public void allTournamentsClick(View v) throws UnsupportedEncodingException {
        Intent operationsScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(operationsScreen);
    }

    //public void onLoadTournamentsClick(View v) throws UnsupportedEncodingException {
        //if (tableLayout != null && tableLayout.getChildCount()>3) {
        //    tableLayout.removeViews(3, 1);
        //}
        //loadAllTournaments("SELECT Id, Name, Status__c, Format__c, Type__c from Tournament__c");
        //System.out.println("aaa");
    //}


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
                        Toast.makeText(Tournament1Activity.this,
                                Tournament1Activity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void addTableRow(JSONObject jsonObject) throws JSONException {
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



