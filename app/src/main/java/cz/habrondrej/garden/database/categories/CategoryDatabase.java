package cz.habrondrej.garden.database.categories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.database.DatabaseHelper;

public abstract class CategoryDatabase<T> extends DatabaseHelper<T> {

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";

    public CategoryDatabase(@Nullable Context context) {
        super(context);
    }

    public static void onCreateDB(SQLiteDatabase db) {
        GroupDatabase.onCreateCatDB(db);
        PlaceDatabase.onCreateCatDB(db);
        SpeciesDatabase.onCreateCatDB(db);
        TypeDatabase.onCreateCatDB(db);
    }

    public static void onUpgradeDB(SQLiteDatabase db, int oldVersion, int newVersion) {
        GroupDatabase.onUpgradeCatDB(db, oldVersion, newVersion);
        PlaceDatabase.onUpgradeCatDB(db, oldVersion, newVersion);
        SpeciesDatabase.onUpgradeCatDB(db, oldVersion, newVersion);
        TypeDatabase.onUpgradeCatDB(db, oldVersion, newVersion);
    }

    protected boolean deleteById(int id, String tableName) {
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(tableName, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        return delete > 0;
    }
}
