package de.isolute.demokratielive;

/**
 * Created by aprang on 07.05.16.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class HeadRequest extends AsyncTask<String, Void, String> {

    public Button button;
    public String stationKey;

    @Override
    protected String doInBackground(String... params) {
        if (stationKey.equals(""))
            return "";

        if (stationKey.equals("Marzahn-Hellersdorf")) {
            stationKey = "MH";
        }
        URI uri = URI.create("http://stream.demokratielive.org/HLS/" + stationKey + "/livestream.m3u8");

        HttpClient httpClient = new DefaultHttpClient();
        HttpHead httphead = new HttpHead(params[0]);
        httphead.setURI(uri);

        try {
            HttpResponse response = httpClient.execute(httphead);

            return response.getFirstHeader("Date").getValue();
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String dateString) {

        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

        try {
            Date headerDate = (Date)formatter.parse(dateString);
            long headerUnixTime = headerDate.getTime() / 1000L;
            long currentUnixTime = System.currentTimeMillis() / 1000L;
            Date currentDate = new Timestamp(System.currentTimeMillis() - Calendar.getInstance().getTimeZone().getOffset(System.currentTimeMillis()));

            System.out.println("Now: " + currentDate.toString() + " - Header: " + headerDate.toString());

            if (headerUnixTime + 60000L > currentUnixTime) {
                button.setEnabled(true);
                button.setText("Livestream Starten");
            }

        } catch (ParseException e) {

            button.setEnabled(false);
            button.setText("Aktuell kein Livestream");
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}

