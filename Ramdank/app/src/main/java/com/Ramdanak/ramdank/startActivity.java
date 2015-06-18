package com.Ramdanak.ramdank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;

/**
 * Start Activity of the app
 */
public class startActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        this.context = context;
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: this should be moved to Application.onCreate
        ApplicationInitializer initializer = new ApplicationInitializer();
        initializer.execute();
    }

    /**
     * Initialize the program database and network manager.
     */
    private class ApplicationInitializer extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (TvScheduleDbHelper.createInstance(getApplicationContext()) == null) {
                    Log.d("START", "configuration failed!");
                    return false;
                }

                Log.d("START", "first runs!");
                if (Application.isFirstRun()) {
                    UIController.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "downloading content", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (NetworkManager.checkInternetOpened(context)) {
                        Application.setFirstRun();

                        UpdatesCrawler crawler = new UpdatesCrawler(context);
                        crawler.getLatestUpdates();

                        while (!crawler.isDone()) {
                            /*try {
                                Thread.sleep(100, 40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                        }

                        if (crawler.isBad()) {
                            UIController.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "retry later", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        UIController.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(context, "need internet connection", Toast.LENGTH_LONG).show();
                            }
                        });
                        return false;
                    }

                }
            } catch (InstantiationError e) {
                //Log.e(TAG, e.getMessage());
                Toast.makeText(getApplicationContext(), "Ramdanak failed to start! please contact the developers for details"
                , Toast.LENGTH_LONG).show();
                return false;
            }

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
            if (aBoolean) {
                startActivity(new Intent(startActivity.this, Main.class));
            }

            finish();
        }
    }
}
