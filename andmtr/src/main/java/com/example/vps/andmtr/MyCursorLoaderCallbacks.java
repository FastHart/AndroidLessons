package com.example.vps.andmtr;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import java.util.concurrent.TimeUnit;


/**
 * Created by vps on 20.12.15.
 */
public class MyCursorLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor>  {

    Context context;
    DB db;
    String db_table;
    SimpleCursorAdapter scAdapter;

    public MyCursorLoaderCallbacks(Context cont, DB db, String db_table, SimpleCursorAdapter adapter) {
        super();
        this.context = cont;
        this.db = db;
        this.db_table = db_table;
        this.scAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MyCursorLoader(context, db, db_table);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    static class MyCursorLoader extends CursorLoader {

        DB db;
        String db_table;

        public MyCursorLoader(Context context, DB db, String db_table) {
            super(context);
            this.db = db;
            this.db_table= db_table;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData(db_table);
            //try {
                //TimeUnit.SECONDS.sleep(3);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
            return cursor;
        }

    }
}
