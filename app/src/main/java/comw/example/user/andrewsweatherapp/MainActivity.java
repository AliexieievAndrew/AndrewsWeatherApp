package comw.example.user.andrewsweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import comw.example.user.andrewsweatherapp.Weather4Days.WeatherFragment4Days;
import comw.example.user.andrewsweatherapp.WeatherHours.WeatherFragmentHoursToday;
import comw.example.user.andrewsweatherapp.WeatherToday.WeatherFragmentMain;

public class MainActivity extends AppCompatActivity {
    public static LinearLayout linearLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeatherFragmentMain()).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.containerWeatherHour, new WeatherFragmentHoursToday()).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.containerWeatherDays, new WeatherFragment4Days()).commit();
        }


    }
    @Override
    public void recreate() {
        super.recreate();
    }

    public static void setBackgroundApp(int background){
        linearLayoutMain.setBackgroundResource(background);
    }
}
