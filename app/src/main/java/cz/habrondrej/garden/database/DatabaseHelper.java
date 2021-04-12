package cz.habrondrej.garden.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public abstract class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        this(context, "garden.db", null, DB_VERSION);
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

}
