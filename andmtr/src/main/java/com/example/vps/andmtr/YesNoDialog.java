package com.example.vps.andmtr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class YesNoDialog extends DialogFragment implements DialogInterface.OnClickListener, TextWatcher {

    public String type;
    public String host;
    public String ip;
    public LinearLayout lv;
    public EditText host_field;
    public EditText ip_field;
    public AlertDialog thisDialog;
    public Button positiveButton;

    @Override
    public void onStart(){
        super.onStart();
        positiveButton = thisDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if ( type.equals("edit_host")  || type.equals("add_host") ) {
            positiveButton.setEnabled(false);
        }
    }

    public interface dialogResult {
        void onResultCallback (String type, String host, String ip);
    }
    private dialogResult mydialogResult;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mydialogResult = (dialogResult) activity;
        lv = new LinearLayout(activity);
        lv.setOrientation(LinearLayout.VERTICAL);
        host_field = new EditText(activity);
        host_field.setId(R.id.host_field);
        ip_field = new EditText(activity);
        ip_field.setId(R.id.ip_field);
        lv.addView(host_field, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        lv.addView(ip_field, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        type = getArguments().getString("type");
        switch (type){
            case "delete_host":
                adb.setMessage(R.string.dialog_text_delete_host);
                adb.setPositiveButton(R.string.dialog_positive_button, this);
                adb.setNegativeButton(R.string.dialog_negative_button, this);
            break;
            case "edit_host": case "add_host":
                host = getArguments().getString("host");
                ip = getArguments().getString("ip");
                host_field.setText(host);
                host_field.setHint(R.string.dialog_text_host_field);
                host_field.addTextChangedListener(this);
                ip_field.setText(ip);
                ip_field.setHint(R.string.dialog_text_ip_field);
                ip_field.addTextChangedListener(this);
                adb.setTitle(R.string.dialog_text_edit_host);
                adb.setView(lv);
                adb.setPositiveButton(R.string.dialog_edit_host_positive_button, this);
                adb.setNegativeButton(R.string.dialog_edit_host_negative_button, this);
            break;
        }
        thisDialog = adb.create();
        positiveButton = thisDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        return thisDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case Dialog.BUTTON_POSITIVE:
                if ( type.equals("edit_host") || type.equals("add_host") ) {
                    host = host_field.getText().toString();
                    ip = ip_field.getText().toString();
                }
                mydialogResult.onResultCallback(type, host, ip);
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }

    // TextWatcher interface implementation
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ( positiveButton != null ) {
            String host = host_field.getText().toString();
            String ip = ip_field.getText().toString();
            if (!host.isEmpty() && !ip.isEmpty()) {
                positiveButton.setEnabled(true);
            } else {
                positiveButton.setEnabled(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {    }


}
