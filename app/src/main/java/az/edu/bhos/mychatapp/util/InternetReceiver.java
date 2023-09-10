package az.edu.bhos.mychatapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.preference.Preference;

public class InternetReceiver extends BroadcastReceiver {
    private static Preference preference = null;

    public static void setPreference(Preference preference) {
        InternetReceiver.preference = preference;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if (status.equals("connected") && preference != null ) {
                preference.setSummary("Connected");
        }
        else if (status.equals("disconnected") && preference != null)
                preference.setSummary("Disconnected");
        }
}
