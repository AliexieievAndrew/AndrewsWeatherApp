package comw.example.user.andrewsweatherapp.WeatherHours;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import comw.example.user.andrewsweatherapp.Data.CitySelected;
import comw.example.user.andrewsweatherapp.JSON_SharedScreen;
import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.WeatherApiCenter;
import comw.example.user.andrewsweatherapp.WeatherManagement;
import comw.example.user.andrewsweatherapp.WeatherObject;

// фрагмент для наполнения погоды сегодня почасово до конца дня
/*
 * Class extends Fragment class
 * for filling the current temperature in hours
 *
 * interval 3 hours
 *
 * Dynamic object
 * Using RecyclerView
 *
 */
public class WeatherFragmentHoursToday extends Fragment {

    Handler handler;

    public WeatherFragmentHoursToday(){
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View viewHoursToday = inflater.inflate(R.layout.weather_fragment_hours_today,container,false);

        return viewHoursToday;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherPerHourTodayUpdate(new CitySelected(getActivity()).getCity());
    }

    private void weatherPerHourTodayUpdate(String cityName) {
        new Thread () {
            @Override
            public void run() {
                final JSONObject json = WeatherApiCenter.getJSONWeather(getActivity(), cityName, WeatherApiCenter.OPEN_WEATHER_MAP_API__NAME_CITY_FIVE_DAYS);
                final JSONObject jsonSaved = JSON_SharedScreen.getJSON(getActivity(), JSON_SharedScreen.FILE_NAME_WEATHER_5_DAYS);

                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            parsingWeather(jsonSaved, getActivity());

                            Toast.makeText(getActivity(),"something wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            parsingWeather(json, getActivity());
                        }
                    });
                }
            }
        }.start();
    }

    private void parsingWeather(JSONObject json, Activity activity) {

        try {
            List<WeatherObject> weatherList = WeatherManagement.getListWeather(activity, json);

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerViewAdapterWeather adapter = new RecyclerViewAdapterWeather(activity, (ArrayList<WeatherObject>) weatherList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
