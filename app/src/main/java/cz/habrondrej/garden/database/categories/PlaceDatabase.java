package cz.habrondrej.garden.database.categories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.model.categories.Place;

public class PlaceDatabase extends CategoryDatabase<Place> {

    public static final String TABLE_NAME = "PLACE_TABLE";

    private static final String DATABASE_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT)";

    public PlaceDatabase(@Nullable Context context) {
        super(context);
    }

    protected static void onCreateCatDB(@NotNull SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    protected static void onUpgradeCatDB(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreateCatDB(db);
    }

    @Override
    public boolean create(@NotNull Place place) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, place.getTitle());
        long insert = db.insert(TABLE_NAME, null, cv);

        return insert > 0;
    }

    @Override
    public Place getOneById(int id) throws IndexOutOfBoundsException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String title = cursor.getString(1);

            return new Place(id, title);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public List<Place> getAll() {
        List<Place> places = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));

                places.add(new Place(id, title));
                cursor.moveToNext();
            }
        }

        return places;
    }

    @Override
    public boolean update(@NotNull Place place) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, place.getTitle());
        int update = db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(place.getId())});

        return update > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return deleteById(id, TABLE_NAME);
    }

}
