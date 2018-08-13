package comw.example.user.andrewsweatherapp.WeatherToday;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import comw.example.user.andrewsweatherapp.Data.CitySelected;
import comw.example.user.andrewsweatherapp.Geo.ReturnGeoLocationActivity;
import comw.example.user.andrewsweatherapp.JSON_SharedScreen;
import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.WeatherApiCenter;
import comw.example.user.andrewsweatherapp.WeatherManagement;

/*
 * Class extends Fragment class
 * for filling the current temperature
 */

public class WeatherFragmentMain extends Fragment implements View.OnClickListener{

    // in testing, may be exception (Thread)
    private static long sunset;
    private static long sunrise;

    // assets/font/weather.tiff
    Typeface weatherFont;

    Handler handler;

    TextView textViewLocation;
    TextView textViewCurrentTime;
    TextView textViewWeatherTempNow;
    TextView textViewWeatherNowIcon;
    TextView textViewWeatherDescriptionNow;

    ImageView imgGeoLocation;
    ImageView imgEditLocation;

    public WeatherFragmentMain() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_weather_main, container,false);

        textViewLocation = (TextView) mainView.findViewById(R.id.textViewLocation);
        textViewCurrentTime = (TextView) mainView.findViewById(R.id.textViewCurrentTime);
        textViewWeatherTempNow = (TextView) mainView.findViewById(R.id.textViewWeatherTempNow);
        textViewWeatherNowIcon = (TextView) mainView.findViewById(R.id.textViewWeatherTempNowIcon);
        textViewWeatherDescriptionNow = (TextView) mainView.findViewById(R.id.textViewWeatherDescriptionNow);

        imgGeoLocation = (ImageView) mainView.findViewById(R.id.imgGeoLocation);
        imgEditLocation = (ImageView) mainView.findViewById(R.id.imgEditLocation);

        imgGeoLocation.setOnClickListener(this);

        textViewWeatherNowIcon.setTypeface(weatherFont);

        return mainView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");

        weatherDataUpdate(new CitySelected(getActivity()).getCity());
    }

    private void weatherDataUpdate(String cityName) {
        new Thread() {
            @Override
            public void run() {

                final JSONObject json = WeatherApiCenter.getJSONWeather(getActivity(), cityName, WeatherApiCenter.OPEN_WEATHER_MAP_API_NOW_NAME_CITY);

                final JSONObject jsonSaved = JSON_SharedScreen.getJSON(getActivity(), JSON_SharedScreen.FILE_NAME_WEATHER_NOW);

                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            parsingWeather(jsonSaved);

                            Toast.makeText(getActivity(),"something wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            parsingWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    // Parsing weather
    private void parsingWeather(JSONObject json) {
        try {
            textViewLocation.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            // get JSONObject.array ("weather" - array, getJSONObject(0) - null array's index)
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            textViewWeatherDescriptionNow.setText(details.getString("description"));
            textViewWeatherTempNow.setText((int)Math.round(main.getDouble("temp")) + "°");//  ℃

            DateFormat df = DateFormat.getDateTimeInstance();
            String updated = df.format(new Date(json.getLong("dt")*1000));

            textViewWeatherNowIcon.setText(WeatherManagement.getWeatherIcon(getActivity(),details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000,new Date().getTime()));

            sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000l;
            sunset = json.getJSONObject("sys").getLong("sunset") * 1000l;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static long getSunset() {
        return sunset;
    }

    public static long getSunrise() {
        return sunrise;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgGeoLocation:
                Intent intentLocation = new Intent(getActivity(), ReturnGeoLocationActivity.class);

                startActivityForResult(intentLocation,1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) {
            return;
        }

        String cityName = data.getStringExtra("cityName");
        Log.d("CITY", "city = " + cityName);

        new CitySelected(getActivity()).setCity(cityName);
        
        onRestart();
    }


    /*
     * Restart method
     *
     * using when needs to refresh weather info (change city)
     */
    private void onRestart() {
        new Handler().post(new Runnable() {

            @Override
            public void run()
            {
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
    }
}