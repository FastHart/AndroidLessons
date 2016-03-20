/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
	
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.jasonmcreynolds.dslvdbdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseAdapter extends SQLiteOpenHelper {

	private static DatabaseAdapter sSingleton;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "notes";
	private static final int SCHEMA_VERSION = 1;
	public static final String ITEM_KEY_ROWID = "_id";
	public static final String ITEM_TABLE = "item_table";
	public static final String ITEM_NAME = "item_name";
	public static final String ITEM_DETAILS = "item_details";
	public static final String ITEM_POSITION = "item_position";

	private final String sort_order = "ASC"; // ASC or DESC

	// String to create the initial notes database table
	private static final String DATABASE_CREATE_ITEMS = "CREATE TABLE item_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT, item_details TEXT, item_image_name TEXT, item_image_path TEXT, item_position INTEGER);";

	// Methods to setup database singleton and connections
	synchronized static DatabaseAdapter getInstance(Context ctxt) {
		if (sSingleton == null) {
			sSingleton = new DatabaseAdapter(ctxt);
		}
		return sSingleton;
	}

	private DatabaseAdapter(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
		//sSingleton = this;
	}

	public DatabaseAdapter openConnection() throws SQLException {
		if (mDb == null) {
			mDb = sSingleton.getWritableDatabase();
		}
		return this;
	}

	public synchronized void closeConnection() {
		if (sSingleton != null) {
			sSingleton.close();
			mDb.close();
			sSingleton = null;
			mDb = null;
		}
	}

	// initial database load with dummy records
	
	@Override
	public void onCreate(SQLiteDatabase mDb) {
		try {
			mDb.beginTransaction();

			mDb.execSQL(DATABASE_CREATE_ITEMS);

			ContentValues cv_items = new ContentValues();

			cv_items.put(ITEM_NAME, "Note to self");
			cv_items.put(ITEM_DETAILS,
					"Learn how to create a cool drag and drop list.");
			cv_items.put(ITEM_POSITION, 0);
			/*
			 * The second argument of the insert statement is for the �null
			 * column hack� when the ContentValues instance is empty � the
			 * column named as the �null column hack� will be explicitly
			 * assigned the value NULL in the SQL INSERT statement generated by
			 * insert(). This is required due to a quirk in SQLite�s support for
			 * the SQL INSERT statement. -CommonsWare
			 */
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Buy groceries");
			cv_items.put(
					ITEM_DETAILS,
					"Need: Ice cream, candy, soda, chips, salsa and more ice cream...oh, and don't forget the cake and cookies to go with the ice cream.");
			cv_items.put(ITEM_POSITION, 1);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 3");
			cv_items.put(ITEM_DETAILS, "Note 3");
			cv_items.put(ITEM_POSITION, 2);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 4");
			cv_items.put(ITEM_DETAILS, "Note 4");
			cv_items.put(ITEM_POSITION, 3);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 5");
			cv_items.put(ITEM_DETAILS, "Note 5");
			cv_items.put(ITEM_POSITION, 4);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 6");
			cv_items.put(ITEM_DETAILS, "Note 6");
			cv_items.put(ITEM_POSITION, 5);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 7");
			cv_items.put(ITEM_DETAILS, "Note 7");
			cv_items.put(ITEM_POSITION, 6);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 8");
			cv_items.put(ITEM_DETAILS, "Note 8");
			cv_items.put(ITEM_POSITION, 7);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 9");
			cv_items.put(ITEM_DETAILS, "Note 9");
			cv_items.put(ITEM_POSITION, 8);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			cv_items.put(ITEM_NAME, "Note 10");
			cv_items.put(ITEM_DETAILS, "Note 10");
			cv_items.put(ITEM_POSITION, 9);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			mDb.setTransactionSuccessful();

		} finally {
			mDb.endTransaction();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase mDb, int oldVersion, int newVersion) {
		// You probably shouldn't do the following in a production app,
		// but this is just a demo...
		mDb.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
		onCreate(mDb);
	}

	// Methods for Items

	public Cursor getAllItemRecords() {
		return mDb.query(ITEM_TABLE, new String[] { ITEM_KEY_ROWID, ITEM_NAME,
				ITEM_DETAILS, ITEM_POSITION }, null, null, null, null,
				"item_position " + sort_order);
	}

	public Cursor getItemRecord(long rowId) throws SQLException {
		Cursor mLetterCursor = mDb.query(true, ITEM_TABLE, new String[] {
				ITEM_KEY_ROWID, ITEM_NAME, ITEM_DETAILS, ITEM_POSITION },
				ITEM_KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mLetterCursor != null) {
			mLetterCursor.moveToFirst();
		}
		return mLetterCursor;
	}

	public long insertItemRecord(String item_name, String item_details) {
		int item_Position = getMaxColumnData();
		ContentValues initialItemValues = new ContentValues();
		initialItemValues.put(ITEM_NAME, item_name);
		initialItemValues.put(ITEM_DETAILS, item_details);
		initialItemValues.put(ITEM_POSITION, (item_Position + 1));

		return mDb.insert(ITEM_TABLE, null, initialItemValues);
	}

	public boolean deleteItemRecord(long rowId) {
		return mDb.delete(ITEM_TABLE, ITEM_KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateItemRecord(long rowId, String item_name,
			String item_details) {
		ContentValues ItemArgs = new ContentValues();
		ItemArgs.put(ITEM_NAME, item_name);
		ItemArgs.put(ITEM_DETAILS, item_details);
		return mDb.update(ITEM_TABLE, ItemArgs, ITEM_KEY_ROWID + "=" + rowId,
				null) > 0;
	}

	public boolean updateItemPosition(long rowId, Integer position) {
		ContentValues ItemArgs = new ContentValues();
		ItemArgs.put(ITEM_POSITION, position);
		return mDb.update(ITEM_TABLE, ItemArgs, ITEM_KEY_ROWID + "=" + rowId,
				null) > 0;
	}

	public boolean deleteAllItems() {

		int doneDelete = 0;
		doneDelete = mDb.delete(ITEM_TABLE, null, null);
		Log.w("Items Database Deleted", Integer.toString(doneDelete)
				+ " item_table removed from database.");
		return doneDelete > 0;
	}

	public void addDummyRecords(int numRecords) {
		ContentValues cv_items = new ContentValues();
		int mStartPosition = getMaxColumnData();
		int mNewPosition;
		if (mStartPosition == 0) {
			mNewPosition = 0;
		} else {
			mNewPosition = mStartPosition + 1;
		}

		for (int i = 0; i < numRecords; i++) {
			String itemName = "Note " + (mNewPosition + 1);
			String itemDetails = "Note " + (mNewPosition + 1)
					+ " stuff to do...";

			cv_items.put(ITEM_NAME, itemName);
			cv_items.put(ITEM_DETAILS, itemDetails);
			cv_items.put(ITEM_POSITION, mNewPosition);
			mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

			mNewPosition++;
		}
	}

	public int getMaxColumnData() {

		final SQLiteStatement stmt = mDb
				.compileStatement("SELECT MAX(item_position) FROM item_table");

		return (int) stmt.simpleQueryForLong();
	}

	public void createInitialItemsDatabase() {

		ContentValues cv_items = new ContentValues();

		cv_items.put(ITEM_NAME, "Note to self");
		cv_items.put(ITEM_DETAILS,
				"Learn how to create a cool drag and drop list.");
		cv_items.put(ITEM_POSITION, 0);
		/*
		 * The second argument of the insert statement is for the �null column
		 * hack� when the ContentValues instance is empty � the column named as
		 * the �null column hack� will be explicitly assigned the value NULL in
		 * the SQL INSERT statement generated by insert(). This is required due
		 * to a quirk in SQLite�s support for the SQL INSERT statement.
		 * -CommonsWare
		 */
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Buy groceries");
		cv_items.put(
				ITEM_DETAILS,
				"Need: Ice cream, candy, soda, chips, salsa and more ice cream...oh, and don't forget the cake and cookies to go with the ice cream.");
		cv_items.put(ITEM_POSITION, 1);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 3");
		cv_items.put(ITEM_DETAILS, "Note 3");
		cv_items.put(ITEM_POSITION, 2);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 4");
		cv_items.put(ITEM_DETAILS, "Note 4");
		cv_items.put(ITEM_POSITION, 3);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 5");
		cv_items.put(ITEM_DETAILS, "Note 5");
		cv_items.put(ITEM_POSITION, 4);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 6");
		cv_items.put(ITEM_DETAILS, "Note 6");
		cv_items.put(ITEM_POSITION, 5);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 7");
		cv_items.put(ITEM_DETAILS, "Note 7");
		cv_items.put(ITEM_POSITION, 6);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 8");
		cv_items.put(ITEM_DETAILS, "Note 8");
		cv_items.put(ITEM_POSITION, 7);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 9");
		cv_items.put(ITEM_DETAILS, "Note 9");
		cv_items.put(ITEM_POSITION, 8);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);

		cv_items.put(ITEM_NAME, "Note 10");
		cv_items.put(ITEM_DETAILS, "Note 10");
		cv_items.put(ITEM_POSITION, 9);
		mDb.insert(ITEM_TABLE, ITEM_NAME, cv_items);
	}
}