package com.example.sonam.glookoapp;

import android.util.Log;

import java.security.spec.ECField;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sonam on 3/14/2017.
 */

public class Readings  {
    private String bg_value,meal;
    private String timeStamp;

    public Readings(String bg_value,String timeStamp,String meal)
    {
        this.bg_value=bg_value;
        this.timeStamp=timeStamp;
        this.meal=meal;
    }
    public String getBg_value() {
        return bg_value;
    }

    public void setBg_value(String bg_value) {
        this.bg_value = bg_value;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getTimeStamp() {
        return timeStamp;

    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


}
