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
import comw.example.user.andrewsweatherapp.MainActivity;
import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.WeatherApiCenter;
import comw.example.user.andrewsweatherapp.WeatherManagement;

/*
 * Class extends Fragment class
 * for filling the current temperature
 */

public class WeatherFragmentMain extends Fragment implements View.OnClickListener{

    private final static String TAG_WEATHER_FRAGMENT_MAIN = "weatherMain";

    // in testing, may be exception (Thread)
    // getting actual sunset and sunrise
    private static long sunset;
    private static long sunrise;

    // assets/font/weather.tiff
    Typeface weatherFont;

    // assets/font/weather.tiff
    Typeface descriptionFont;

    Handler handler;

    TextView textViewLocation;
    TextView textViewWeatherTempNow;
    TextView textViewWeatherDescriptionNow;

    ImageView imgGeoLocation;

    public WeatherFragmentMain() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_weather_main, container,false);

        textViewLocation = (TextView) mainView.findViewById(R.id.textViewLocation);
        textViewWeatherTempNow = (TextView) mainView.findViewById(R.id.textViewWeatherTempNow);
        textViewWeatherDescriptionNow = (TextView) mainView.findViewById(R.id.textViewWeatherDescriptionNow);

        imgGeoLocation = (ImageView) mainView.findViewById(R.id.imgGeoLocation);

        imgGeoLocation.setOnClickListener(this);

        textViewWeatherTempNow.setTypeface(descriptionFont);
        textViewLocation.setTypeface(descriptionFont);
        textViewWeatherDescriptionNow.setTypeface(descriptionFont);

        return mainView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        descriptionFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

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
//            textViewLocation.setText(json.getString("name").toUpperCase(Locale.US) +
//                    ", " +
//                    json.getJSONObject("sys").getString("country"));

            textViewLocation.setText(json.getString("name"));

            // get JSONObject.array ("weather" - array, getJSONObject(0) - null array's index)
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            textViewWeatherDescriptionNow.setText(details.getString("description"));
            textViewWeatherTempNow.setText((int)Math.round(main.getDouble("temp")) + "°");//  ℃

            DateFormat df = DateFormat.getDateTimeInstance();
            String updated = df.format(new Date(json.getLong("dt")*1000));

            // testing dynamic background
            MainActivity.setBackgroundApp(WeatherManagement.getWeatherBackground(getActivity(),details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000l,
                    json.getJSONObject("sys").getLong("sunset") * 1000l, new Date().getTime()));

            Log.d(TAG_WEATHER_FRAGMENT_MAIN, "parsingWeather: sunrise = "
                    + json.getJSONObject("sys").getLong("sunrise"));
            Log.d(TAG_WEATHER_FRAGMENT_MAIN, "parsingWeather: sunset = "
                    + json.getJSONObject("sys").getLong("sunset"));

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

    // onRestart app when update city
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) {
            return;
        }

        String cityName = data.getStringExtra("cityName");
        Log.d(TAG_WEATHER_FRAGMENT_MAIN, "city = " + cityName);

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