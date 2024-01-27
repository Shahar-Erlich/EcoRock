package com.ecorock.game.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.ecorock.game.User;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "user_name";
    private static final String COLUMN_MAIL = "user_mail";
    private static final String COLUMN_PASSWORD = "user_pass";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME+ " TEXT, " +
                COLUMN_MAIL+ " TEXT, " +
                COLUMN_PASSWORD + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(String userName,String mail ,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USERNAME, userName);
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean FindUserByMailExists(String mail)
    {
        boolean isUnique = false;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+  COLUMN_ID +" FROM "+ TABLE_NAME + " WHERE " + COLUMN_MAIL + " = ?";

        cursor = db.rawQuery(query, new String[]{mail});
        isUnique = cursor.getCount() == 0;
        cursor.close();

        return isUnique;
    }
    public User FindUserByMail(String mail)
    {
        User user;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + COLUMN_MAIL + " = ?";

        cursor = db.rawQuery(query, new String[]{mail});
        if(cursor.getCount()==1) {
            cursor.moveToFirst();
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return user;
        }

        return null;
    }
    public Boolean CheckLogIn(String Email, String Pass)
    {

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MAIL + " = '"+ Email+ "' AND " + COLUMN_PASSWORD + " = '" + Pass + "'";
        try (SQLiteDatabase db = this.getReadableDatabase())
        {
            Cursor cursor = db.rawQuery(query,null);
            Boolean result = false;
            if (cursor.getCount()!=-1){
               cursor.moveToFirst();
                    int exists = cursor.getCount();
                    result = exists==1;
                }
                cursor.close();
            return result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String name,String mail, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, name);
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_PASSWORD, password);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
