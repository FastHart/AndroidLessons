package com.example.vps.p1361_cursorloader;

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
    SimpleCursorAdapter scAdapter;

    public MyCursorLoaderCallbacks(Context cont, DB db, SimpleCursorAdapter adapter) {
        super();
        this.context = cont;
        this.db = db;
        this.scAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MyCursorLoader(context, db);
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

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }
}
