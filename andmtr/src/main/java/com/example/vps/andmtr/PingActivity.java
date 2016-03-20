package com.example.vps.andmtr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PingActivity extends Activity {

    // string array: {"item name", "item address"}
    public String[] item;
    public String item_address;
    // return string array: {"test name", "test type", "test interval", "test count","test resolve"}
    public String[] test;
    public String test_name, test_interval;
    public Integer test_resolve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        Intent intent = getIntent();
        //item = intent.getStringArrayExtra("item");
        //test = intent.getStringArrayExtra("test");

        //TextView text = (TextView) findViewById(R.id.text1);
        //text.setText("Item: " + String.valueOf(item) + " Test id: " + String.valueOf(test));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
