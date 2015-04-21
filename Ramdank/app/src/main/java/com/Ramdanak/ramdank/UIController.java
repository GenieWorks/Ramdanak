package com.Ramdanak.ramdank;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by root on 4/21/15.
 */
public class UIController {
    public static void runOnUiThread(Runnable runnable){
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler .post(runnable);
    }
}
