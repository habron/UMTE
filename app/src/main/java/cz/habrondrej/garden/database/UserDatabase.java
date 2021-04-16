package cz.habrondrej.garden.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import cz.habrondrej.garden.model.User;

public class UserDatabase extends DatabaseHelper<User> {

    public static final String TABLE_NAME = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String DEFAULT_USERNAME = "Uživatel";

    private static final String DATABASE_CREATE_TABLE =  "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT)";

    public UserDatabase(@Nullable Context context) {
        super(context);
    }


    public static void onCreateDB(@NotNull SQLiteDatabase db) {
        System.out.println("User onCreate");
        db.execSQL(DATABASE_CREATE_TABLE);

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, DEFAULT_USERNAME);
        db.insert(TABLE_NAME, null, cv);
    }


    public static void onUpgradeDB(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreateDB(db);
    }

    public User getUser() throws IllegalStateException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int userID = cursor.getInt(0);
            String username = cursor.getString(1);
            String password = cursor.getString(2);

            return new User(userID, username, password);
        }

        throw new IllegalStateException("Can not get user!");
    }

    @Override
    public boolean create(User user) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPasswordHash());
        long insert = db.insert(TABLE_NAME, null, cv);

        return insert > 0;
    }

    @Override
    public User getOneById(int id) throws IndexOutOfBoundsException {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String username = cursor.getString(1);
            String password = cursor.getString(2);

            return new User(id, username, password);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>();
    }

    @Override
    public boolean update(@NotNull User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPasswordHash());

        int update = db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});

        return update > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
