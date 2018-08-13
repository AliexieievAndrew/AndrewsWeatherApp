package comw.example.user.andrewsweatherapp.Weather4Days.Models;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import comw.example.user.andrewsweatherapp.WeatherManagement;
import comw.example.user.andrewsweatherapp.WeatherObject;

public class TitleCreator {
    private final String TAG_TITLE = "myLogs";

    private List<TitleParent> titleParents;
    private static final int TIME_DAY = 15;
    private static final int TIME_NIGHT = 3;

    Context mContext;

    public TitleCreator(Context context, LinkedHashMap<Integer, ArrayList<WeatherObject>> weatherMap) {
        titleParents = new ArrayList<>();
        Log.d(TAG_TITLE, "TitleCreator: ");

        mContext = context;

        List<Integer> keys = getListWeekDays(weatherMap);

        for (int i = 0; i < weatherMap.size(); i++) {

            String weekDay = WeatherManagement.getWeekDay(keys.get(i));
            Log.d(TAG_TITLE, "weekday = " + weekDay);
            String date = getTime(weatherMap.get(keys.get(i)).get(i).getDateTime());
            Log.d(TAG_TITLE, "date = " + date);

            int weatherDay = getWeatherDayOrNight(weatherMap.get(keys.get(i)), TIME_DAY);
            int weatherNight = getWeatherDayOrNight(weatherMap.get(keys.get(i)), TIME_NIGHT);

            String icon = getPriorityWeatherIcon(weatherMap.get(keys.get(i)));

            Log.d(TAG_TITLE, "TitleCreator: icon = " + icon);


            TitleParent titleParent = new TitleParent(weekDay, date, weatherDay, weatherNight,icon);
            titleParents.add(titleParent);
        }
    }

    private List<Integer> getListWeekDays(LinkedHashMap <Integer, ArrayList<WeatherObject>> map) {
        List<Integer> keys = new ArrayList<>();
        for (Integer key : map.keySet()) {
            keys.add(key);
        }
        return keys;
    }
    // возвращает температуру
    // @TIME_DAY день = 15:00
    // @TIME_NIGHT ночь = 03:00
    private static int getWeatherDayOrNight(ArrayList<WeatherObject> dayWeatherList, int timeDay){
        int temp = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeDay);

        for (int i = 0; i < dayWeatherList.size(); i++) {

            long ms = dayWeatherList.get(i).getDateTime() * 1000l;
            Calendar calendarTemp = Calendar.getInstance();
            calendarTemp.setTimeInMillis(ms);

            if (calendar.get(Calendar.HOUR_OF_DAY) == calendarTemp.get(Calendar.HOUR_OF_DAY)) {
                temp = dayWeatherList.get(i).getTemp();
                return temp;
            }
        }
        return temp;
    }

    // возвращает иконку с погодой, с наиболее поплуярной погодой за день
    private String getPriorityWeatherIcon(ArrayList<WeatherObject> dayWeatherList) {
        String ic = "";
        List<Integer> list = new ArrayList<>();

        for (WeatherObject weatherObject: dayWeatherList) {
            list.add(weatherObject.getWeatherId());
        }

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        Integer count;

        for (Integer i : list) {
            count = map.get(i);
            map.put(i, count == null ? 1 : count + 1);
        }

        int key = Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        Log.d(TAG_TITLE, "getPriorityWeatherIcon: key = " + key);

        ic = WeatherManagement.getWeatherIcon
                ((Activity) mContext,key,
                        WeatherManagement.SIMPLE_SUNRISE,
                        WeatherManagement.SIMPLE_SUNSET,
                        WeatherManagement.SIMPLE_CURRENT_TIME);
        return ic;
    }

    private String getTime(long date) {
        DateFormat df = new SimpleDateFormat("dd.MM");
        return df.format(new Date(date *1000));
    }

    public List<TitleParent> getAll() {
        return titleParents;
    }
}
