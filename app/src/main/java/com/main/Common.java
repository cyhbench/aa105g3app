package com.main;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by cuser on 2016/12/14.
 */

public class Common {
    //    public static String URL = "http://192.168.196.189:8080/Spot_MySQL_Web/";
    public static String URL = "http://10.0.2.2:8081/AA105G3/";
//    public static String URL = "http://192.168.42.190:8081/AA105G3/";
//    public static String URL = "http://35.163.5.128:8081/AA105G3/";

//    public static String URL = "http://10.120.26.11:8081/AA105G3/";

    public final static String PREF_FILE = "preference";

    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
