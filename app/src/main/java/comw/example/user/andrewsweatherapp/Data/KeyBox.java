package comw.example.user.andrewsweatherapp.Data;



import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import java.util.Date;

import comw.example.user.andrewsweatherapp.Data.DB;

public class KeyBox {

    private final int ONE_MINUTE_MILLISECONDS = 1000;

    private static final String LOG_TAG = "apiKeyTag";

    private static long currentTime;
    private static long timeOfUse;

    private DB db;
    private Cursor cursor;

    public KeyBox(Activity activity) {
        db = new DB(activity);
        db.open();
    }

    public String getKey() {
        String key = checkTimer();

        if (key == null) {
            return null;
        } else {
            return key;
        }
    }

    public String checkTimer() {
        String key = null;
        cursor = db.getAllData();

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DB.COLUMN_ID);
            int keyIndex = cursor.getColumnIndex(DB.COLUMN_KEY);
            int TimeOfUseIndex = cursor.getColumnIndex(DB.COLUMN_TIME_OF_USE);

            currentTime = new Date().getTime();

            do {
                timeOfUse = cursor.getLong(TimeOfUseIndex);

                if(currentTime - ONE_MINUTE_MILLISECONDS > timeOfUse) {

                    long id = cursor.getLong(idIndex);
                    key = new String(cursor.getString(keyIndex));

                    Log.d(LOG_TAG, "key: " + key);
                    Log.d(LOG_TAG, "time of use = " + timeOfUse);
                    Log.d(LOG_TAG, "currentTime = " + currentTime);
                    Log.d(LOG_TAG, "operation is " + (currentTime - ONE_MINUTE_MILLISECONDS > timeOfUse));

                    db.editRec(id,currentTime);

                    return key;
                }
            } while (cursor.moveToNext());
        }
        return null;
    }
}
