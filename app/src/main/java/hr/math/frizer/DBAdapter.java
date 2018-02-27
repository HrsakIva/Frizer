package hr.math.frizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ana on 27.2.2018..
 */

class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_USERNAME = "username";
    static final String KEY_PASSWORD = "password";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "username text not null, password text not null);";

    // --- table Salon ---
    static final String KEY_ROWID_SALON = "_id";
    static final String KEY_NAME_SALON = "name";
    static final String KEY_EMAIL_SALON = "email";
    static final String KEY_TELNUMBER_SALON = "telnumber";
    static final String KEY_ADDRESS_SALON = "address";
    static final String KEY_WORKHOURS_SALON= "workhours";
    static final String KEY_RATING_SALON = "rating";

    static final String DATABASE_TABLE_SALON = "salon";

    static final String DATABASE_CREATE_SALON =
            "create table salon (_id integer primary key autoincrement, "
                    + "name text not null, address text not null, email text not null, telnumber text not null, workhours text not null, rating text not null);";




    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE_SALON);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS contacts");
            db.execSQL("DROP TABLE IF EXISTS salon");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertUser(String username, String password)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public long insertSalon(String name, String address, String email, String telnumber, String workhours, String rating)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME_SALON, name);
        initialValues.put(KEY_ADDRESS_SALON, address);
        initialValues.put(KEY_EMAIL_SALON, email);
        initialValues.put(KEY_TELNUMBER_SALON, telnumber);
        initialValues.put(KEY_WORKHOURS_SALON,workhours);
        initialValues.put(KEY_RATING_SALON,rating);

        return db.insert(DATABASE_TABLE_SALON, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteUser(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteSalon(long rowId)
    {
        return db.delete(DATABASE_TABLE_SALON, KEY_ROWID_SALON + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllUsers()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_USERNAME,
                KEY_PASSWORD}, null, null, null, null, null);
    }

    public Cursor getAllSalons()
    {
        return db.query(DATABASE_TABLE_SALON, new String[] {KEY_ROWID_SALON, KEY_NAME_SALON, KEY_ADDRESS_SALON,
                KEY_EMAIL_SALON, KEY_TELNUMBER_SALON, KEY_WORKHOURS_SALON, KEY_RATING_SALON }, null, null, null, null, null);
    }


    //---retrieves a particular contact---
    public Cursor getUser(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_USERNAME, KEY_PASSWORD}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSalon(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_SALON, new String[] {KEY_ROWID_SALON,
                                KEY_NAME_SALON, KEY_ADDRESS_SALON, KEY_EMAIL_SALON, KEY_TELNUMBER_SALON, KEY_WORKHOURS_SALON, KEY_RATING_SALON}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateUser(long rowId, String name, String email)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME, name);
        args.put(KEY_PASSWORD, email);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateSalon(long rowId, String name, String address, String email, String telnumber, String workhours, String rating)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME_SALON, name);
        args.put(KEY_ADDRESS_SALON, address);
        args.put(KEY_EMAIL_SALON, email);
        args.put(KEY_TELNUMBER_SALON, telnumber);
        args.put(KEY_WORKHOURS_SALON,workhours);
        args.put(KEY_RATING_SALON,rating);

        return db.update(DATABASE_TABLE_SALON, args, KEY_ROWID_SALON + "=" + rowId, null) > 0;
    }

    //---checks if user already exists in database

}
