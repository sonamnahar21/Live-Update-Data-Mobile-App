package com.example.sonam.glookoapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReadingsAdapter extends RecyclerView.Adapter<ReadingsAdapter.MyViewHolder> {
    private List<Readings> readingsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bg_value, meal, timeStamp;

        public MyViewHolder(View view) {
            super(view);
            bg_value = (TextView) view.findViewById(R.id.bg_value);
            timeStamp = (TextView) view.findViewById(R.id.timeStamp);
            meal = (TextView) view.findViewById(R.id.meal);
        }
    }


    public ReadingsAdapter(List<Readings> readingsList) {
        this.readingsList = readingsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reading_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            Readings reading = readingsList.get(position);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = formatter.parse(reading.getTimeStamp());
            System.out.println(formatter.format(date));

            holder.bg_value.setText(reading.getBg_value());
            holder.meal.setText(reading.getMeal());
            holder.timeStamp.setText(outputFormat.format(date));
        }
        catch (Exception e)
        {
            Log.d("Error",e.getMessage());

        }






    }

    @Override
    public int getItemCount() {
        return  readingsList.size();
    }
}
