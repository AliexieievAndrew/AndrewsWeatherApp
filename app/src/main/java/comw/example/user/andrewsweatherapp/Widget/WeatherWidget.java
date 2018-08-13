package comw.example.user.andrewsweatherapp.Widget;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.WeatherApiCenter;
import comw.example.user.andrewsweatherapp.WeatherManagement;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final String ACTION_UPDATE_WIDGET_TIME = "update_widget";
    private static final String ACTION_UPDATE_WIDGET_WEATHER = "update_widget_weather";

    static  final String LOG_WEATHER = "MyService";

    private static final long TIME_UPDATE_TIME = 60000; // 1 minute
    private static final long TIME_UPDATE_WEATHER = 3*60*1000; // 3 minutes

    private static String time, weekDay;
    private static String city;

    SharedPreferences sharedPreferences;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        views.setTextViewText(R.id.textWidgetTime, time);
        views.setTextViewText(R.id.textWidgetweekDay, weekDay);
        views.setTextViewText(R.id.textWidgetCity, city);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        long time = (long) System.currentTimeMillis();

        // ** alarm settings for the update time
        Intent intentTime = new Intent(context, WeatherWidget.class);
        intentTime.setAction(ACTION_UPDATE_WIDGET_TIME);

        PendingIntent pendingIntentTime = PendingIntent.getBroadcast(context,0,intentTime,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,time,TIME_UPDATE_TIME,pendingIntentTime);

        // ** alarm settings for the update weather
        Intent intentWeather = new Intent(context, WeatherWidget.class);
        intentWeather.setAction(ACTION_UPDATE_WIDGET_WEATHER);
        PendingIntent pendingIntentWeather = PendingIntent.getBroadcast(context,0,intentWeather,0);

        AlarmManager alarmManagerWeather = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerWeather.setRepeating(AlarmManager.RTC, time, TIME_UPDATE_WEATHER,pendingIntentWeather);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equalsIgnoreCase(ACTION_UPDATE_WIDGET_TIME)) {
            Log.d("MyService", "ACTION_UPDATE_WIDGET_TIME");
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context,appWidgetManager,ids);
        } else if(intent.getAction().equalsIgnoreCase(ACTION_UPDATE_WIDGET_WEATHER)){
            Log.d("MyService", "ACTION_UPDATE_WIDGET_WEATHER");
//            context.startService(new Intent(context,UpdateService.class));

            GetJSONTask getJSONTask = new GetJSONTask(context);
            getJSONTask.execute();

            try {
                JSONObject jsonObject = getJSONTask.get(2,TimeUnit.SECONDS);
                Log.d("MyService", "i have JSON");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        sharedPreferences = context.getSharedPreferences("cityName",Context.MODE_PRIVATE);
        city = sharedPreferences.getString("city","Kyiv,ua");
        Log.d("MyService", "city = " + city);

        Calendar calendar = Calendar.getInstance();

        time = getTime(System.currentTimeMillis());
        weekDay = WeatherManagement.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));



        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {

        // Enter relevant functionality for when the last widget is disabled
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(ACTION_UPDATE_WIDGET_TIME);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    public static String getTime(long date) {
        DateFormat df = new SimpleDateFormat("HH.mm");

        return df.format(new Date(date));
    }

    public class GetJSONTask extends AsyncTask<Void, Void, JSONObject>{
        private Context mContext;
        JSONObject data;

        public GetJSONTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            data = getJSONWeather(city);
            return  data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONObject("main");

                RemoteViews updateViews =
                        new RemoteViews(mContext.getPackageName(),R.layout.weather_widget);
                updateViews.setTextViewText
                    (R.id.textWidgetDescription,details.getString("description"));
                updateViews.setTextViewText
                    (R.id.textWidgetTemp,(int)Math.round(main.getDouble("temp")) + "°");


                updateViews.setImageViewBitmap(R.id.textWidgetWeather,
                        convertToImage(getWeatherIcon(details.getInt("id"),
                                jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                                jsonObject.getJSONObject("sys").getLong("sunset") * 1000,new Date().getTime())));

                ComponentName widget = new ComponentName(mContext.getPackageName(), WeatherWidget.class.getName());
                AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
                manager.updateAppWidget(widget,updateViews);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.d("MyService", "NullPointerException");
            }
        }
        private JSONObject getJSONWeather(String city){

            JSONObject mJSONWeather;

            try {
                URL url = new URL(String.format(WeatherApiCenter.OPEN_WEATHER_MAP_API_NOW_NAME_CITY, city));

                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                connection.addRequestProperty("x-api-key", "2b33780596f8200a4d1c5390e422e52e");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);

                String tmp = "";
                while((tmp = reader.readLine()) != null) {
                    json.append(tmp).append("\n");
                }
                reader.close();

                mJSONWeather = new JSONObject(json.toString());

                if(mJSONWeather.getInt("cod") == 200){
                    Log.d("MyService", "AsyncTask: jsonObject: successful");
                    return mJSONWeather;
                }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            Log.d("MyService", "AsyncTask: jsonObject: return null");

            return  null;
        }

        private final String getWeatherIcon(int actualId, long sunrise, long sunset, long currentTime) {
            int id = actualId / 100;
            String icon = "";

            if(actualId == 800){

                if(currentTime >= sunrise && currentTime < sunset) {
                    icon = mContext.getString(R.string.weather_sunny); // чистое небо днем
                } else {
                    icon = mContext.getString(R.string.weather_clear_night); // чистое небо ночью
                }
            } else {
                // проверем погоду, и в зависимости от id (данных погоды) выставляем шрифт-иконку
                switch(id) {
                    case 2 :
                        icon = mContext.getString(R.string.weather_thunder);
                        break;
                    case 3 :
                        icon = mContext.getString(R.string.weather_drizzle);
                        break;
                    case 7 :
                        icon = mContext.getString(R.string.weather_foggy);
                        break;
                    case 8 :
                        icon = mContext.getString(R.string.weather_cloudy);
                        break;
                    case 6 :
                        icon = mContext.getString(R.string.weather_snowy);
                        break;
                    case 5 :
                        icon = mContext.getString(R.string.weather_rainy);
                        break;
                }
            }
            Log.d("MyService", "Icon:  = " + icon + " = ok");
            return icon;
        }

        private Bitmap convertToImage(String text) {
            Bitmap btmText = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas cnvText = new Canvas(btmText);

            // assets/font/weather.tiff
            Typeface  weatherFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/weather.ttf");
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setSubpixelText(true);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setTypeface(weatherFont);

            cnvText.drawText(text, 20, 80, paint);

            return btmText;
        }
    }
}