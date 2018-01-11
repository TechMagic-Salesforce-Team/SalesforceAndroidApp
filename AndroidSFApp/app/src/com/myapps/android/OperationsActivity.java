package com.myapps.android;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toolbar;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import org.w3c.dom.Text;

/**
 * Created by rostyk_haidukevych on 12/28/17.
 */

public class OperationsActivity extends SalesforceActivity {

    @Override
    public void onResume(RestClient client) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operations);
        //TextView textView = findViewById(R.id.textInfo);
        //textView.setText("Operation screen");

        int BOOKSHELF_ROWS = SalesforceSDKManager.getOperations().size();

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        TableRow.LayoutParams aParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        aParams.topMargin = 2;
        aParams.rightMargin = 2;


        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tableRow.addView(makeTextView("Number", aParams), 0);
        tableRow.addView(makeTextView("Operation", aParams), 1);
        tableRow.addView(makeTextView("Object", aParams), 2);
        tableLayout.addView(tableRow, 0);

        for (int i = 1; i < BOOKSHELF_ROWS+1; i++) {
            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            tableRow.addView(makeTextView(""+SalesforceSDKManager.getOperations().get(i-1).getNumber(), aParams), 0);
            tableRow.addView(makeTextView(SalesforceSDKManager.getOperations().get(i-1).getAction(), aParams), 1);
            tableRow.addView(makeTextView(SalesforceSDKManager.getOperations().get(i-1).getObjectName(), aParams), 2);
            //tableRow.addView(makeTextView(SalesforceSDKManager.getOperations().get(i-1).getObjectName(), aParams), 3);
            //tableRow.addView(makeTextView("aaa", aParams), 2);
            tableLayout.addView(tableRow, i);
        }

    }


    private TextView makeTextView(String text, TableRow.LayoutParams params) {
        params.setMargins(12,12,0,0);
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextSize(30);
        textView.setLayoutParams(params);
        textView.setPadding(15,0,0,0);
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/drawable/owd.png";
        //Drawable d = Drawable.createFromPath(path);
        //d.setBounds(0,0,30,30);
        //textView.setBackground(d);
        /*
            works only with img formats
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shape, 0, 0, 0);
        */
        //textView.setBackgroundColor();
        return textView;
    }

}
