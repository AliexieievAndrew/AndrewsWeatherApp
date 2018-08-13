package comw.example.user.andrewsweatherapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DB {
    private static final String DB_NAME = "weatherApp";
    private static final String DB_TABLE_NAME = "keyTab";
    private static final int DB_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY = "keyApp";
    public static final String COLUMN_TIME_OF_USE = "timeOfUse";

    private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_KEY + " TEXT, "
            + COLUMN_TIME_OF_USE + " LONG" + ");";

    private final String[] KEYS_TO_API ={
            "c2deccbfb91aecb71dee4b0d0c084366",
            "c3b908798ab765b2792ba1cd364f5772",
            "2b33780596f8200a4d1c5390e422e52e"};

    private final Context ctx;

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DB(Context ctx) {
        this.ctx = ctx;
    }

    //open connect
    public void open(){
        dbHelper = new DBHelper(ctx, DB_NAME,null,DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    // close connect
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // get all data from table DBHelper
    public Cursor getAllData(){
        return sqLiteDatabase.query(DB_TABLE_NAME,null,
                null,null,null,null,null);
    }

    public void addRec(String key, long timeOfUse) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_KEY, key);
        contentValues.put(COLUMN_TIME_OF_USE,timeOfUse);
        sqLiteDatabase.insert(DB_TABLE_NAME,null,contentValues);
    }

    // delete from DB
    public void delRec(long id) {
        sqLiteDatabase.delete(DB_TABLE_NAME,COLUMN_ID + " = " + id,null);
    }

    //edit key and time
    public void editRec(long id, String key, long timeOfUses) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_KEY, key);
        contentValues.put(COLUMN_TIME_OF_USE,timeOfUses);
        sqLiteDatabase.update(DB_TABLE_NAME,contentValues,COLUMN_ID + " = " + id,null);
    }

    // edit time
    public void editRec(long id, long timeOfUse) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIME_OF_USE,timeOfUse);
        sqLiteDatabase.update(DB_TABLE_NAME,contentValues,COLUMN_ID + " = " + id,null);
    }

    // create DataBase
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // create and inflate DB
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < KEYS_TO_API.length; i++) {
                contentValues.put(COLUMN_KEY,KEYS_TO_API[i]);
                contentValues.put(COLUMN_TIME_OF_USE, (long) new Date().getTime() - 3600000);
                db.insert(DB_TABLE_NAME,null,contentValues);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}