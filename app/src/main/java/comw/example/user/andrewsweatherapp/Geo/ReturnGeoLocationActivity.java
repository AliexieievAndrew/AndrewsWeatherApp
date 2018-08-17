package comw.example.user.andrewsweatherapp.Geo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import comw.example.user.andrewsweatherapp.R;

public class ReturnGeoLocationActivity extends AppCompatActivity {

    private final static String GEO_LOG = "geoLogs";

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_geo_location);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragmentt);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                sendCity(getCity());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });
    }
    private void sendCity(String city) {
        Intent intent = new Intent();
        intent.putExtra("cityName", city);
        setResult(RESULT_OK,intent);
        finish();
    }

    private String getCity() {
        Geocoder geocoder = new Geocoder(ReturnGeoLocationActivity.this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cityName = addresses.get(0).getLocality();
        String countryCode = addresses.get(0).getCountryCode();

        Log.d(GEO_LOG, "getCity(): return  " + cityName + "," + countryCode);

        return cityName + "," + countryCode;
    }
}