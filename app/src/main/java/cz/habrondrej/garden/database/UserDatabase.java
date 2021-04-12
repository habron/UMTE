package cz.habrondrej.garden.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import cz.habrondrej.garden.model.User;

public class UserDatabase extends DatabaseHelper {

    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String DEFAULT_USERNAME = "Uživatel";

    public UserDatabase(@Nullable Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";

        db.execSQL(createTableStatement);

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, DEFAULT_USERNAME);
        db.insert(USER_TABLE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public User getUser() throws IllegalStateException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int userID = cursor.getInt(0);
            String username = cursor.getString(1);
            String password = cursor.getString(2);

            return new User(userID, username, password);
        }

        throw new IllegalStateException("Can not get user!");
    }


    public boolean update(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPasswordHash());

        int update = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});

        return update > 0;
    }
}
