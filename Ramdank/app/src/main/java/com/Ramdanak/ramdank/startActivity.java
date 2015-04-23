package com.Ramdanak.ramdank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
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
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ApplicationInitializer initializer = new ApplicationInitializer();
        initializer.execute();
    }

    /**
     * Initialize the program database and network manager.
     */
    private class ApplicationInitializer extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            if (TvScheduleDbHelper.createInstance(getApplicationContext()) == null) {
                Log.d("START", "configuration failed!");
                return false;
            }

            NetworkManager.init(getApplicationContext());
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar mSpinner = (ProgressBar) findViewById(R.id.Splash_ProgressBar);
            mSpinner.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            // proceed to app if everything was initialized correctly
            if (aBoolean)
                startActivity(new Intent(startActivity.this, Main.class));

            finish();
        }
    }
}