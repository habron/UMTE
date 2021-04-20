package cz.habrondrej.garden.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.database.categories.GroupDatabase;
import cz.habrondrej.garden.database.categories.PlaceDatabase;
import cz.habrondrej.garden.database.categories.SpeciesDatabase;
import cz.habrondrej.garden.database.categories.TypeDatabase;
import cz.habrondrej.garden.model.Plant;
import cz.habrondrej.garden.model.categories.Group;
import cz.habrondrej.garden.model.categories.Place;
import cz.habrondrej.garden.model.categories.Species;
import cz.habrondrej.garden.model.categories.Type;
import cz.habrondrej.garden.utils.DateParser;

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

    private GroupDatabase groupDatabase;
    private PlaceDatabase placeDatabase;
    private SpeciesDatabase speciesDatabase;
    private TypeDatabase typeDatabase;

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
        groupDatabase = new GroupDatabase(context);
        placeDatabase = new PlaceDatabase(context);
        speciesDatabase = new SpeciesDatabase(context);
        typeDatabase = new TypeDatabase(context);
    }

    protected static void onCreateDB(@NotNull SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    protected static void onUpgradeDB(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreateDB(db);
    }

    @Override
    public boolean create(@NotNull Plant plant) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(COLUMN_TITLE, plant.getTitle());
        cv.put(COLUMN_DATE, plant.getDate() == null ? null : plant.getDate().toString());
        cv.put(COLUMN_DESCRIPTION, plant.getDescription());
        cv.put(COLUMN_GROUP_ID, plant.getGroup() == null ? null : plant.getGroup().getId());
        cv.put(COLUMN_PLACE_ID, plant.getPlace() == null ? null : plant.getPlace().getId());
        cv.put(COLUMN_SPECIES_ID, plant.getSpecies() == null ? null : plant.getSpecies().getId());
        cv.put(COLUMN_TYPE_ID, plant.getType() == null ? null : plant.getType().getId());
        cv.put(COLUMN_ARCHIVE, plant.isArchive());
        long insert = db.insert(TABLE_NAME, null, cv);

        return insert > 0;
    }

    @Override
    public Plant getOneById(int id) throws IndexOutOfBoundsException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            LocalDate date = DateParser.parseDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            int groupId = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_ID));
            int placeId = cursor.getInt(cursor.getColumnIndex(COLUMN_PLACE_ID));
            int speciesId = cursor.getInt(cursor.getColumnIndex(COLUMN_SPECIES_ID));
            int typeId = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE_ID));
            boolean archive = cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVE)) > 0;

            Group group = null;
            Place place = null;
            Species species = null;
            Type type = null;

            if (groupId != 0)
                group = groupDatabase.getOneById(groupId);
            if (placeId != 0)
                place = placeDatabase.getOneById(placeId);
            if (speciesId != 0)
                species = speciesDatabase.getOneById(speciesId);
            if (typeId != 0)
                type = typeDatabase.getOneById(typeId);

            return new Plant(id, title, date, description, group, place, species, type, archive);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public List<Plant> getAll() {
        return getAll(false);
    }

    public List<Plant> getAll(boolean isArchive) {
        List<Plant> plants = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ARCHIVE + " = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{isArchive ? "1" : "0"});

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                LocalDate date = DateParser.parseDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                boolean archive = cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVE)) == 1;
                int groupId = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_ID));
                int placeId = cursor.getInt(cursor.getColumnIndex(COLUMN_PLACE_ID));
                int speciesId = cursor.getInt(cursor.getColumnIndex(COLUMN_SPECIES_ID));
                int typeId = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE_ID));

                Group group = null;
                Place place = null;
                Species species = null;
                Type type = null;

                if (groupId != 0)
                    group = groupDatabase.getOneById(groupId);
                if (placeId != 0)
                    place = placeDatabase.getOneById(placeId);
                if (speciesId != 0)
                    species = speciesDatabase.getOneById(speciesId);
                if (typeId != 0)
                    type = typeDatabase.getOneById(typeId);

                plants.add(new Plant(id, title, date, description, group, place, species, type, archive));
                cursor.moveToNext();
            }
        }

        return plants;
    }

    @Override
    public boolean update(Plant plant) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(COLUMN_TITLE, plant.getTitle());
        cv.put(COLUMN_DATE, plant.getDate().toString());
        cv.put(COLUMN_DESCRIPTION, plant.getDescription());
        cv.put(COLUMN_GROUP_ID, plant.getGroup().getId());
        cv.put(COLUMN_PLACE_ID, plant.getPlace().getId());
        cv.put(COLUMN_SPECIES_ID, plant.getSpecies().getId());
        cv.put(COLUMN_TYPE_ID, plant.getType().getId());
        cv.put(COLUMN_ARCHIVE, plant.isArchive());
        long update = db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(plant.getId())});

        return update > 0;
    }

    @Override
    public boolean deleteById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        return delete > 0;
    }
}
