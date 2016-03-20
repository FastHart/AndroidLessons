package com.example.vps.andmtr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;

import static android.app.PendingIntent.getActivity;



public class MainActivity extends FragmentActivity {

    public static String LOG_TAG; // R.string.log_tag

    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    Bundle dialog_args = new Bundle();
    ItemsDialog myDialog;

    //String data[] = { "one", "two", "three", "four" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOG_TAG = getResources().getString(R.string.log_tag);

        // сохраняем параметры диалога в Bundle и создаем диалог (всплывающее меню выбора теста)
        //dialog_args.putStringArray("data", data);
        myDialog = new ItemsDialog();
        //myDialog.setArguments(dialog_args);


        // открываем подключение к БД
        db = new DB(this);
        db.open();

        // получаем курсор
        cursor = db.getAllData(DB.TABLE_ITEMS);
        //startManagingCursor(cursor);

        // формируем столбцы сопоставления
        String[] from = new String[] { DB.ITEMS_COLUMN_NAME, DB.ITEMS_COLUMN_ADDR};
        int[] to = new int[] { R.id.tvName, R.id.tvAddr };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter( this, R.layout.item, cursor, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        //getLoaderManager()
        getSupportLoaderManager().initLoader(0, null, new MyCursorLoaderCallbacks(this, db, DB.TABLE_ITEMS, scAdapter));

        // Обработка нажатия на пункт меню
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Pressed Item position= " + position + " id= " + id);
                // get item form db (String array: {"item name", "item address"})
                String[] item = db.getItemById(String.valueOf(id), DB.TABLE_ITEMS);
                Log.d(LOG_TAG, "Name=" + item[0] + " Addr=" + item[1]);

                // put item[] to dialog arguments
                //dialog_args.putLong("item", id);
                dialog_args.putStringArray("item", item);
                myDialog.setArguments(dialog_args);
                // ShowDialog
                myDialog.show(getFragmentManager(), "tests_dialog");
            }
        });

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
        if (id == R.id.settings_edit_hosts) {
            Intent intent = new Intent(this, SettingsEditHosts.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.settings_edit_tests) {
            return true;
        }
        if (id == R.id.settings_edit_import_export) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickButtAdd(View view) {
        Log.d(LOG_TAG, "Add pressed");
    }

    public void onClickButtPrefs(View view) {
        Log.d(LOG_TAG, "Prefs pressed");
    }

    @Override
    public void onResume (){
        super.onResume();
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        //db.close();
    }
}
