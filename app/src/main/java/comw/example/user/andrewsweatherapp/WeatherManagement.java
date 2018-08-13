package comw.example.user.andrewsweatherapp;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/*
 * Class for weather data processing
 */
public class WeatherManagement {

    /*
     * Constants for obtaining daytime temperature icons,
     * works only in the case of a clear sky by day or night
     */
    public static final int SIMPLE_SUNRISE = 1;
    public static final int SIMPLE_CURRENT_TIME = 2;
    public static final int SIMPLE_SUNSET = 3;

    /*
     * Return ArrayList<WeatherObject> with parsed info
     * 5 days
     */
    public static List<WeatherObject> getListWeather (Activity activity, JSONObject jsonObject) throws JSONException {
        ArrayList<WeatherObject> weatherList = new ArrayList<>();

        JSONObject weather = jsonObject;

        for (int i = 0; i < weather.getJSONArray("list").length(); i++) {

            // take array
            JSONObject details = weather.getJSONArray("list").getJSONObject(i);

            // long dateTime, int temp, int weatherId, int humidity, String weatherMain, String weatherDescription
            weatherList.add(new WeatherObject(
                    details.getLong("dt"),
                    details.getJSONObject("main").getInt("temp"),
                    details.getJSONArray("weather").getJSONObject(0).getInt("id"),
                    details.getJSONObject("main").getInt("humidity"),
                    details.getJSONArray("weather").getJSONObject(0).getString("main"),
                    details.getJSONArray("weather").getJSONObject(0).getString("description")));
        }
        return weatherList;
    }
    /*
     * Return weather icon (String),
     * needs added assets
     */

    public final static String getWeatherIcon(Activity activity, int actualId, long sunrise, long sunset, long currentTime) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800){

            /* Using sunrise and sunset to take an icon in day time
             * day or night
             * when weather clean
             */
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = activity.getString(R.string.weather_sunny); // чистое небо днем
            } else {
                icon = activity.getString(R.string.weather_clear_night); // чистое небо ночью
            }
        } else {
            switch(id) {
                case 2 :
                    icon = activity.getString(R.string.weather_thunder);
                    break;
                case 3 :
                    icon = activity.getString(R.string.weather_drizzle);
                    break;
                case 7 :
                    icon = activity.getString(R.string.weather_foggy);
                    break;
                case 8 :
                    icon = activity.getString(R.string.weather_cloudy);
                    break;
                case 6 :
                    icon = activity.getString(R.string.weather_snowy);
                    break;
                case 5 :
                    icon = activity.getString(R.string.weather_rainy);
                    break;
            }
        }
        Log.d("TAGTAG", "Icon:  = " + icon + " = ok");
        return icon;
    }

    /*
     * Return weather Map
     * key - day of the week
     * value - weather list per day in hours
     */
    public static LinkedHashMap <Integer, ArrayList<WeatherObject>> getWeatherMap(ArrayList<WeatherObject> weatherList) {
        LinkedHashMap<Integer, ArrayList<WeatherObject>> weatherMap = new LinkedHashMap<>();

        int tempDayOfWeek = 0;

        ArrayList<WeatherObject> weatherArrayList = new ArrayList<>();

        // temporary
        Calendar calendarTemp = Calendar.getInstance();
        // now
        Calendar calendarNow = Calendar.getInstance();

        for (WeatherObject weatherObject : weatherList) {
            long millisecondsCorrect = weatherObject.getDateTime()*1000l;
            calendarTemp.setTimeInMillis(millisecondsCorrect);

            if (calendarTemp.get(Calendar.DAY_OF_MONTH) != calendarNow.get(Calendar.DAY_OF_MONTH)) {
                if (tempDayOfWeek == 0) {
                    tempDayOfWeek = calendarTemp.get(Calendar.DAY_OF_WEEK);
                }

                if (tempDayOfWeek == calendarTemp.get(Calendar.DAY_OF_WEEK)) {
                    weatherArrayList.add(weatherObject);
                } else {
                    weatherMap.put(tempDayOfWeek,weatherArrayList);
                    tempDayOfWeek = calendarTemp.get(Calendar.DAY_OF_WEEK);
                    weatherArrayList = new ArrayList<>();
                }
            }
        }
        return weatherMap;
    }
    /*
     * Return weekday in String
     */
    public static String getWeekDay(int weekDay) {
        String day = null;
        switch (weekDay) {
            case Calendar.SUNDAY:
                day = new String("SUNDAY");
                break;
            case Calendar.MONDAY:
                day = new String("MONDAY");
                break;
            case Calendar.TUESDAY:
                day = new String("TUESDAY");
                break;
            case Calendar.WEDNESDAY:
                day = new String("WEDNESDAY");
                break;
            case Calendar.THURSDAY:
                day = new String("THURSDAY");
                break;
            case Calendar.FRIDAY:
                day = new String("FRIDAY");
                break;
            case Calendar.SATURDAY:
                day = new String("SATURDAY");
                break;
        }
        return day;
    }
    // in testing
    private boolean isDay(long timeMillis) {

        long ms = timeMillis*1000l;

        Calendar calendarDay = Calendar.getInstance();
        calendarDay.set(Calendar.HOUR_OF_DAY, 6);

        Calendar calendarNight2 = Calendar.getInstance();
        calendarNight2.set(Calendar.HOUR_OF_DAY, 22);

        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTimeInMillis(ms);

        if (calendarTemp.getTimeInMillis() < calendarDay.getTimeInMillis()) {
            return false;
        } else
        if (calendarTemp.getTimeInMillis() < calendarNight2.getTimeInMillis()) {
            return true;
        } else {
            return false;
        }
    }
}
