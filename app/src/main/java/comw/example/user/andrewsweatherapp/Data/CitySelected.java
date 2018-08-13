package comw.example.user.andrewsweatherapp.Data;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class CitySelected {
    private final String KEY_CITY = "city";
    private final String KEY_LAT = "LAT";
    private final String KEY_LON = "LON";

    float defLat = 50.4333f;
    float deflon = 30.5167f;


    SharedPreferences sharedPreferences;

    public CitySelected(Activity activity) {
        Log.d("CitySelected", "new CitySelected ");
//        this.sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE); // MODE_WORLD_READABLE MODE_PRIVATE

        // testing
        this.sharedPreferences = activity.getSharedPreferences("cityName", Activity.MODE_PRIVATE);

        Log.d("CitySelected", "new CitySelected: successful");
    }

    public String getCity() {

        Log.d("CitySelected", "setCity: city = " + sharedPreferences.getString(KEY_CITY, "kyiv,ua"));
        Log.d("CitySelected", "setCity: successful");

        return sharedPreferences.getString(KEY_CITY, "Kyiv");
    }

    public void setCity(String city) {
        sharedPreferences.edit().putString(KEY_CITY, city).commit();

        Log.d("CitySelected", "setCity: successful");
    }

    public void setCity(long lat, long lon) {
        sharedPreferences.edit().putLong(KEY_LAT, lat);
        sharedPreferences.edit().putLong(KEY_LON, lon);
    }

    public float getLat() {
        return sharedPreferences.getFloat(KEY_LAT, defLat);
    }


    public float getLon() {
        return sharedPreferences.getFloat(KEY_LON, deflon);
    }
}
