package az.edu.bhos.mychatapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;

public class CheckInternet {

    public static String getNetworkInfo(Context context){
        String status = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null){
            String command = "ping -c 1 www.google.com";
            try {
                if (Runtime.getRuntime().exec(command).waitFor() == 0)
                    status = "connected";
                else
                    status = "disconnected";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            status = "disconnected";
        }

        return status;
    }
}