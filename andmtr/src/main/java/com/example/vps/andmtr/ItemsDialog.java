package com.example.vps.andmtr;

import android.content.Intent;
import android.database.Cursor;
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

    public static String LOG_TAG; //R.string.log_tag
    public String[] item;
    DB db;
    Cursor cursor;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LOG_TAG = getResources().getString(R.string.log_tag);
        item = getArguments().getStringArray("item");

        // открываем подключение к БД
        db = new DB(getActivity());
        db.open();

        // получаем курсор
        cursor = db.getAllData(DB.TABLE_TESTS);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.item_menu)
                .setCursor(cursor, myClickListener, DB.TESTS_COLUMN_NAME);
        return adb.create();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            int test_id = which + 1;
            // get test info from DB (string array: {"test name", "test type", "test interval", "test resolve"})
            String[] choosed_test = db.getItemById(String.valueOf(test_id), DB.TABLE_TESTS);
            // выводим в лог позицию нажатого элемента
            Log.d(LOG_TAG, "Which = " + test_id + " Test choosed: " + choosed_test[0] + " Item: " + item);
            //Intent intent = new Intent(getActivity(), PingActivity.class);
            Intent intent = new Intent(getActivity(), PingActivityWithFragment.class);
            intent.putExtra("item", item);
            intent.putExtra("test", choosed_test);
            startActivity(intent);

        }
    };


    public void onDetach() {
        super.onDetach();
        // закрываем подключение при выходе
        db.close();
    }
}

