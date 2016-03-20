package com.example.vps.p0231_oneactivitystate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    final String TAG ="States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Main on create");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Main on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"Main on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"Main on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"Main on stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Main on destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Main on restart");
    }

    public void onClickBut(View view) {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
}
