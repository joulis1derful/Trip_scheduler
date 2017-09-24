package com.joulis1derful.tripscheduler.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.joulis1derful.tripscheduler.R;
import com.joulis1derful.tripscheduler.model.TripInfo;

public class DetailsActivity extends AppCompatActivity {
    private TextView city1;
    private TextView city2;
    private TextView city1_id;
    private TextView city2_id;
    private TextView time_from_placeholder;
    private TextView time_from;
    private TextView time_to_placeholder;
    private TextView time_to;
    private TextView date_from_placeholder;
    private TextView date_from;
    private TextView date_to_placeholder;
    private TextView date_to;
    private TextView info_from;
    private TextView info_to;
    private TextView cities_info;
    private TextView price;
    private TextView bus_id;
    private TextView reservation_count;
    private TextView additional_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        changeTitleBarColor();
        setContentView(R.layout.activity_details);

        initUI();
    }

    private void changeTitleBarColor() {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(getString(R.color.red)));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    private void hideStatusBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
    }

    private void initUI() {
        city1 = (TextView) findViewById(R.id.city1_name_details);
        city2 = (TextView) findViewById(R.id.city2_name_details);
        city1_id = (TextView) findViewById(R.id.city1_id);
        city2_id = (TextView) findViewById(R.id.city2_id);
        time_from_placeholder = (TextView) findViewById(R.id.time_from_placeholder);
        time_from = (TextView) findViewById(R.id.time_from_details);
        time_to_placeholder = (TextView) findViewById(R.id.time_to_placeholder);
        time_to = (TextView) findViewById(R.id.time_to_details);
        date_from_placeholder = (TextView) findViewById(R.id.date_from_placeholder);
        date_from = (TextView) findViewById(R.id.date_from_details);
        date_to_placeholder = (TextView) findViewById(R.id.date_to_placeholder);
        date_to = (TextView) findViewById(R.id.date_to_details);
        info_from = (TextView) findViewById(R.id.info_from_details);
        info_to = (TextView) findViewById(R.id.info_to_details);
        cities_info = (TextView) findViewById(R.id.cities_info_details);
        price = (TextView) findViewById(R.id.price_details);
        bus_id = (TextView) findViewById(R.id.bus_id_details);
        reservation_count = (TextView) findViewById(R.id.reservation_count_details);
        additional_info = (TextView) findViewById(R.id.additional_info);

        TripInfo trip = (TripInfo) getIntent().getSerializableExtra("data_trip");
        city1.setText(trip.getCity_from().getName());
        city2.setText(trip.getCity_to().getName());
        city1_id.setText("id: " + trip.getCity_from().getId());
        city2_id.setText("id: " + trip.getCity_to().getId());
        cities_info.setText("Полный маршрут: " + trip.getInfo());
        time_from_placeholder.setText("Время отъезда:");
        time_from.setText(trip.getTime_from());
        time_to_placeholder.setText("Время прибытия:");
        time_to.setText(trip.getTime_to());
        date_from_placeholder.setText("Дата отъезда:");
        date_from.setText(trip.getDate_from());
        date_to_placeholder.setText("Дата прибытия:");
        date_to.setText(trip.getDate_to());
        additional_info.setText("Дополнительная информация:");
        info_from.setText(trip.getInfo_from());
        info_to.setText(trip.getInfo_to());
        price.setText("Цена билета: " + String.valueOf(trip.getPrice()) + " \u20B4");
        bus_id.setText("Автобус №" + String.valueOf(trip.getBus_id()));
        reservation_count.setText("Зарезервировано: " + String.valueOf(trip.getReservation_count()));
        applyFonts();

    }

    private void applyFonts() {
        Typeface ralewayBold = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        Typeface ralewayExtraBold = Typeface.createFromAsset(getAssets(), "fonts/Raleway-ExtraBold.ttf");
        Typeface ralewayRegular = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface montserratExtraBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ExtraBold.ttf");
        Typeface montserratItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Italic.ttf");
        Typeface encodeSansExpandedMedium = Typeface.createFromAsset(getAssets(), "fonts/EncodeSansExpanded-Medium.ttf");
        city1.setTypeface(ralewayExtraBold);
        city2.setTypeface(ralewayExtraBold);
        bus_id.setTypeface(ralewayBold);
        cities_info.setTypeface(ralewayBold);
        city1_id.setTypeface(encodeSansExpandedMedium);
        city2_id.setTypeface(encodeSansExpandedMedium);
        date_from.setTypeface(ralewayRegular);
        date_to.setTypeface(ralewayRegular);
        time_to.setTypeface(ralewayRegular);
        time_from.setTypeface(ralewayRegular);
        date_from_placeholder.setTypeface(montserratExtraBold);
        date_to_placeholder.setTypeface(montserratExtraBold);
        time_from_placeholder.setTypeface(montserratExtraBold);
        time_to_placeholder.setTypeface(montserratExtraBold);
        additional_info.setTypeface(montserratExtraBold);
        info_from.setTypeface(montserratItalic);
        info_to.setTypeface(montserratItalic);
    }

}