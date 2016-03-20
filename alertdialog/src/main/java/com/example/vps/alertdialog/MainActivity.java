package com.example.vps.alertdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    static String LOG_TAG = "andmtr_mylog";
    public static String[] data = {"One", "Two", "Three"};
    public Bundle dialog_args = new Bundle();
    public ItemsDialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog_args.putStringArray("data", data);
        myDialog = new ItemsDialog();
        myDialog.setArguments(dialog_args);




    }

    @Override
    protected void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArray("data", data);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        data = savedInstanceState.getStringArray("data");
    }

     public void onButtonClick(View view) {
        myDialog.show(getFragmentManager(), "ttt");
    }

    public static class ItemsDialog1 extends DialogFragment{
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("Select test")
                    .setItems(data, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(LOG_TAG, "which = " + which);
                        }
                    });
            return adb.create();
        }
    }

}
