package com.Ramdanak.ramdank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.Ramdanak.ramdank.model.TvChannel;
import com.Ramdanak.ramdank.model.TvRecord;
import com.Ramdanak.ramdank.model.TvShow;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Start Activity of the app
 */
public class startActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread dbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (TvScheduleDbHelper.createInstance(getApplicationContext()) == null) {
                    Log.d("START", "configuration failed!");
                    finish();
                }


            }
        });

        dbThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            startActivity(new Intent(startActivity.this, Main.class));
            finish();
        }

    }
}
