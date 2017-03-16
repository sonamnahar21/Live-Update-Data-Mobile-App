package com.example.sonam.glookoapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spyhunter99.supertooltips.ToolTip;
import com.spyhunter99.supertooltips.ToolTipManager;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Readings> readingsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReadingsAdapter mAdapter;
    ToolTipManager tooltips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtDate= (TextView) this.findViewById(R.id.txtStickyHeader);
        tooltips = new ToolTipManager(this);
        setSupportActionBar(toolbar);
        prepareData();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ReadingsAdapter(readingsList,txtDate,recyclerView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            // added tooltip code for item clicked event
            @Override
            public void onClick(View view, int position) {
                Readings readings = readingsList.get(position);
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    Date date = formatter.parse(readings.getTimeStamp());
                    System.out.println(formatter.format(date));

                    ToolTip toolTip = new ToolTip()
                            .withText("BG Value: "+ readings.getBg_value()+"\n"+"Meal: "+readings.getMeal()+"\nTimeStamp: "+outputFormat.format(date))
                            .withTextColor(Color.WHITE)
                            .withColor(R.color.black)
                            .withAnimationType(ToolTip.AnimationType.FROM_MASTER_VIEW)
                            .withShadow();
                    tooltips.showToolTip(toolTip, view);
                }
                catch (Exception e)
                {
                    Log.d("Exception",e.getMessage());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // OnScrollListener for recycler view
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // get the first and last position from layout manager
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                Log.d("First item",firstVisiblePosition+"");
                Log.d("last item",layoutManager.findLastVisibleItemPosition()+"");
                getItem(firstVisiblePosition,layoutManager.findLastVisibleItemPosition());
                if(layoutManager.findLastVisibleItemPosition()==readingsList.size()-1)
                {
                    Log.d("end of list",layoutManager.findLastVisibleItemPosition() +"  "+(readingsList.size()-1));
                    Toast.makeText(getApplicationContext(), R.string.EndOfTheList, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        tooltips.onDestroy();
        tooltips = null;
    }
    // to show the date on sticky header
    public void getItem(int startPosition,int lastPosition) {
        Log.d("data",readingsList.get(startPosition).getTimeStamp()+"-"+readingsList.get(lastPosition).getTimeStamp());
        TextView txtDate= (TextView) this.findViewById(R.id.txtStickyHeader);

        String formattedLabel= getFormattedLabel(readingsList.get(startPosition).getTimeStamp(), readingsList.get(lastPosition).getTimeStamp());
        txtDate.setText(formattedLabel);
    }
    // to read data from json string and add data to list
    private void prepareData() {

        String json= "[{ bg_value: 150, timeStamp: '2015-04-28T11:15:34', meal: 'before_meal'}, {bg_value: 90, timeStamp: ' 2015-04-16T11:15:34'}, {bg_value: 250, timeStamp:'2015-04-17T11:15:34', meal: 'after_meal'},{ bg_value: 150, timeStamp: '2015-04-18T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-19T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-20T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-21T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-22T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-23T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-24T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-25T11:15:34', meal: 'before_meal'},{ bg_value: 150, timeStamp: '2015-04-26T11:15:34', meal: 'before_meal'}]";
        try
        {
            Type listType = new TypeToken<ArrayList<Readings>>(){}.getType();
            readingsList = new Gson().fromJson(json, listType);
            for (Readings r:readingsList) {
                Log.d("item",r.getBg_value()+" "+r.getMeal()+" "+r.getTimeStamp());
            }
            //to sort the readingsList for timestamp
            Collections.sort(readingsList, new Comparator<Readings>()
            {
                public int compare(Readings o1, Readings o2) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date d1 = sdf.parse(o1.getTimeStamp());
                        Date d2 = sdf.parse(o2.getTimeStamp());
                        return d1.compareTo(d2);
                    }
                    catch (Exception e)
                    {
                        Log.d("Exception",e.getMessage());
                        return 5;
                    }
                }
            });
            mAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            Log.e("Error",e.getMessage());
        }
    }
    // returns formatted label for sticky header
    public static String getFormattedLabel(String strStartDate, String strEndDate)
    {
        Date startDate=null,endDate=null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM, d yyyy");
        try {
            startDate=formatter.parse(strStartDate);
            endDate=formatter.parse(strEndDate);
        }
        catch (Exception e)
        {
            Log.e("Exception",e.getMessage());
        }
        return outputFormat.format(startDate)+" - "+outputFormat.format(endDate);
    }
}