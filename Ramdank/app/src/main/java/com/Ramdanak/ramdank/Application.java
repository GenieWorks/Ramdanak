package com.Ramdanak.ramdank;

import com.instabug.library.Instabug;

/**
 *
 * Created by Mohamed on 6/8/2015.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Instabug.initialize(this, "55e18901e18c3baaa8cb9b937925ea20");
    }
}
