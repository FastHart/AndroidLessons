package com.example.vps.pingtest1;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<String> {

    EditText edit;
    TextView text;
    final static String TAG = "PingTest";
    static final int LOADER_ID = 1;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = (EditText)findViewById(R.id.editText1);
        edit.setText("127.0.0.1");
        text = (TextView)findViewById(R.id.textView1);

        bundle = new Bundle();
        getLoaderManager().initLoader(LOADER_ID, bundle, this);


    }


    public void onClick(View view) {
        String host = String.valueOf(edit.getText());
        bundle.putString("host", host);
        Loader<String> loader = getLoaderManager().restartLoader(LOADER_ID, bundle, this);
        loader.forceLoad();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = new PingAsyncLoader(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        text.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
