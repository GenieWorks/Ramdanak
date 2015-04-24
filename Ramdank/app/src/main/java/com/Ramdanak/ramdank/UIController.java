package com.Ramdanak.ramdank;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by root on 4/21/15.
 */
public class UIController {
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable){
        uiHandler .post(runnable);
    }
}