package com.example.vps.andmtr;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static java.lang.System.nanoTime;

/**
 * Created by vps on 11.01.16.
 */
public class PingFragment extends Fragment {

    public static String LOG_TAG; //R.string.log_tag

    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(String line);
        void onCancelled();
        void onPostExecute(String line);
    }

    private TaskCallbacks mCallbacks;
    private PingTask mTask;
    public String ping_output_line = "";
    public Long timeout_ns = 0L;
    public Long timestamp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        LOG_TAG = getResources().getString(R.string.log_tag);
    }

/*
    public void onResume(){
        super.onResume();
        if (mCallbacks != null) {
            mCallbacks.onPostExecute(ping_output_line);
        }
    }
*/

    public void startPing (String command) {
        mTask = new PingTask();
        mTask.execute(command);
    }

    public void startPing (String command, int timeout) {
        this.timeout_ns = (long) timeout * 1000000000;
        mTask = new PingTask();
        mTask.execute(command);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class PingTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            ping_output_line = "";
            timestamp =  nanoTime();
            try {
                String pingCmd = params[0];
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(pingCmd);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                try {
                    int wf = p.waitFor();
                } catch (InterruptedException e) {
                }
                if ( p.exitValue() != 0 ){
                    String errLine;
                    while ((errLine = err.readLine()) != null) {
                        Log.d(LOG_TAG, errLine);
                        ping_output_line += "ERROR: " + errLine + "\n";
                    }
                    //return null;
                }
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    //Log.d(LOG_TAG, inputLine);
                    ping_output_line += inputLine + "\n";
                    //publishProgress(ping_output_line);
                }
                in.close();
            }//try
            catch (IOException e) {
                Log.d(LOG_TAG, "ERR!!" + params[0]);
                Log.d(LOG_TAG, e.getMessage());
                return null;
            }
            //do delay before exit task
            Long time_elapsed = timeout_ns - ( nanoTime() - timestamp );
            if ( time_elapsed >  0L ) {
                try {
                    TimeUnit.NANOSECONDS.sleep(time_elapsed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Log.d(LOG_TAG, e.getMessage());
                }
            }
            return ping_output_line;
        }

        @Override
        protected void onProgressUpdate(String... line) {
            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(line[0]);
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(String line) {
            ping_output_line = line;
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(line);
            }
        }

    }

}
