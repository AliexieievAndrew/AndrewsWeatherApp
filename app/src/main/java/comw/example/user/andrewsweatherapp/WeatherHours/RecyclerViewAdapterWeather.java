package comw.example.user.andrewsweatherapp.WeatherHours;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.WeatherManagement;
import comw.example.user.andrewsweatherapp.WeatherObject;
import comw.example.user.andrewsweatherapp.WeatherToday.WeatherFragmentMain;

/**/

public class RecyclerViewAdapterWeather extends RecyclerView.Adapter<RecyclerViewAdapterWeather.ViewHolder>{

    Context context;
    private ArrayList<WeatherObject> weatherList;

    // Constructor
    public RecyclerViewAdapterWeather(Context context, ArrayList<WeatherObject> weatherList) {
        this.context = context;
        this.weatherList = getWeatherList(weatherList);
    }

    // get weather in array only for today
    public ArrayList<WeatherObject> getWeatherList(ArrayList<WeatherObject> weatherList) {

        Calendar calendarTemp = Calendar.getInstance(); // temporary
        Calendar calendarNow = Calendar.getInstance(); // now

        ArrayList<WeatherObject> weatherArray = new ArrayList<>();

        for (WeatherObject weatherObject : weatherList) {

            long millisecondsCorrect = weatherObject.getDateTime()*1000l;
            calendarTemp.setTimeInMillis(millisecondsCorrect);

            if (calendarTemp.get(Calendar.DAY_OF_MONTH) == calendarNow.get(Calendar.DAY_OF_MONTH)) {
                weatherArray.add(weatherObject);
            }
        }
        return weatherArray;
    }

    // create holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_hours_today,parent,false);
        return new ViewHolder(view);
    }

    // связываем
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewWeatherTime.setText(getTimeHour(weatherList.get(position).getDateTime()));

        holder.textViewWeatherTimeIcon.setText(WeatherManagement.getWeatherIcon(
                (Activity)context, weatherList.get(position).getWeatherId(),
                WeatherFragmentMain.getSunrise(), WeatherFragmentMain.getSunset(),weatherList.get(position).getDateTime()*1000l));

        holder.textView_weatherTimeTemp.setText(weatherList.get(position).getTemp() + "°"); // ℃
    }


    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Typeface weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/weather.ttf");

        TextView textViewWeatherTime;
        TextView textViewWeatherTimeIcon;
        TextView textView_weatherTimeTemp;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewWeatherTime = itemView.findViewById(R.id.textViewWeatherTime);
            textViewWeatherTimeIcon = itemView.findViewById(R.id.textViewWeatherTimeIcon);
            textView_weatherTimeTemp = itemView.findViewById(R.id.textView_weatherTimeTemp);
            textViewWeatherTimeIcon.setTypeface(weatherFont);
        }
    }
    private String getTimeHour(long date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(new Date(date *1000));
    }
}
