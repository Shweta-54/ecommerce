package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;

public class ActivityUtils {

    @SuppressLint("ObsoleteSdkInt")
    public static void applyTransition(Activity activity) {
        // Apply transition only for older Android versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Use the deprecated method for versions below Android M (API 23)
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
        }
        // For newer versions, you can either avoid transition or use newer options
        // Alternatively, you can use ActivityOptions to create custom transitions
    }
}