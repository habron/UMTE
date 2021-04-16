package cz.habrondrej.garden.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.database.categories.CategoryDatabase;

public abstract class DatabaseHelper<T> extends SQLiteOpenHelper implements IRepository<T> {

    public static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        this(context, "garden.db", null, DB_VERSION);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        UserDatabase.onCreateDB(db);
        CategoryDatabase.onCreateDB(db);
        PlantDatabase.onCreateDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserDatabase.onUpgradeDB(db, oldVersion, newVersion);
        CategoryDatabase.onUpgradeDB(db, oldVersion, newVersion);
        PlantDatabase.onUpgradeDB(db, oldVersion, newVersion);
    }

}
