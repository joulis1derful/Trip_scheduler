package com.joulis1derful.tripscheduler.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.joulis1derful.tripscheduler.R;
import com.joulis1derful.tripscheduler.adapter.TripInfoAdapter;
import com.joulis1derful.tripscheduler.model.TripInfo;
import com.joulis1derful.tripscheduler.service.ParseTripInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TripInfoAdapter mAdapter;
    private List<TripInfo> trips;

    private List<String> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseData();

        refreshUI();

    }

    private void refreshUI() {
        trips = new ArrayList<>();
        TripInfo.City city1 = new TripInfo.City(1, 2, "Kiev");
        TripInfo.City city2 = new TripInfo.City(2, 1, "Chernigov");

        TripInfo trip1 = new TripInfo(city1, city2, "15.08.2017", "16.08.2017", "15:00", "01:00",
                "Da", "Net", "lol", 250, 15, 0);
        TripInfo trip2 = new TripInfo(city2, city1, "15.08.2017", "16.08.2017", "15:00", "01:00",
                "Da", "Net", "lol", 250, 15, 0);
        trips.add(trip1);
        trips.add(trip2);
        TextView emptyList = (TextView) findViewById(R.id.list_is_empty);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new TripInfoAdapter(this, trips);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void parseData() {
        result = new ArrayList<>();
        new ParseTripInfo(new ParseTripInfo.ParseResponse() {
            @Override
            public void processFinish(List<String> results) {
                result.addAll(results);
//                for(String s : result) {
//                    Log.d("RESULTS", s);
//                }
            }
        }).execute();
    }

}
