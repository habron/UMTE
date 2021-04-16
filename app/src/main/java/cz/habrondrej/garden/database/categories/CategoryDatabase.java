package cz.habrondrej.garden.database.categories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import cz.habrondrej.garden.database.IRepository;
import cz.habrondrej.garden.database.DatabaseHelper;

public abstract class CategoryDatabase<T> extends DatabaseHelper<T> {

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";

    public CategoryDatabase(@Nullable Context context) {
        super(context);
    }

    protected boolean deleteById(int id, String tableName) {
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(tableName, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        return delete > 0;
    }
}
