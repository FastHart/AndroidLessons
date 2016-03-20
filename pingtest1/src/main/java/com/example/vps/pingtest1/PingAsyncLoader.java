package com.example.vps.pingtest1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vps on 11.01.16.
 */
public class PingAsyncLoader extends AsyncTaskLoader <String> {

    final static String TAG = "PingTest";
    String host;

    public PingAsyncLoader(Context context, Bundle bundle) {
        super(context);
        this.host = bundle.getString("host");
    }

    @Override
    public String loadInBackground() {
        String pingResult = "";
        try {
            String pingCmd = "ping -c 10 " + host;
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Log.d(TAG, inputLine);
                pingResult += inputLine + "\n";
            }
            in.close();
        }//try
        catch (IOException e) {
            return null;
        }
        return pingResult;
    }
}
