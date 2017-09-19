package com.joulis1derful.tripscheduler.service;

import android.os.AsyncTask;
import android.util.Log;

import com.joulis1derful.tripscheduler.model.TripInfo;

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

    private List<TripInfo> tripsList;

    public ParseResponse delegate = null;

    public interface ParseResponse {
        void processFinish(List<TripInfo> results);
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

                TripInfo.City objCity1 = new TripInfo.City(city1Id, city1Highlight, city1Name);
                TripInfo.City objCity2 = new TripInfo.City(city2Id, city2Highlight, city2Name);
                TripInfo objTrip = new TripInfo(tripId, objCity1, objCity2, from_date, to_date, from_time,
                        to_time, from_info, to_info, info, price, bus_id, reservation_count);
                tripsList.add(objTrip);
             //   Log.d("JSON", trip.getString("id"));
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
