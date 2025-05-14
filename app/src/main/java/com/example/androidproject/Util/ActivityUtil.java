package com.example.androidproject.Util;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityUtil {

    public static void enableUpButton(AppCompatActivity activity) {
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) actionbar.setDisplayHomeAsUpEnabled(true);
    }

}
