package com.example.marim.githubtask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by marim on 8/21/2017.
 */

public class DB_Helper extends SQLiteOpenHelper {

    private static final int database_VERSION = 1;

    private static final String database_NAME = "repo.db";
    private static final String table_name = "repository";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String USERNAME = "username";
    private static final String FORK = "fork";
    private static final String REPO_URL = "repo_url";
    private static final String OWNER_URL = "owner_url";


    private static final String[] COLUMNS = {ID, NAME, DESCRIPTION, USERNAME, FORK, REPO_URL, OWNER_URL};

    public DB_Helper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Event_TABLE = "CREATE TABLE repository ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT,"
                + "description TEXT, " + "username TEXT, " + "fork TEXT, " + " repo_url TEXT, " + " owner_url TEXT)";
        db.execSQL(CREATE_Event_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        this.onCreate(db);
    }

    public void createRow(String Name_, String Description_, String Username_, String Fork_, String RepoUrl_, String OwnerUrl_) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, Name_);
        values.put(DESCRIPTION, Description_);
        values.put(USERNAME, Username_);
        values.put(FORK, Fork_);
        values.put(REPO_URL, RepoUrl_);
        values.put(OWNER_URL, OwnerUrl_);

        db.insert(table_name, null, values);
    }

    public List<DB_Model> getAllRows() {
        List<DB_Model> eventsM = new LinkedList<DB_Model>();

        String query = "SELECT  * FROM " + table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        DB_Model EM = null;
        if (cursor.moveToFirst()) {
            do {

                EM = new DB_Model();
                EM.setID(Integer.parseInt(cursor.getString(0)));
                EM.setName(cursor.getString(1));
                EM.setDescription(cursor.getString(2));
                EM.setUsername(cursor.getString(3));
                EM.setFork(cursor.getString(4));
                EM.setRepo_url(cursor.getString(5));
                EM.setOwner_url(cursor.getString(6));

                eventsM.add(EM);
            } while (cursor.moveToNext());
        }

        return eventsM;
    }

    public DB_Model readRepository(int id) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_name, COLUMNS, " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        try {
            if (cursor != null)
                cursor.moveToFirst();

            DB_Model EM = new DB_Model();
            EM.setID(Integer.parseInt(cursor.getString(0)));
            EM.setName(cursor.getString(1));
            EM.setDescription(cursor.getString(2));
            EM.setUsername(cursor.getString(3));
            EM.setFork(cursor.getString(4));
            EM.setRepo_url(cursor.getString(5));
            EM.setOwner_url(cursor.getString(6));

            return EM;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return null;


    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}