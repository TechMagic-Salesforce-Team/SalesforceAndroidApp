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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Tab1Fragment extends Fragment {
   private static final String TAG = "Tab1Fragment";
   private TableLayout tableLayout;
   private TextView textView;
   private EditText nameInput;
   private Spinner statusSpinner;
   private Spinner formatSpinner;
   private Spinner typeSpinner;
   private Map<String, JSONObject> tournamentsSync = new HashMap<>();

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
            nameInput = view.findViewById(R.id.name_tournament_input);
            statusSpinner = view.findViewById(R.id.status_tournament_spinner);
            typeSpinner = view.findViewById(R.id.type_tournament_input);
            formatSpinner = view.findViewById(R.id.format_tournament_input);


            setTheSameOnChangeToSpinners(statusSpinner);
            setTheSameOnChangeToSpinners(typeSpinner);
            setTheSameOnChangeToSpinners(formatSpinner);

            addItemsToSpinner(statusSpinner, Arrays.asList("Upcoming", "Current", "Completed"), InputType.S);
            addItemsToSpinner(formatSpinner, Arrays.asList("1 x 1", "2 x 2"), InputType.F);
            addItemsToSpinner(typeSpinner, Arrays.asList("RR", "SE", "DE"), InputType.T);

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

            nameInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                    clearTable();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        //clearTable();
//                        loadAllTournamentsToTheTable("SELECT Id, Name, Status__c, Format__c, " +
//                                "Type__c from Tournament__c where Name like '%"+nameInput.getText().toString()+"%'");
                        findTournamentsByInputsAndFillTable();
                        //updateUI(nameInput.getText().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void setOnRowClick(TableRow row, final JSONObject tournament){
        row.setClickable(true);  //allows you to select a specific row
        row.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StaticSources.tournamentSelected = tournament;
                Intent tournamentActivity = new Intent(getContext(), TournamentInfoActivity.class);
                startActivity(tournamentActivity);
            }
        });
    }

    public void setTheSameOnChangeToSpinners(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                try {
                    clearTable();
                    findTournamentsByInputsAndFillTable();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void addItemsToSpinner(Spinner spinner, List<String> items, InputType inputType) {
        List<String> spinnerItems = new ArrayList<String>();
        spinnerItems.add(inputType.toString());
        spinnerItems.addAll(items);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerItems);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


//     void updateUI(String text) throws InterruptedException {
//            textView = getView().findViewById(R.id.text_tab1);
//            if (textView==null) {
//                Thread.sleep(1000);
//                updateUI(text);
//            }
//            textView.setText("Ui text: "+text);
//      }

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

        TextView typeText = makeColumn((String) ""+jsonObject.get("Type__c").toString().split(" ")[0].charAt(0)+
                jsonObject.get("Type__c").toString().split(" ")[1].charAt(0));

//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
//                new ViewGroup.MarginLayoutParams
//                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(10,0,0,0);
//        typeText.setLayoutParams(params);

        tableRow.addView(typeText,1);
        tableRow.addView(makeColumn((String) jsonObject.get("Format__c")),2);
        tableRow.addView(makeColumn((String) jsonObject.get("Status__c")),3);
        tableRow.setPadding(0,10,0,0);
        setOnRowClick(tableRow, jsonObject);
        tournamentsSync.put(jsonObject.get("Id").toString(), jsonObject);
        tableLayout.addView(tableRow);
    }

    private TextView makeColumn(String text) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(10,10,0,0);
        return textView;
    }

    private void loadAllTournamentsToTheTable(String soql) {
        tournamentsSync.clear();
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
        tableLayout.removeViews(4,tableLayout.getChildCount()-4);
    }

    private void findTournamentsByInputsAndFillTable() throws JSONException, InterruptedException {
        String name = nameInput.getText().toString();

        String type = typeSpinner.getSelectedItem().toString();

        switch (type) {
            case "RR" : type = "Round Robin"; break;
            case "SE" : type = "Single Elimination"; break;
            case "DE" : type = "Double Elimination"; break;
        }

        String format = formatSpinner.getSelectedItem().toString();
        String status = statusSpinner.getSelectedItem().toString();

        for (String key : tournamentsSync.keySet()) {
            if (tournamentsSync.get(key).get("Name").toString().toUpperCase().contains(name.toUpperCase())
                    &&
                    (tournamentsSync.get(key).get("Type__c").toString().equals(type) ||
                            type.equals(InputType.T.toString()))
                    &&
                    (tournamentsSync.get(key).get("Status__c").toString().equals(status) ||
                            status.equals(InputType.S.toString()))
                    &&
                    (tournamentsSync.get(key).get("Format__c").toString().equals(format) ||
                            format.equals(InputType.F.toString()))
                    ) {
                addTableRow(tournamentsSync.get(key));
            }
        }
    }


    enum InputType {
        S, F, T
    }

}
