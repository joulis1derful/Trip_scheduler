package com.joulis1derful.tripscheduler.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joulis1derful.tripscheduler.R;
import com.joulis1derful.tripscheduler.adapter.TripInfoAdapter;
import com.joulis1derful.tripscheduler.model.TripInfo;
import com.joulis1derful.tripscheduler.service.ParseTripInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView emptyList;
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;

    private TripInfoAdapter mAdapter;
    private List<TripInfo> trips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        emptyList = (TextView) findViewById(R.id.list_is_empty);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        showProgressBar(true);

        parseData();
//        refreshUI();

    }

    private void refreshUI() {
//        trips = new ArrayList<>();
//        TripInfo.City city1 = new TripInfo.City(1, 2, "Kiev");
//        TripInfo.City city2 = new TripInfo.City(2, 1, "Chernigov");
//
//        TripInfo trip1 = new TripInfo(1, city1, city2, "15.08.2017", "16.08.2017", "15:00", "01:00",
//                "Da", "Net", "lol", 250, 15, 0);
//        TripInfo trip2 = new TripInfo(2, city2, city1, "15.08.2017", "16.08.2017", "15:00", "01:00",
//                "Da", "Net", "lol", 250, 15, 0);
//        trips.add(trip1);
//        trips.add(trip2)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mAdapter = new TripInfoAdapter(this, trips);
        mRecycler.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        showProgressBar(false);

    }

    private void checkParseResults() {
        if(trips.isEmpty()) {
            mRecycler.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

    private void parseData() {
        trips = new ArrayList<>();
        ParseTripInfo parsingObj = new ParseTripInfo(new ParseTripInfo.ParseResponse() {
            @Override
            public void processFinish(List<TripInfo> results) {
                //  showProgressBar(true);
                trips.addAll(results);
                refreshUI();
                checkParseResults();
            }
        });
        parsingObj.execute();
    }

    private void showProgressBar(boolean state) {
        if(state) {
            mRecycler.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecycler.setVisibility(View.VISIBLE);
        }
    }

}
