package com.coboltforge.dontmind.xbox.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseProvider{
        private String TAG = DatabaseProvider.class.getSimpleName();

        private  DatabaseProvider database;

	    public  void saveBookmark(ConnectionBean conn) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            //Log.d(TAG, "Saving bookmark for conn " + conn.toString());
            conn.save(db);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //Log.e(TAG, "Error saving bookmark: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }


        public  void writeRecent(ConnectionBean conn) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            MostRecentBean mostRecent = getMostRecent(db);
            if (mostRecent == null) {
                mostRecent = new MostRecentBean();
                mostRecent.setConnectionId(conn.get_Id());
                mostRecent.Gen_insert(db);
            } else {
                mostRecent.setConnectionId(conn.get_Id());
                mostRecent.Gen_update(db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private SQLiteDatabase getWritableDatabase() {
	        return null;
    }


    /**
     * Return the object representing the app global state in the database, or null
     * if the object hasn't been set up yet
     *
     * @param db App's database -- only needs to be readable
     * @return Object representing the single persistent instance of MostRecentBean, which
     * remoteInputStream the app's global state
     */
    public static MostRecentBean getMostRecent(SQLiteDatabase db) {
        ArrayList<MostRecentBean> recents = new ArrayList<MostRecentBean>(1);
        MostRecentBean.getAll(db, MostRecentBean.GEN_TABLE_NAME, recents, MostRecentBean.GEN_NEW);
        if (recents.size() == 0){
            return null;
        }
        return recents.get(0);
    }


    public void close() {
    }
}