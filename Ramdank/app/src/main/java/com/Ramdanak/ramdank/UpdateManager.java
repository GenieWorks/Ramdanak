package com.Ramdanak.ramdank;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ***Better move these methods each for the underlining activity it will contain.
 * // TODO: copy and complete later
 * Created by mohamed on 4/21/15.
 */
public class UpdateManager {

    private class SendRating extends AsyncTask<Object, Void, Boolean> {

        /**
         * @see AsyncTask<Object, Void, Boolean>.doInBackground
         * @param params id comes first, then the rating value as double. Finally true if channel
         *               rating, false for show rating
         * @return true if successfully updated the rating, false otherwise.
         */
        @Override
        protected Boolean doInBackground(Object... params) {
            int id = (int) params[0];
            double raring = (double) params[1];
            boolean isChannelRating = (boolean) params[2];

            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(NetworkManager.getInstance().getServerURL());

                List nameValuePairs = new ArrayList<BasicNameValuePair>();

                if (isChannelRating)
                    nameValuePairs.add(new BasicNameValuePair("POST_TYPE", "R_C"));
                else
                    nameValuePairs.add(new BasicNameValuePair("POST_TYPE", "R_S"));

                nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
                nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(raring)));

                UrlEncodedFormEntity form = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                form.setContentEncoding(HTTP.UTF_8);
                httppost.setEntity(form);


                HttpResponse response = httpclient.execute(httppost);

                //Log.d(TAG, String.valueOf(response.getStatusLine().getStatusCode()));
                int status = response.getStatusLine().getStatusCode();
                if ( status == 200) {
                    //  Log.d(TAG, "Success");
                    return true;
                } else if (status == 500) {
                    //Log.e(TAG, "Internal server error");
                    return false;
                } else {
                    //Log.e(TAG, "bad argument!");
                    return  false;
                }

            } catch (Exception e) {
                //Log.e(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            // TODO: show error message to user
        }
    }

    private class CheckForUpdates extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}