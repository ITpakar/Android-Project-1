package com.tagcash.waalah.util;

import android.net.ConnectivityManager;

public class NetworkConnectionUtil {
    private static ConnectivityManager connectivityManager;

    public static void initializeNetworkConnectionUtil(ConnectivityManager _connectivityManager) {
        connectivityManager = _connectivityManager;
    }

    public static boolean isOnline() {
        return (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected());
    }
}
