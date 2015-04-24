package com.Ramdanak.ramdank;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Checks for network connection
 * Created by mohamed on 4/21/15.
 */
public class NetworkManager {
    private static final String url = "";

    private static final String TAG = "CLIENT";

    private boolean connected = false;

    private Context appContext;

    private static NetworkManager networkManager;


    private NetworkManager(Context context) {
        appContext = context;

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // check if you are connected or not
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "not connected to internet!");
            connected = true;
        } else {
            Log.d(TAG, "connected to internet!");
            connected = false;
        }
    }

    public static NetworkManager getInstance() {
        return networkManager;
    }
    /**
     * Is mobile connected to internet
     * @return
     */
    public boolean isConnected() {
        assert connected == true;
        return connected;
    }

    /**
     * Gets the Server URL.
     * @return server URL
     */
    public String getServerURL() {
        return url;
    }

    /**
     * Initializes the client class
     * @param context application context of the main activity.
     */
    public static void init(Context context) {
        if (networkManager == null)
            networkManager = new NetworkManager(context);
    }

    /**
     * Enable mobile wifi.
     * @return true on success, false otherwise.
     */
    public boolean enableWifi() {
        WifiManager wifiManager = (WifiManager)appContext.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.setWifiEnabled(true)) {
            Log.d(TAG, "wifi enabled!");
            return true;
        } else {
            Log.d(TAG, "failed to open wifi");
            return false;
        }

    }
}