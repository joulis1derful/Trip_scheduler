package com.joulis1derful.tripscheduler.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParseTripInfo extends AsyncTask<Void, Void, String> {
    public static final String URL_TO_PARSE =
            "http://projects.gmoby.org/web/index.php/api/trips?from_date=2016-01-01&to_date=2018-03-01";
    public static final String HTTP_METHOD = "GET";
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String resultJson = "";

    private List<String> tripsList;

    public ParseResponse delegate = null;

    public interface ParseResponse {
        void processFinish(List<String> results);
    }

    public ParseTripInfo(ParseResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("START PARSING", "JSON DATA IS DOWNLOADING");
    }

    @Override
    protected String doInBackground(Void... params) {
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
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        Log.d("JSON STRING", strJson);

        tripsList = new ArrayList<>();
        JSONObject dataJson = null;
        try {
            dataJson = new JSONObject(strJson);
            JSONArray trips = dataJson.getJSONArray("data");

            for(int i = 0; i < trips.length(); i++) {
                JSONObject trip = trips.getJSONObject(i);
                tripsList.add(trip.getString("from_city"));

           //     Log.d("JSON", trip.getString("from_city"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        delegate.processFinish(tripsList);
    }

    private void closeStream(Closeable s){
        try {
            if(s!=null) {
                s.close();
            }
        } catch(IOException e){
            Log.d("BufferedReader error", "Error occured while reading");
        }
    }
}
