package com.joulis1derful.tripscheduler.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joulis1derful.tripscheduler.R;
import com.joulis1derful.tripscheduler.model.TripInfo;

import java.util.List;

public class TripInfoAdapter extends RecyclerView.Adapter<TripInfoAdapter.TripInfoViewHolder> {
    private Context context;
    private List<TripInfo> tripInfoList;

    public TripInfoAdapter(Context context, List<TripInfo> tripInfoList) {
        this.context = context;
        this.tripInfoList = tripInfoList;
    }

    @Override
    public TripInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdListItem = R.layout.trip_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdListItem, parent, false);
        return new TripInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripInfoViewHolder holder, int position) {
        holder.bind(tripInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return tripInfoList.size();
    }

    class TripInfoViewHolder extends RecyclerView.ViewHolder {
        TextView city1;
        TextView city2;
        TextView time_from;
        TextView time_to;
        TextView price;
        TextView date_from;
        TextView date_to;
        TripInfoViewHolder(View itemView) {
            super(itemView);
            city1 = (TextView) itemView.findViewById(R.id.city1);
            city2 = (TextView) itemView.findViewById(R.id.city2);
            time_from = (TextView) itemView.findViewById(R.id.time_from);
            time_to = (TextView) itemView.findViewById(R.id.time_to);
            price = (TextView) itemView.findViewById(R.id.price);
            date_from = (TextView) itemView.findViewById(R.id.date_from);
            date_to = (TextView) itemView.findViewById(R.id.date_to);
            applyFonts(itemView);
        }

        void bind(TripInfo tripInfo) {
            city1.setText(tripInfo.getCity_from().getName());
            city2.setText(tripInfo.getCity_to().getName());
            time_from.setText(tripInfo.getTime_from());
            time_to.setText(tripInfo.getTime_to());
            price.setText(String.valueOf(tripInfo.getPrice()) + " \u20B4");
            date_from.setText(tripInfo.getDate_from());
            date_to.setText(tripInfo.getDate_to());
        }

        void applyFonts(View view) {
            Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(),
                    "fonts/EncodeSansExpanded-Medium.ttf");
            Typeface priceFont = Typeface.createFromAsset(view.getContext().getAssets(),
                    "fonts/Montserrat-Regular.ttf");
            city1.setTypeface(typeface);
            city2.setTypeface(typeface);
            time_from.setTypeface(typeface);
            time_to.setTypeface(typeface);
            date_from.setTypeface(typeface);
            date_to.setTypeface(typeface);
            price.setTypeface(priceFont);
        }
    }
}
