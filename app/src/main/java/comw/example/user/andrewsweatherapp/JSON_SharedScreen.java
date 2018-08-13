package comw.example.user.andrewsweatherapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
 * Class to save JSON object
 *
 * Has such constants - file names to save
 *
 * Use:
 * FILE_NAME_WEATHER_NOW - to save weather now
 * FILE_NAME_WEATHER_5_DAYS - to save weather forecast
 */
public class JSON_SharedScreen {
    private static final String TAG_SAVE_JSON = "saveJSON";

    public static final String FILE_NAME_WEATHER_NOW = "weatherData.json";
    public static final String FILE_NAME_WEATHER_5_DAYS = "weatherDataFiveDays.json";

    /*
     * Saving String in *.json
     *
     * fileName - use constants
     * FILE_NAME_WEATHER_NOW - to save weather now or
     * FILE_NAME_WEATHER_5_DAYS - to save weather forecast
     */

    protected static void saveJSON (Context context, String jsonString, String fileName) {
        Log.d(TAG_SAVE_JSON, "saveJSON: fileName = " + fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(fileName,Context.MODE_PRIVATE)));
            bw.write(jsonString);
            bw.close();

            Log.d(TAG_SAVE_JSON, "saveJSON: done successful");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Return saved String in *.json
     *
     * fileName - use constants
     * FILE_NAME_WEATHER_NOW - to return saved weather now or
     * FILE_NAME_WEATHER_5_DAYS - to return saved weather forecast
     */
    public static JSONObject getJSON(Context context, String fileName) {
        JSONObject jsonObject = null;
        BufferedReader bufferedReader = null;
        StringBuffer json = new StringBuffer(1024);

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            String tmp = "";

            while((tmp = bufferedReader.readLine()) != null) {
                json.append(tmp);
            }

            jsonObject = new JSONObject(json.toString());

            Log.d(TAG_SAVE_JSON, "getJSON: done successful");

            return jsonObject;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG_SAVE_JSON,"FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG_SAVE_JSON,"IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG_SAVE_JSON,"JSONException");
        }
        return null;
    }
}
