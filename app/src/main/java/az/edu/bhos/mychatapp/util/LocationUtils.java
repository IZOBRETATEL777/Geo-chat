package az.edu.bhos.mychatapp.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static String getCityNameFromCoordinates(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String cityName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                cityName = address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }
}
