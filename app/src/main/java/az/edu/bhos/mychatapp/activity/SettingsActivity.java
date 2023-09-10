package az.edu.bhos.mychatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import az.edu.bhos.mychatapp.R;
import az.edu.bhos.mychatapp.util.InternetReceiver;

public class SettingsActivity extends AppCompatActivity {

    protected static InternetReceiver internetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        internetReceiver = new InternetReceiver();
        registerReceiver(internetReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference name = findPreference("name");
            Preference location = findPreference("location");
            Preference temperature = findPreference("temperature");
            Preference networkStatus = findPreference("network_status");
            InternetReceiver.setPreference(networkStatus);
            assert temperature != null;
            temperature.setSummary(String.valueOf(getDeviceTemperature(this.requireContext())));

            assert name != null;
            name.setOnPreferenceChangeListener((preference, newValue) -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newDisplayName = (String) newValue;
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newDisplayName).build();
                assert user != null;
                user.updateProfile(request);
                return true;
            });

            assert location != null;
            location.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(requireActivity(), LocationSelectionActivity.class);
                startActivityForResult(intent, 101);
                return true;
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 101) {
                if (resultCode == RESULT_OK) {
                    String selectedCity = data.getStringExtra("selected_city");
                    SharedPreferences sp = getPreferenceManager().getSharedPreferences();
                    assert sp != null;
                    sp.edit().putString("location", selectedCity).apply();
                }
            }
        }
    }

    protected static float getDeviceTemperature(Context c) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = c.registerReceiver(null, intentFilter);
        assert batteryStatus != null;
        int temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        return temperature / 10.0f;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetReceiver);
    }
}