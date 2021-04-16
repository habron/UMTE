package cz.habrondrej.garden.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.database.categories.GroupDatabase;
import cz.habrondrej.garden.database.categories.PlaceDatabase;
import cz.habrondrej.garden.database.categories.SpeciesDatabase;
import cz.habrondrej.garden.database.categories.TypeDatabase;
import cz.habrondrej.garden.model.Plant;

public class PlantDatabase extends DatabaseHelper<Plant> {

    public static final String TABLE_NAME = "PLANT_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_GROUP_ID = "GROUP_ID";
    public static final String COLUMN_PLACE_ID = "PLACE_ID";
    public static final String COLUMN_SPECIES_ID = "SPECIES_ID";
    public static final String COLUMN_TYPE_ID = "TYPE_ID";
    public static final String COLUMN_ARCHIVE = "ARCHIVE";

    private static final String DATABASE_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_GROUP_ID + " INTEGER, " +
            COLUMN_PLACE_ID + " INTEGER, " +
            COLUMN_SPECIES_ID + " INTEGER, " +
            COLUMN_TYPE_ID + " INTEGER, " +
            COLUMN_ARCHIVE + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_GROUP_ID + ") REFERENCES " + GroupDatabase.TABLE_NAME + " (" + GroupDatabase.COLUMN_ID + ") ON DELETE SET NULL, " +
            "FOREIGN KEY (" + COLUMN_PLACE_ID + ") REFERENCES " + PlaceDatabase.TABLE_NAME + " (" + PlaceDatabase.COLUMN_ID + ") ON DELETE SET NULL, " +
            "FOREIGN KEY (" + COLUMN_SPECIES_ID + ") REFERENCES " + SpeciesDatabase.TABLE_NAME + " (" + SpeciesDatabase.COLUMN_ID + ") ON DELETE SET NULL, " +
            "FOREIGN KEY (" + COLUMN_TYPE_ID + ") REFERENCES " + TypeDatabase.TABLE_NAME + " (" + TypeDatabase.COLUMN_ID + ") ON DELETE SET NULL)";

    public PlantDatabase(@Nullable Context context) {
        super(context);
    }

    protected static void onCreateDB(@NotNull SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    protected static void onUpgradeDB(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreateDB(db);
    }

    @Override
    public boolean create(Plant category) {
        return false;
    }

    @Override
    public Plant getOneById(int id) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public List<Plant> getAll() {
        return getAll(false);
    }

    public List<Plant> getAll(boolean isArchive) {
        List<Plant> plants = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ARCHIVE + " = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(isArchive)});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));

                Date date = null;
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                plants.add(new Plant(id, title, date, description));
                cursor.moveToNext();
            }
        }

        return plants;
    }

    @Override
    public boolean update(Plant category) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
