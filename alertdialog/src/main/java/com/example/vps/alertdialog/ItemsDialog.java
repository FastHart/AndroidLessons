package com.example.vps.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by vps on 22.11.15.
 */
public class ItemsDialog extends DialogFragment{



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //String[] data = savedInstanceState.getStringArray("data");
        String[] data = getArguments().getStringArray("data");
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Select test")
                .setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("andmtr_mylog", "which = " + which);
                    }
                });

        return adb.create();
    }


}

