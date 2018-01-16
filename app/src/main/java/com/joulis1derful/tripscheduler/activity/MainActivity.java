package com.joulis1derful.tripscheduler.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joulis1derful.tripscheduler.R;
import com.joulis1derful.tripscheduler.adapter.OnItemClickAdapter;
import com.joulis1derful.tripscheduler.adapter.TripInfoAdapter;
import com.joulis1derful.tripscheduler.model.TripInfo;
import com.joulis1derful.tripscheduler.service.ParseTripInfo;
import com.joulis1derful.tripscheduler.util.DbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.os.AsyncTask.Status.RUNNING;
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

public class MainActivity extends AppCompatActivity {
    public static final int POSITION_OFFSET_FOR_DB = 501;

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView emptyList;
    private RecyclerView mRecycler;
    private Button retryButton;

    private TripInfoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TripInfo> tripsList;

    private DbHelper mDb;
    private Cursor mCursor;

    private ParseTripInfo pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTitleBar();
        setContentView(R.layout.activity_main);
        mDb = DbHelper.getDbInstance(this);
        initUI();
        showFromDb();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pt != null && pt.getStatus() == RUNNING) {
            pt.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pt != null && pt.getStatus() == RUNNING) {
            pt.cancel(true);
        }
    }

    private void initUI() {
        emptyList = (TextView) findViewById(R.id.list_is_empty);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        retryButton = (Button) findViewById(R.id.btn_retry);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.addOnItemTouchListener(new OnItemClickAdapter(this, new OnItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCursor = mDb.getDataById(position + POSITION_OFFSET_FOR_DB);
                if (mCursor.moveToNext()) {
                    int tripId = mCursor.getColumnIndex(KEY_ID);
                    int city1Highlight = mCursor.getColumnIndex(KEY_CITY1_HIGHLIGHT);
                    int city1Id = mCursor.getColumnIndex(KEY_CITY1_ID);
                    int city1Name = mCursor.getColumnIndex(KEY_CITY1_NAME);
                    int city2Highlight = mCursor.getColumnIndex(KEY_CITY2_HIGHLIGHT);
                    int city2Id = mCursor.getColumnIndex(KEY_CITY2_ID);
                    int city2Name = mCursor.getColumnIndex(KEY_CITY2_NAME);
                    int from_date = mCursor.getColumnIndex(KEY_DATE_FROM);
                    int to_date = mCursor.getColumnIndex(KEY_DATE_TO);
                    int from_time = mCursor.getColumnIndex(KEY_TIME_FROM);
                    int to_time = mCursor.getColumnIndex(KEY_TIME_TO);
                    int from_info = mCursor.getColumnIndex(KEY_INFO_FROM);
                    int to_info = mCursor.getColumnIndex(KEY_INFO_TO);
                    int info = mCursor.getColumnIndex(KEY_INFO);
                    int price = mCursor.getColumnIndex(KEY_PRICE);
                    int bus_id = mCursor.getColumnIndex(KEY_BUS_ID);
                    int reservation_count = mCursor.getColumnIndex(KEY_RESERVATION_COUNT);

                    TripInfo.City city1 = new TripInfo.City(mCursor.getInt(city1Id),
                            mCursor.getInt(city1Highlight), mCursor.getString(city1Name));
                    TripInfo.City city2 = new TripInfo.City(mCursor.getInt(city2Id),
                            mCursor.getInt(city2Highlight), mCursor.getString(city2Name));
                    TripInfo trip = new TripInfo(mCursor.getInt(tripId), city1, city2, mCursor.getString(from_date),
                            mCursor.getString(to_date), mCursor.getString(from_time), mCursor.getString(to_time),
                            mCursor.getString(from_info), mCursor.getString(to_info), mCursor.getString(info),
                            mCursor.getInt(price), mCursor.getInt(bus_id), mCursor.getInt(reservation_count));
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("data_trip", trip);
                    startActivity(intent);
                }
            }
        }));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFromDb();
            }
        });
    }

    private void hideTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
    }

    private void refreshUI() {
        mAdapter = new TripInfoAdapter(this, tripsList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void showEmptyList() {
            mRecycler.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
    }

    private void showFromDb() {
        if (mDb.getAllData().getCount() == 0) {
            parseData();
        } else {
            tripsList = new ArrayList<>();
            mCursor = mDb.getAllData();
            if (mCursor.moveToNext()) {
                int tripId = mCursor.getColumnIndex(KEY_ID);
                int city1Highlight = mCursor.getColumnIndex(KEY_CITY1_HIGHLIGHT);
                int city1Id = mCursor.getColumnIndex(KEY_CITY1_ID);
                int city1Name = mCursor.getColumnIndex(KEY_CITY1_NAME);
                int city2Highlight = mCursor.getColumnIndex(KEY_CITY2_HIGHLIGHT);
                int city2Id = mCursor.getColumnIndex(KEY_CITY2_ID);
                int city2Name = mCursor.getColumnIndex(KEY_CITY2_NAME);
                int from_date = mCursor.getColumnIndex(KEY_DATE_FROM);
                int to_date = mCursor.getColumnIndex(KEY_DATE_TO);
                int from_time = mCursor.getColumnIndex(KEY_TIME_FROM);
                int to_time = mCursor.getColumnIndex(KEY_TIME_TO);
                int from_info = mCursor.getColumnIndex(KEY_INFO_FROM);
                int to_info = mCursor.getColumnIndex(KEY_INFO_TO);
                int info = mCursor.getColumnIndex(KEY_INFO);
                int price = mCursor.getColumnIndex(KEY_PRICE);
                int bus_id = mCursor.getColumnIndex(KEY_BUS_ID);
                int reservation_count = mCursor.getColumnIndex(KEY_RESERVATION_COUNT);
                do {
                    TripInfo.City city1 = new TripInfo.City(mCursor.getInt(city1Id),
                            mCursor.getInt(city1Highlight), mCursor.getString(city1Name));
                    TripInfo.City city2 = new TripInfo.City(mCursor.getInt(city2Id),
                            mCursor.getInt(city2Highlight), mCursor.getString(city2Name));
                    TripInfo trip = new TripInfo(mCursor.getInt(tripId), city1, city2, mCursor.getString(from_date),
                            mCursor.getString(to_date), mCursor.getString(from_time), mCursor.getString(to_time),
                            mCursor.getString(from_info), mCursor.getString(to_info), mCursor.getString(info),
                            mCursor.getInt(price), mCursor.getInt(bus_id), mCursor.getInt(reservation_count));
                    tripsList.add(trip);
                } while (mCursor.moveToNext());
            }
            mCursor.close();
            refreshUI();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void parseData() {
        if (isOnline()) {
            showRetryButton(false);
            pt = new ParseTripInfo(this, new ParseTripInfo.ParseResponse() {
               @Override
               public void processFinish(boolean response) {
                   if(!response) {
                       showEmptyList();
                       Toast.makeText(MainActivity.this, "There is nothing to parse",
                               Toast.LENGTH_LONG).show();
                   } else {
                       showFromDb();
                   }
               }
           });
            pt.execute();
        } else {
            retry();
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            Log.e(TAG, "Input/output error has occured");
        } catch (InterruptedException e) {
            Log.e(TAG, "Thread error has occured");
        }

        return false;
    }

    private void retry() {
        showRetryButton(true);
        Toast.makeText(this, "Can't download the data.\nCheck Internet connection and try again",
                Toast.LENGTH_SHORT).show();
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseData();
            }
        });
    }

    private void showRetryButton(boolean state) {
        if (state) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mRecycler.setVisibility(View.GONE);
            emptyList.setVisibility(View.GONE);
            retryButton.setVisibility(View.VISIBLE);
        } else {
            retryButton.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }
}
