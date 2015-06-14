package com.Ramdanak.ramdank;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Checks for network connection
 * Created by mohamed on 4/21/15.
 */
public abstract class NetworkManager {

    /**
     * Initializes the client class
     *
     * @param context application context of the main activity.
     */
    public static boolean checkInternetOpened(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()
        ||
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isRoaming();
    }
}
