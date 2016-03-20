package com.example.vps.andmtr;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SettingsEditHosts extends FragmentActivity implements YesNoDialog.dialogResult {

    public static String LOG_TAG; // R.string.log_tag

    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    DialogFragment dialogDelete;
    DialogFragment dialogEdit;
    Bundle dialogArgs = new Bundle();
    Long id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LOG_TAG = getResources().getString(R.string.log_tag);
        // открываем подключение к БД
        db = new DB(this);
        db.open();

        // получаем курсор
        cursor = db.getAllData(DB.TABLE_ITEMS);

        // формируем столбцы сопоставления
        String[] from = new String[] { DB.ITEMS_COLUMN_NAME, DB.ITEMS_COLUMN_ADDR};
        int[] to = new int[] { R.id.tvName, R.id.tvAddr };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter( this, R.layout.item_settings_hosts, cursor, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);
        // создаем footer
        //View footerView =  ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_with_button, null, false);
        View footerView = getLayoutInflater().inflate(R.layout.footer_with_button, null);
        lvData.addFooterView(footerView);
        lvData.setFooterDividersEnabled(false);
        Button button_add = (Button) findViewById(R.id.button_add_item);
        button_add.setText(R.string.button_add_host);

        // создаем лоадер для чтения данных
        //getLoaderManager()
        getSupportLoaderManager().initLoader(0, null, new MyCursorLoaderCallbacks(this, db, DB.TABLE_ITEMS, scAdapter));


        //Готовим диалог
        dialogDelete = new YesNoDialog();
        dialogEdit = new YesNoDialog();
    }

    public void onClickButtDeleteHost(View view) {
        id = getId(view);
        //Log.d(LOG_TAG, "position: "+String.valueOf(pos)+" id: "+String.valueOf(id));
        // показываем диалог
        dialogArgs.clear();
        dialogArgs.putString("type", "delete_host");
        dialogDelete.setArguments(dialogArgs);
        dialogDelete.show(getFragmentManager(),"dialogDelete");
    }

    public void onClickButtEditHost(View view) {
        id = getId(view);
        String[] item = db.getItemById(String.valueOf(id), DB.TABLE_ITEMS);
        dialogArgs.clear();
        dialogArgs.putString("type", "edit_host");
        dialogArgs.putString("host", item[0]);
        dialogArgs.putString("ip", item[1]);
        dialogEdit.setArguments(dialogArgs);
        dialogEdit.show(getFragmentManager(), "dialogEdit");
    }

    public void onClickAddItem(View view) {
        dialogArgs.clear();
        dialogArgs.putString("type", "add_host");
        dialogEdit.setArguments(dialogArgs);
        dialogEdit.show(getFragmentManager(), "dialogEdit");
    }

    public long getId (View view) {
        ListView parentListView = (ListView)view.getParent().getParent().getParent();
        Integer pos = parentListView.getPositionForView(view);
        return parentListView.getItemIdAtPosition(pos);
    }

    @Override
    public void onResultCallback(String type, String host, String ip) {
        switch (type) {
            case "delete_host":
                if ( db.delRec(DB.TABLE_ITEMS, id) ){
                    Toast.makeText(this, R.string.host_deleted, Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                else {
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;
            case "edit_host":
                if ( db.updateRec(DB.TABLE_ITEMS, id, host, ip) ) {
                    Toast.makeText(this, R.string.host_updated, Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                else {
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;
            case "add_host":
                if ( db.addRec(DB.TABLE_ITEMS, host, ip) ) {
                    Toast.makeText(this, R.string.host_added, Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                else {
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;
            }

    }


}
