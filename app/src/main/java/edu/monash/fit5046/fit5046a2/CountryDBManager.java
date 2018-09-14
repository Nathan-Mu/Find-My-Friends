package edu.monash.fit5046.fit5046a2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nathan on 28/4/17.
 */

public class CountryDBManager {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "country.db";
    private final Context context;
    private static final String SQL_CREATE_STATEMENT =
            "CREATE TABLE " + CountryDBStructure.tableEntry.TABLE_NAME + " (" +
                    CountryDBStructure.tableEntry._ID + " INTEGER PRIMARY KEY, " +
                    CountryDBStructure.tableEntry.COLUMN_ID + " TEXT, " +
                    CountryDBStructure.tableEntry.COLUMN_NAME + " TEXT, " +
                    CountryDBStructure.tableEntry.COLUMN_ABBREVIATION + " TEXT);";
    private static final String SQL_REMOVE_STATEMENT =
            "DROP TABLE IF EXISTS " + CountryDBStructure.tableEntry.TABLE_NAME;
    private CountryDBHelper countryDBHelper;
    private SQLiteDatabase db;
    private String[] projection = {CountryDBStructure.tableEntry.COLUMN_ID, CountryDBStructure.tableEntry.COLUMN_NAME, CountryDBStructure.tableEntry.COLUMN_ABBREVIATION};

    public CountryDBManager(Context ctx)
    {
        this.context = ctx;
        countryDBHelper = new CountryDBHelper(context);
    }

    public CountryDBManager open() throws SQLException
    {
        db = countryDBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        countryDBHelper.close();
    }

    public long insertCountry(String countryID, String countryName, String abbreviation)
    {
        ContentValues values = new ContentValues();
        values.put(CountryDBStructure.tableEntry.COLUMN_ID, countryID);
        values.put(CountryDBStructure.tableEntry.COLUMN_NAME, countryName);
        values.put(CountryDBStructure.tableEntry.COLUMN_ABBREVIATION, abbreviation);
        return db.insert(CountryDBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllCountries()
    {
        return db.query(CountryDBStructure.tableEntry.TABLE_NAME, projection, null, null, null, null, null);
    }

    public int deleteCountry(String rowId)
    {
        String[] selectionArgs = {String.valueOf(rowId)};
        String selection = CountryDBStructure.tableEntry.COLUMN_ID + " LIKE ?";
        return db.delete(CountryDBStructure.tableEntry.TABLE_NAME, selection, selectionArgs);
    }

    public boolean updateUser(String id, String name)
    {
        ContentValues values = new ContentValues();
        values.put(CountryDBStructure.tableEntry.COLUMN_NAME, name);
        String selection = CountryDBStructure.tableEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        int count = db.update(
                CountryDBStructure.tableEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count > 0;
    }

    public void initialize()
    {
        insertCountry("1", "Austria", "AT");
        insertCountry("2", "Australia", "AU");
        insertCountry("3", "Brazil", "BR");
        insertCountry("4", "Canada", "CA");
        insertCountry("5", "China", "CN");
        insertCountry("6", "Germany", "DE");
        insertCountry("7", "France", "FR");
        insertCountry("8", "India", "IN");
        insertCountry("9", "New Zealand", "NZ");
    }

    private static class CountryDBHelper extends SQLiteOpenHelper
    {
        public CountryDBHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(SQL_CREATE_STATEMENT);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


}
