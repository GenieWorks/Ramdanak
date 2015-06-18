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
import android.widget.TextView;
import android.widget.Toast;

import com.Ramdanak.ramdank.DbHelper.TvScheduleDbHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Start Activity of the app
 */
public class startActivity extends Activity {
    private static final String TAG = Application.APPTAG + "start";
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
                    Log.d(TAG, "configuration failed!");
                    return false;
                }

                // see issue #14
                ParseQuery<ParseObject> query = ParseQuery.getQuery("GoogleTurnAround");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            for(ParseObject object : list) {
                                if (object.getBoolean("turn_around")) {
                                    Log.d(TAG, "turnaround");
                                    Application.setTurnAround();
                                }
                            }
                        }
                    }
                });

            } catch (InstantiationError e) {
                Log.e(TAG, "ramadank failed", e);
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
