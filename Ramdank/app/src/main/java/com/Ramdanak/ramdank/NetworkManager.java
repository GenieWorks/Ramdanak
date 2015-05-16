package com.Ramdanak.ramdank;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Checks for network connection
 * Created by mohamed on 4/21/15.
 */
public class NetworkManager {
    private static final String url = "";
    private static final String TAG = "CLIENT";

    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo;
    private NetworkInfo mobileInfo;


    private static NetworkManager networkManager;

    /**
     * Create instance of class
     *
     * @param context application context of the main activity.
     */
    private NetworkManager(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Gets the instance of the network manager class
     *
     * @return class instance
     */
    public static NetworkManager getInstance() {
        return networkManager;
    }

    /**
     * Is mobile connected to internet
     *
     * @return true if connected to wifi, false otherwise.
     */
    public boolean isConnected() {
        return wifiInfo.isConnected() || mobileInfo.isConnected();
    }

    /**
     * Gets the Server URL.
     *
     * @return server URL
     */
    public String getServerURL() {
        return url;
    }

    /**
     * Initializes the client class
     *
     * @param context application context of the main activity.
     */
    public static void init(Context context) {
        if (networkManager == null)
            networkManager = new NetworkManager(context);
    }

    /**
     * Enable mobile wifi.
     *
     * @return true on success, false otherwise.
     */
    public boolean enableWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.setWifiEnabled(true)) {
            Log.d(TAG, "wifi enabled!");
            return true;
        } else {
            Log.d(TAG, "failed to open wifi");
            return false;
        }
    }

    /**
     * Open
     */
    public void enableMobilePackage() {
        UIController.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "please enable data roaming", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.phone", "com.android.phone.Settings");
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        });
    }
}
