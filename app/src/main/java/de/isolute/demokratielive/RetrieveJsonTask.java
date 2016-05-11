package de.isolute.demokratielive;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by aprang on 04.05.16.
 */
class RetrieveJsonTask extends AsyncTask<String, Void, JSONObject> {

    private Exception exception;

    protected JSONObject doInBackground(String... urls) {

        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpPost httppost = new HttpPost(urls[0]);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            return null;
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e) {
            return null;
        }

        // Convert string to object
        try {
            jsonObject = new JSONObject(result);
        } catch(JSONException e) {
            return null;
        }

        return jsonObject;
    }

    protected void onPostExecute(JSONObject jsonObject) {
        EventsModel.getInstance().stations = jsonObject;
        MainActivity mainActivity = EventsModel.getInstance().mainActivity;
        mainActivity.updateViewPager();

        ViewPager mViewPager = (ViewPager) mainActivity.findViewById(R.id.container);
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}