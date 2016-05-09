package bello.andrea.imieiposti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class PlacesManager {

    private SQLiteDatabase database;
    private PlacesSQLiteHelper dbHelper;

    public PlacesManager(Context context) {
        dbHelper = new PlacesSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addPlace(Place place) {
        ContentValues values = new ContentValues();
        values.put(PlacesSQLiteHelper.COLUMN_PLACE_NAME, place.getName());
        values.put(PlacesSQLiteHelper.COLUMN_PLACE_DESCRIPTION, place.getDescription());
        values.put(PlacesSQLiteHelper.COLUMN_PLACE_CATEGORY, place.getCategory().ordinal());
        values.put(PlacesSQLiteHelper.COLUMN_PLACE_LATITUDE, place.getLatitude());
        values.put(PlacesSQLiteHelper.COLUMN_PLACE_LONGITUDE, place.getLongitude());
        long insertId = database.insert(
                PlacesSQLiteHelper.TABLE_PLACES,
                null,
                values
        );
        return insertId;
    }

    public void deletePlace(Place place) {
        long id = place.getId();
        database.delete(
                PlacesSQLiteHelper.TABLE_PLACES,
                PlacesSQLiteHelper.COLUMN_PLACE_ID + " = " + id,
                null
        );
    }

    public List<Place> getAllPlaces(){
        return getAllPlaces(null);
    }

    public List<Place> getAllPlaces(String search) {
        List<Place> books = new ArrayList<>();

        String query = null;
        if(!TextUtils.isEmpty(search))
            query = PlacesSQLiteHelper.COLUMN_PLACE_NAME + " LIKE '%" + search + "%'";

        Cursor cursor = database.query(
                PlacesSQLiteHelper.TABLE_PLACES,
                null,
                query,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Place place = cursorToPlace(cursor);
            books.add(place);
            cursor.moveToNext();
        }

        cursor.close();
        return books;
    }

    private Place cursorToPlace(Cursor cursor) {
        Place place = new Place();
        place.setId(cursor.getLong(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_ID));
        place.setName(cursor.getString(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_NAME));
        place.setDescription(cursor.getString(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_DESCRIPTION));
        place.setCategory(Category.values()[cursor.getInt(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_CATEGORY)]);
        place.setLatitude(cursor.getDouble(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_LATITUDE));
        place.setLongitude(cursor.getDouble(PlacesSQLiteHelper.INDEX_COLUMN_PLACE_LONGITUDE));
        return place;
    }
}
