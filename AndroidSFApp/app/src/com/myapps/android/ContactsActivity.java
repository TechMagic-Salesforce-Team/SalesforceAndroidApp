package com.myapps.android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ContactsActivity extends SalesforceActivity {

    private RestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactsf);
    }

    @Override
    public void onResume(RestClient client) {
        this.client = client;
    }


    public void onAddContactClick(View v) throws UnsupportedEncodingException, IOException {
        final EditText nameEditText = findViewById(R.id.NameField);
        final EditText lastNameEditText = findViewById(R.id.LastNameField);

        String name = nameEditText.getText().toString();
        final String lastName = lastNameEditText.getText().toString();
        //String contactJson = "{\"FirstName\" : \""+name+"\", \"LastName\" : \""+lastName+"\"}";

        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("FirstName", name);
        fields.put("LastName", lastName);

        final RestRequest restRequest = RestRequest.getRequestForCreate(ApiVersionStrings.getVersionNumber(this), "Contact", fields);

        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
            @Override
            public void onSuccess(final RestRequest request, final RestResponse result) {
                result.consumeQuietly(); // consume before going back to main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nameEditText.setText("");
                            lastNameEditText.setText("");
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
                        Toast.makeText(ContactsActivity.this,
                                ContactsActivity.this.getString(SalesforceSDKManager.getInstance().getSalesforceR().stringGenericError(), exception.toString()),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


}
