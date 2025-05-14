package com.example.androidproject.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.Util.ActivityUtil;

/**
 * Base class for activities of which the back button should retain
 * intent data, and not be recreated.
 *
 * (AndroidManifest.xml contains android:parentActivityName only for the assignment)
 */
public abstract class ActivityWithBackButton extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityUtil.enableUpButton(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
