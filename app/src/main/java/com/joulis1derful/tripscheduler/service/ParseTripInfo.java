package com.joulis1derful.tripscheduler.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.joulis1derful.tripscheduler.util.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.joulis1derful.tripscheduler.util.DbContract.KEY_BUS_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_HIGHLIGHT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_NAME;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_HIGHLIGHT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_NAME;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_DATE_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_DATE_TO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO_TO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_PRICE;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_RESERVATION_COUNT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_TIME_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_TIME_TO;


public class ParseTripInfo extends AsyncTask<Void, Void, String> {
    public static final String URL_TO_PARSE =
            "http://projects.gmoby.org/web/index.php/api/trips?from_date=2016-01-01&to_date=2018-03-01";
    public static final String HTTP_METHOD = "GET";

    private static final String TAG = ParseTripInfo.class.getSimpleName();

    private BufferedReader reader = null;
    private String resultJson = "";

    private ProgressDialog dialog;

    private ParseResponse delegate;
    private DbHelper mDb;

    public interface ParseResponse {
        void processFinish(boolean response);
    }

    public ParseTripInfo(Activity activity, ParseResponse delegate) {
        dialog = new ProgressDialog(activity);
        this.delegate = delegate;
        this.mDb = DbHelper.getDbInstance(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Please wait...\nDownloading resources");
        dialog.setIndeterminate(true);
        dialog.show();
        Log.d(TAG, "JSON DATA IS DOWNLOADING");
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(URL_TO_PARSE);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_METHOD);
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Some input/output error has occured");
        } finally {
            closeStream(reader);
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        boolean response = false;
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(strJson);
            JSONArray trips = dataJson.getJSONArray("data");

            for(int i = 0; i < trips.length(); i++) {
                JSONObject trip = trips.getJSONObject(i);
                if(trip != null)
                    response = true;
                JSONObject from_city = trip.getJSONObject("from_city");
                JSONObject to_city = trip.getJSONObject("to_city");
                int tripId = trip.getInt("id");
                int city1Highlight = from_city.getInt("highlight");
                int city1Id = from_city.getInt("id");
                String city1Name = from_city.getString("name");
                int city2Highlight = to_city.getInt("highlight");
                int city2Id = to_city.getInt("id");
                String city2Name = to_city.getString("name");
                String from_date = trip.getString("from_date");
                String to_date = trip.getString("to_date");
                String from_time = trip.getString("from_time");
                String to_time = trip.getString("to_time");
                String from_info = trip.getString("from_info");
                String to_info  = trip.getString("to_info");
                String info = trip.getString("info");
                int price = trip.getInt("price");
                int bus_id = trip.getInt("bus_id");
                int reservation_count = trip.getInt("reservation_count");

                ContentValues cv = new ContentValues();
                cv.put(KEY_ID, tripId);
                cv.put(KEY_CITY1_ID, city1Id);
                cv.put(KEY_CITY1_HIGHLIGHT, city1Highlight);
                cv.put(KEY_CITY1_NAME, city1Name);
                cv.put(KEY_CITY2_ID, city2Id);
                cv.put(KEY_CITY2_HIGHLIGHT, city2Highlight);
                cv.put(KEY_CITY2_NAME, city2Name);
                cv.put(KEY_DATE_FROM, from_date);
                cv.put(KEY_DATE_TO, to_date);
                cv.put(KEY_TIME_FROM, from_time);
                cv.put(KEY_TIME_TO, to_time);
                cv.put(KEY_INFO_FROM, from_info);
                cv.put(KEY_INFO_TO, to_info);
                cv.put(KEY_INFO, info);
                cv.put(KEY_BUS_ID, bus_id);
                cv.put(KEY_PRICE, price);
                cv.put(KEY_RESERVATION_COUNT, reservation_count);

                mDb.writeIntoDb(cv);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON error has occured while parsing the data");
        }
        delegate.processFinish(response);
        dialog.cancel();
    }

    private void closeStream(Closeable s){
        try {
            if(s!=null) {
                s.close();
            }
        } catch(IOException e){
            Log.e(TAG, "Error occured while reading");
        }
    }
}
