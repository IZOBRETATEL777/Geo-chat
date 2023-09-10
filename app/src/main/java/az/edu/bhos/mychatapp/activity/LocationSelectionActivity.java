package az.edu.bhos.mychatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;

import az.edu.bhos.mychatapp.BuildConfig;
import az.edu.bhos.mychatapp.R;

public class LocationSelectionActivity extends AppCompatActivity {

    private final String TOKEN = BuildConfig.GOOGLE_API_KEY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        Places.initialize(getApplicationContext(), TOKEN);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("CitySelectionActivity", status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(Place place) {
                String selectedCity = place.getName();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_city", selectedCity);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String selectedCity = place.getName();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selected_city", selectedCity);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                if (data != null) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.d("CitySelectionActivity", status.getStatusMessage());
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("CitySelectionActivity", "The user canceled the operation");
            }
        }
    }
}
