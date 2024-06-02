package com.example.lovelypets.logs;

import android.util.Log;

public class SystemPropertyUtil {
    private static final String TAG = "SystemPropertyUtil";

    public static String getSystemProperty(String key) {
        String value = "";
        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class)
                    .invoke(null, key);
        } catch (Exception e) {
            Log.e(TAG, "Failed to access system property: " + key, e);
        }
        return value;
    }
}
