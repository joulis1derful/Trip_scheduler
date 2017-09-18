package com.joulis1derful.tripscheduler.adapter;

import android.content.Context;
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
        TextView date;
        TripInfoViewHolder(View itemView) {
            super(itemView);
            city1 = (TextView) itemView.findViewById(R.id.city1);
            city2 = (TextView) itemView.findViewById(R.id.city2);
            time_from = (TextView) itemView.findViewById(R.id.time_from);
            time_to = (TextView) itemView.findViewById(R.id.time_to);
            price = (TextView) itemView.findViewById(R.id.price);
            date = (TextView) itemView.findViewById(R.id.date);
        }

        void bind(TripInfo tripInfo) {
            city1.setText(tripInfo.getCity_from().getName());
            city2.setText(tripInfo.getCity_to().getName());
            time_from.setText(tripInfo.getTime_from());
            time_to.setText(tripInfo.getTime_to());
            price.setText(String.valueOf(tripInfo.getPrice()));
            date.setText(tripInfo.getDate_from());
        }
    }
}
