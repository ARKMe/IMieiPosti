package bello.andrea.imieiposti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlacesSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PLACES = "places";
    public static final String COLUMN_PLACE_ID = "_id";
    public static final String COLUMN_PLACE_NAME = "name";
    public static final String COLUMN_PLACE_DESCRIPTION = "description";
    public static final String COLUMN_PLACE_CATEGORY = "category";
    public static final String COLUMN_PLACE_LATITUDE = "latitude";
    public static final String COLUMN_PLACE_LONGITUDE = "longitude";

    public static final int INDEX_COLUMN_PLACE_ID = 0;
    public static final int INDEX_COLUMN_PLACE_NAME = 1;
    public static final int INDEX_COLUMN_PLACE_DESCRIPTION = 2;
    public static final int INDEX_COLUMN_PLACE_CATEGORY = 3;
    public static final int INDEX_COLUMN_PLACE_LATITUDE = 4;
    public static final int INDEX_COLUMN_PLACE_LONGITUDE = 5;

    private static final String DATABASE_NAME = "places.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_PLACES + " ("
            + COLUMN_PLACE_ID + " integer primary key autoincrement, "
            + COLUMN_PLACE_NAME + " text not null, "
            + COLUMN_PLACE_DESCRIPTION + " text not null, "
            + COLUMN_PLACE_CATEGORY + " integer not null, "
            + COLUMN_PLACE_LATITUDE + " real not null, "
            + COLUMN_PLACE_LONGITUDE + " real not null"
            + ");";

    public PlacesSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("CREATION", DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(PlacesSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

}
