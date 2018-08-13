package comw.example.user.andrewsweatherapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import comw.example.user.andrewsweatherapp.Data.KeyBox;

/*
 * Class for creating weather api
 */
public class WeatherApiCenter {
    public static final String OPEN_WEATHER_MAP_API_FIVE_DAYS =
            "http://api.openweathermap.org/data/2.5/forecast?lat=%.4f&lon=%.4f&units=metric";

    public static final String OPEN_WEATHER_MAP_API_NOW =
            "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric";

    public static final String OPEN_WEATHER_MAP_API_NOW_NAME_CITY =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    public static final String OPEN_WEATHER_MAP_API__NAME_CITY_FIVE_DAYS =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric";


    /*
     * Return JSON object weather data
     *
     * openWeatherMapApi - use the constant
     *      OPEN_WEATHER_MAP_API_NOW_NAME_CITY - to take weather now or
     *      OPEN_WEATHER_MAP_API__NAME_CITY_FIVE_DAYS  - to take weather forecast
     */
    public static JSONObject getJSONWeather(Context context, String cityName, String openWeatherMapApi){
        try {
            Log.d("myLogs", "getJSONWeather: ");
            URL url = new URL(String.format(openWeatherMapApi, cityName));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", new KeyBox((Activity) context).getKey());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp="";
            while((tmp=reader.readLine()) != null) {

                //добавляем прочитанную линию к обьекту StringBuffer и после переводим курсор ниже
                json.append(tmp).append("\n");
            }
            reader.close();

            // backup json
            if (openWeatherMapApi == OPEN_WEATHER_MAP_API_NOW_NAME_CITY) {
                JSON_SharedScreen.saveJSON(context, json.toString(), JSON_SharedScreen.FILE_NAME_WEATHER_NOW);
            } else {
                JSON_SharedScreen.saveJSON(context, json.toString(), JSON_SharedScreen.FILE_NAME_WEATHER_5_DAYS);
            }

            JSONObject data = new JSONObject(json.toString());

            /*
             * Checking
             *
             * if ("cod") != 200 - the query succeeded
             * return ready to use JSON Weather Object
             */
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
