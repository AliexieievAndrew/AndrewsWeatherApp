package comw.example.user.andrewsweatherapp.Weather4Days;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import comw.example.user.andrewsweatherapp.Data.CitySelected;
import comw.example.user.andrewsweatherapp.JSON_SharedScreen;
import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.Weather4Days.Adapter.WeatherExpandableRecyclerViewAdapter;
import comw.example.user.andrewsweatherapp.Weather4Days.Models.TitleChild;
import comw.example.user.andrewsweatherapp.Weather4Days.Models.TitleCreator;
import comw.example.user.andrewsweatherapp.Weather4Days.Models.TitleParent;
import comw.example.user.andrewsweatherapp.WeatherApiCenter;
import comw.example.user.andrewsweatherapp.WeatherToday.WeatherFragmentMain;
import comw.example.user.andrewsweatherapp.WeatherManagement;
import comw.example.user.andrewsweatherapp.WeatherObject;


/*
 * Class extends Fragment class
 * for filling the forecast weather
 */
public class WeatherFragment4Days extends Fragment {

    Handler handler;

    public WeatherFragment4Days() {
        this.handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_four_days, container, false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherFourDaysUpdate(new CitySelected(getActivity()).getCity());
    }

    private void weatherFourDaysUpdate(String cityName) {
        new Thread() {
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

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewFourDays);
            recyclerView.setLayoutManager(layoutManager);

            WeatherExpandableRecyclerViewAdapter adapter =
                    new WeatherExpandableRecyclerViewAdapter(activity, initData((ArrayList<WeatherObject>) weatherList));
            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);

            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private List<ParentObject> initData(ArrayList<WeatherObject> weatherList) {


        TitleCreator titleCreator = new TitleCreator(getContext(),WeatherManagement.getWeatherMap(weatherList));

        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();

        Map<Integer, ArrayList<WeatherObject>> weatherMap =
                WeatherManagement.getWeatherMap(weatherList);
        List<Integer> keys = new ArrayList<>();

        for (Integer key: weatherMap.keySet()) {
            keys.add(key);
        }

        int count = 0;
        // set values to child
        for (TitleParent titleParentObject : titles) {
            List<Object> childList = new ArrayList<>();

            List<WeatherObject> weatherObjects = weatherMap.get(keys.get(count++));


            for (WeatherObject weatherObject : weatherObjects) {

                childList.add(new TitleChild(
                        weatherObject.getDateTime(),
                        WeatherManagement.getWeatherIcon(
                                getActivity(), weatherObject.getWeatherId(),
                                WeatherFragmentMain.getSunrise(), WeatherFragmentMain.getSunset(),
                                weatherObject.getDateTime() * 1000l),
                        weatherObject.getWeatherDescription(),
                        weatherObject.getTemp()));
            }
            titleParentObject.setChildObjectList(childList);

            parentObjects.add(titleParentObject);
        }
        return parentObjects;
    }
}
